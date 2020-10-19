package com.wine.to.up.user.service.controller;

import com.wine.to.up.user.service.domain.dto.ListWineUserDto;
import com.wine.to.up.user.service.domain.dto.UserDto;
import com.wine.to.up.user.service.domain.dto.UserRegistrationDto;
import com.wine.to.up.user.service.domain.response.UserResponse;
import com.wine.to.up.user.service.service.ListSubscriptionService;
import com.wine.to.up.user.service.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static com.wine.to.up.user.service.security.UserDtoToUserResponseMapper.getUserResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserService userService;
    private final ListSubscriptionService listSubscriptionService;

    @GetMapping("/{id}/allinfo")
    public ResponseEntity<UserDto> findUserInfoByID(@PathVariable Long id) {
        UserDto user = userService.getById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findUserByID(@PathVariable Long id) { ;
        return new ResponseEntity<>(getUserResponse(userService.getById(id)), HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> findCurrentUser(HttpServletRequest httpServletRequest) {
        return new ResponseEntity<>(getUserResponse(userService.getCurrentUserInfo(httpServletRequest)), HttpStatus.OK);
    }

    @PostMapping
    public void createUser(@RequestBody UserRegistrationDto userRegistrationDto) {
        userService.signUp(userRegistrationDto);
    }

    @GetMapping("/{id}/subscriptions")
    public ResponseEntity<ListWineUserDto> findUserTokensByWine(@PathVariable Long id) {
        ListWineUserDto listWineUsers = listSubscriptionService.getUserTokens(id);
        return new ResponseEntity<>(listWineUsers, HttpStatus.OK);
    }
}
