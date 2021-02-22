package com.wine.to.up.user.service.controller;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.wine.to.up.commonlib.security.AuthenticationProvider;
import com.wine.to.up.user.service.api.dto.UserResponse;
import com.wine.to.up.user.service.domain.dto.UpdateUserInfoRequest;
import com.wine.to.up.user.service.domain.dto.UserDto;
import com.wine.to.up.user.service.service.CityService;
import com.wine.to.up.user.service.service.UserService;
import io.swagger.annotations.*;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Slf4j
@Api(value="UserController", description="Operations with users")
public class UserController {
    public final UserService userService;
    public final CityService cityService;
    public final ModelMapper modelMapper;

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

    @ApiOperation(value = "Get info about logged in user", authorizations = { @Authorization(value="jwtToken") })
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponse> findCurrentUserInfo(HttpServletRequest httpServletRequest) {
        return new ResponseEntity<>(
            modelMapper.map(userService.getCurrentUserInfo(httpServletRequest), UserResponse.class),
            HttpStatus.OK
        );
    }

    @ApiOperation(
        value = "Change user's info",
        notes = "Description: Updates info about logged in user. " +
                "Parameters cityId and name are optional and only not null fields will affect user's record",
        authorizations = { @Authorization(value="jwtToken") }
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
        if (updatedUser.getPhoneNumber() != null) {
            final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            final UserRecord userByPhoneNumber = firebaseAuth.getUserByPhoneNumber(user.getPhoneNumber());
            final UserRecord.UpdateRequest updateRequest = new UserRecord.UpdateRequest(userByPhoneNumber.getUid());
            updateRequest.setPhoneNumber(updatedUser.getPhoneNumber());
            firebaseAuth.updateUser(updateRequest);
            user.setPhoneNumber(updatedUser.getPhoneNumber());
        }
        if (updatedUser.getBirthday() != null) {
            user.setBirthDate(updatedUser.getBirthday());
        }
        userService.update(user);
        return ResponseEntity.ok(modelMapper.map(userService.getById(user.getId()), UserResponse.class));
    }
}
