package com.wine.to.up.user.service.controller;

import com.wine.to.up.user.service.domain.dto.ListWineUserDto;
import com.wine.to.up.user.service.domain.dto.UserDto;
import com.wine.to.up.user.service.domain.dto.UserRegistrationDto;
import com.wine.to.up.user.service.service.ListSubscriptionService;
import com.wine.to.up.user.service.service.UserService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Slf4j
@Api(value="UserController", description="Operations with users and their subscriptions")
public class UserController {
    public final UserService userService;
    public final ListSubscriptionService listSubscriptionService;

    @ApiOperation(value = "Find user by id",
                    notes = "Description: Returns user and httpStatus OK or error code",
                    response = UserDto.class,
                    responseContainer = "ResponseEntity")
    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<UserDto> findUserByID(
            @ApiParam(name = "id", value = "User's ID", required = true)
            @PathVariable Long id) {
        UserDto user = userService.getById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @ApiOperation(value = "Create user", notes = "Creates new user")
//    @ApiImplicitParams( value = {
//            @ApiImplicitParam(name = "birthDate", dataType = "java.time.LocalDate", paramType = "userRegistrationDto", value = "birth date", required = true),
//            @ApiImplicitParam(name = "sex", dataType = "string", paramType = "userRegistrationDto", value = "male/female", required = true),
//            @ApiImplicitParam(name = "email", dataType = "string", paramType = "userRegistrationDto", value = "email address", required = true, format = ".*@.*//..*")}
//    )
    @PostMapping(produces = "application/json")
    public void createUser(@ApiParam(name = "User Registration Dto", value = "new user's info", required = true)
                               @RequestBody UserRegistrationDto userRegistrationDto) {
        userService.signUp(userRegistrationDto);
    }

    @ApiOperation(value = "find users By Wine", notes = "Finds all subscribed users")
    @GetMapping(value = "/{id}/subscriptions", produces = "application/json")
    public ResponseEntity<ListWineUserDto> findUserTokensByWine(@ApiParam(name = "id", value = " User's ID" , required = true)
                                                                    @PathVariable Long id) {
        ListWineUserDto listWineUsers = listSubscriptionService.getUserTokens(id);
        return new ResponseEntity<>(listWineUsers, HttpStatus.OK);
    }
}
