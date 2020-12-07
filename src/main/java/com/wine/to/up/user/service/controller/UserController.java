package com.wine.to.up.user.service.controller;

import com.wine.to.up.commonlib.security.AuthenticationProvider;
import com.wine.to.up.user.service.api.dto.UserResponse;
import com.wine.to.up.user.service.domain.dto.UserDto;
import com.wine.to.up.user.service.service.UserService;
import io.swagger.annotations.*;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Slf4j
@Api(value="UserController", description="Operations with users")
public class UserController {
    public final UserService userService;
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
    public ResponseEntity<UserResponse> findCurrentUserInfo() {
        return new ResponseEntity<>(
            modelMapper.map(userService.getById(AuthenticationProvider.getUser().getId()), UserResponse.class),
            HttpStatus.OK
        );
    }

    @ApiOperation(value = "Delete user by ID",
            notes = "Description: Removes user with specified ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<UserResponse> deleteUserByID(@PathVariable Long id) {
        userService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
