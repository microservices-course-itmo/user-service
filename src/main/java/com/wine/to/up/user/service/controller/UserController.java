package com.wine.to.up.user.service.controller;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.wine.to.up.commonlib.messaging.KafkaMessageSender;
import com.wine.to.up.commonlib.security.AuthenticationProvider;
import com.wine.to.up.user.service.api.dto.UserResponse;
import com.wine.to.up.user.service.api.message.EntityUpdatedMetaOuterClass.EntityUpdatedMeta;
import com.wine.to.up.user.service.api.message.UserUpdatedEventOuterClass;
import com.wine.to.up.user.service.domain.dto.UpdateUserInfoRequest;
import com.wine.to.up.user.service.domain.dto.UserDto;
import com.wine.to.up.user.service.service.CityService;
import com.wine.to.up.user.service.service.FavoritesService;
import com.wine.to.up.user.service.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Slf4j
@Api(value = "UserController", description = "Operations with users")
public class UserController {
    private final UserService userService;
    private final FavoritesService favoritesService;
    private final CityService cityService;
    private final ModelMapper modelMapper;
    private final KafkaMessageSender<UserUpdatedEventOuterClass.UserUpdatedEvent> messageSender;

    @ApiOperation(value = "Find user by id",
        notes = "Description: Returns user and httpStatus OK or error code",
        response = UserDto.class,
        responseContainer = "ResponseEntity")
    @GetMapping(path = "/{id}/full", produces = "application/json")
    public ResponseEntity<UserDto> findUserByID(
        @ApiParam(name = "id", value = "User's ID", required = true)
        @PathVariable Long id) {
        UserDto user = userService.getById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @ApiOperation(value = "User info by ID",
        notes = "Description: Returns information about user by ID",
        response = UserResponse.class,
        responseContainer = "ResponseEntity")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findUserInfoByID(@PathVariable Long id) {
        return new ResponseEntity<>(modelMapper.map(userService.getById(id), UserResponse.class), HttpStatus.OK);
    }

    @ApiOperation(value = "Get info about logged in user", authorizations = {@Authorization(value = "jwtToken")})
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponse> findCurrentUserInfo() {
        return new ResponseEntity<>(
            modelMapper.map(userService.getById(AuthenticationProvider.getUser().getId()), UserResponse.class),
            HttpStatus.OK
        );
    }

    @ApiOperation(
        value = "Change user's info",
        notes = "Description: Updates info about logged in user. Only non-null fields will be updated.",
        authorizations = {@Authorization(value = "jwtToken")}
    )
    @PatchMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponse> updateUserInfo(@RequestBody UpdateUserInfoRequest updatedUser)
        throws FirebaseAuthException {

        UserDto user = userService.getById(AuthenticationProvider.getUser().getId());
        if (updatedUser.getCityId() != null) {
            user.setCity(cityService.getById(updatedUser.getCityId()));
        }
        if (updatedUser.getName() != null) {
            user.setName(updatedUser.getName());
        }
        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        final UserRecord userRecord = firebaseAuth.getUser(user.getFirebaseId());
        if (!userRecord.getPhoneNumber().equals(user.getPhoneNumber())) {
            user.setPhoneNumber(userRecord.getPhoneNumber());
        }
        if (updatedUser.getBirthday() != null) {
            user.setBirthdate(updatedUser.getBirthday());
        }
        userService.update(user);
        messageSender.sendMessage(
                UserUpdatedEventOuterClass.UserUpdatedEvent.newBuilder()
                        .setUserId(user.getId())
                        .setPhoneNumber(user.getPhoneNumber())
                        .setName(user.getName())
                        .setBirthdate(user.getBirthdate().toString())
                        .setCityId(user.getCity().getId())
                        .setMeta(EntityUpdatedMeta.newBuilder()
                                .setOperationTime(new Date().getTime())
                                .setOperationType(EntityUpdatedMeta.Operation.UPDATE)
                                .build())
                        .build()
        );

        return ResponseEntity.ok(modelMapper.map(userService.getById(user.getId()), UserResponse.class));
    }

    @ApiOperation(value = "Delete user by ID",
        notes = "Description: Removes user with specified ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<UserResponse> deleteUserByID(@PathVariable Long id) {
        favoritesService.clearUserFavorites(id);
        userService.deleteById(id);
        messageSender.sendMessage(
                UserUpdatedEventOuterClass.UserUpdatedEvent.newBuilder()
                        .setUserId(id)
                        .setMeta(EntityUpdatedMeta.newBuilder()
                                .setOperationTime(new Date().getTime())
                                .setOperationType(EntityUpdatedMeta.Operation.DELETE)
                                .build())
                        .build()
        );
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
