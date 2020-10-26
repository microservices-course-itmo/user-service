package com.wine.to.up.user.service.controller;

import com.wine.to.up.user.service.domain.dto.ListWineUserDto;
import com.wine.to.up.user.service.domain.dto.UserDto;
import com.wine.to.up.user.service.service.ListSubscriptionService;
import com.wine.to.up.user.service.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> findUserByID(@PathVariable Long id) {
        UserDto user = userService.getById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/{id}/subscriptions")
    public ResponseEntity<ListWineUserDto> findUserTokensByWine(@PathVariable Long id) {
        ListWineUserDto listWineUsers = listSubscriptionService.getUserTokens(id);
        return new ResponseEntity<>(listWineUsers, HttpStatus.OK);
    }
}
