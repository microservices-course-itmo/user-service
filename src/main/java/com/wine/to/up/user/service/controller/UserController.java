package com.wine.to.up.user.service.controller;

import com.wine.to.up.user.service.domain.dto.UserDto;
import com.wine.to.up.user.service.domain.entity.User;
import com.wine.to.up.user.service.repository.MessageRepository;
import com.wine.to.up.user.service.service.impl.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller of the service
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")

@Slf4j
public class UserController {
    public final UserService userService;

    @GetMapping("/get")
    public ResponseEntity<UserDto> findUserByID(@RequestParam Long id) {
        UserDto user = userService.getById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("register")
    public void createUser(@RequestBody UserDto userData) {
        //log.info(String.format("%s", userData));
        userService.create(userData);
    }
}
