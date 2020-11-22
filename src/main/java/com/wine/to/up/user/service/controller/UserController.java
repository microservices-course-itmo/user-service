package com.wine.to.up.user.service.controller;

import com.wine.to.up.user.service.api.dto.AuthenticationResponse;
import com.wine.to.up.user.service.api.dto.UserResponse;
import com.wine.to.up.user.service.domain.dto.ItemDto;
import com.wine.to.up.user.service.domain.dto.UserDto;
import com.wine.to.up.user.service.service.SubscriptionService;
import com.wine.to.up.user.service.service.UserService;
import io.swagger.annotations.*;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Slf4j
@Api(value="UserController", description="Operations with users and their subscriptions")
public class UserController {
    public final UserService userService;
    public final SubscriptionService subscriptionService;
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
            notes = "Description: REturns information about user by ID",
            response = UserResponse.class,
            responseContainer = "ResponseEntity")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findUserInfoByID(@PathVariable Long id) {
        return new ResponseEntity<>(modelMapper.map(userService.getById(id), UserResponse.class), HttpStatus.OK);
    }

    @ApiOperation(value = "Get info about logged in user", authorizations = { @Authorization(value="jwtToken") })
    @GetMapping("/me")
    public ResponseEntity<UserResponse> findCurrentUserInfo(HttpServletRequest httpServletRequest) {
        return new ResponseEntity<>(
            modelMapper.map(userService.getCurrentUserInfo(httpServletRequest), UserResponse.class),
            HttpStatus.OK
        );
    }

    @ApiOperation(value = "User's subscriptions by",
            notes = "Description: Returns subscription of user by his ID",
            response = List.class,
            responseContainer = "ResponseEntity")
    @GetMapping("/{id}/subscriptions")
    public ResponseEntity<List<ItemDto>> findUsersSubscriptions(@PathVariable Long id) {
        return new ResponseEntity<>(subscriptionService.getItemsByUserId(id), HttpStatus.OK);
    }

    @ApiOperation(value = "Unsubscribe from wine",
            notes = "Description: Removes subscription to wine by itemID from user by userID")
    @PostMapping(path = "/{userId}/unsubscribe/{itemId}")
    public ResponseEntity<Void> removeUserSubscription(
            @PathVariable Long userId,
            @PathVariable String itemId) {
        subscriptionService.removeUserSubscription(itemId, userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "Subscribe to wine",
            notes = "Description: Adds subscription to wine by itemID from user by userID")
    @PostMapping(path = "/{userId}/subscribe/{itemId}")
    public ResponseEntity<Void> addUserSubscription(
            @PathVariable Long userId,
            @PathVariable String itemId) {
        subscriptionService.addUserSubscription(itemId, userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
