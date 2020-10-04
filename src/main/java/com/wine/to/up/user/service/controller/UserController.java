package com.wine.to.up.user.service.controller;

import com.wine.to.up.user.service.domain.dto.ListFavoriteDto;
import com.wine.to.up.user.service.domain.dto.UserDto;
import com.wine.to.up.user.service.domain.dto.UserRegistrationDto;
import com.wine.to.up.user.service.service.ListFavoriteService;
import com.wine.to.up.user.service.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Slf4j
public class UserController {
    public final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> findUserByID(@PathVariable Long id) {
        UserDto user = userService.getById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping
    public void createUser(@RequestBody UserRegistrationDto userRegistrationDto) {
        userService.signUp(userRegistrationDto);
    }
}
