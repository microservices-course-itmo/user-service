package com.wine.to.up.user.service.controller;

import com.wine.to.up.user.service.api.dto.UserResponse;
import com.wine.to.up.user.service.domain.dto.UserDto;
import com.wine.to.up.user.service.service.ListSubscriptionService;
import com.wine.to.up.user.service.service.UserService;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Slf4j
public class UserController {
    public final UserService userService;
    public final ListSubscriptionService listSubscriptionService;
    public final ModelMapper modelMapper;

    @GetMapping("/{id}/full")
    public ResponseEntity<UserDto> findUserByID(@PathVariable Long id) {
        UserDto user = userService.getById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findUserInfoByID(@PathVariable Long id) {
        return new ResponseEntity<>(modelMapper.map(userService.getById(id), UserResponse.class), HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> findCurrentUserInfo(HttpServletRequest httpServletRequest) {
        return new ResponseEntity<>(
            modelMapper.map(userService.getCurrentUserInfo(httpServletRequest), UserResponse.class),
            HttpStatus.OK
        );
    }
}
