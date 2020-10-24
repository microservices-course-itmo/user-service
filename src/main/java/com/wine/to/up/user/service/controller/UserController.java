package com.wine.to.up.user.service.controller;

import com.wine.to.up.user.service.domain.dto.UserDto;
import com.wine.to.up.user.service.domain.dto.UserRegistrationDto;
import com.wine.to.up.user.service.domain.response.UserResponse;
import com.wine.to.up.user.service.service.ListSubscriptionService;
import com.wine.to.up.user.service.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static com.wine.to.up.user.service.domain.response.mapper.UserDtoToUserResponseMapper.getUserResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Slf4j
public class UserController {
    public final UserService userService;
    public final ListSubscriptionService listSubscriptionService;

    @GetMapping("/{id}/info")
    public ResponseEntity<UserDto> findUserByID(@PathVariable Long id) {
        UserDto user = userService.getById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping
    public void createUser(@RequestBody UserRegistrationDto userRegistrationDto) {
        userService.signUp(userRegistrationDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findUserInfoByID(@PathVariable Long id) { ;
        return new ResponseEntity<>(getUserResponse(userService.getById(id)), HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> findCurrentUserInfo(HttpServletRequest httpServletRequest) {
        return new ResponseEntity<>(getUserResponse(userService.getCurrentUserInfo(httpServletRequest)), HttpStatus.OK);
    }
}
