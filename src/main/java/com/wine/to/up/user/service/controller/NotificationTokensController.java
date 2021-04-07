package com.wine.to.up.user.service.controller;

import com.wine.to.up.commonlib.security.AuthenticationProvider;
import com.wine.to.up.user.service.domain.entity.NotificationTokenType;
import com.wine.to.up.user.service.service.NotificationTokensService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notification_tokens")
@Slf4j
@Api(value = "NotificationTokensController", description = "Operations with notification tokens")
public class NotificationTokensController {
    private final NotificationTokensService notificationTokensService;

    @ApiOperation(value = "Add token to list",
            notes = "Description: Adds application token to notification tokens list of currently logged in user",
            authorizations = {@Authorization(value = "jwtToken")})
    @PostMapping(path = "/")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> addUserNotificationToken(
            @RequestParam String token,
            @RequestParam NotificationTokenType tokenType
    ) {
        notificationTokensService.addNotificationToken(AuthenticationProvider.getUser().getId(), token, tokenType);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/{userId}")
    @ApiOperation(value = "Add token to list",
            notes = "Description: Adds application token to notification tokens list",
            authorizations = {@Authorization(value = "jwtToken")})
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> addUserNotificationTokenById(
            @PathVariable Long userId,
            @RequestParam String token,
            @RequestParam NotificationTokenType tokenType
    ) {
        notificationTokensService.addNotificationToken(userId, token, tokenType);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/")
    @ApiOperation(value = "Checks if user has specified token", authorizations = @Authorization(value = "jwtToken"))
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Map<String, Object>> checkUserToken(@RequestParam String token) {
        final Long userId = AuthenticationProvider.getUser().getId();
        final boolean exists = notificationTokensService.existsByTokenAndUserId(token, userId);

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("exists", exists);

        return ResponseEntity.ok(response);
    }

    @ApiOperation(value = "Remove app token from list",
            notes = "Description: Removes application token from notification tokens list of currently logged in user",
            authorizations = {@Authorization(value = "jwtToken")})
    @DeleteMapping(path = "/")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> removeNotificationToken(@RequestParam String token) {
        final Long userId = AuthenticationProvider.getUser().getId();
        notificationTokensService.removeNotificationToken(token, userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
