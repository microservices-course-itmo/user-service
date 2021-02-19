package com.wine.to.up.user.service.controller;

import com.wine.to.up.commonlib.security.AuthenticationProvider;
import com.wine.to.up.user.service.domain.entity.NotificationToken;
import com.wine.to.up.user.service.domain.entity.NotificationTokenType;
import com.wine.to.up.user.service.service.NotificationTokensService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notification_tokens")
@Slf4j
@Api(value = "NotificationTokensController", description = "Operations with notification tokens")
public class NotificationTokensController {
    public final NotificationTokensService notificationTokensService;

    @ApiOperation(value = "Add token to list",
            notes = "Description: Adds application token to notification tokens list of currently logged in user")
    @PostMapping(path = "/")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> addUserNotificationToken(@RequestParam String token, @RequestParam NotificationTokenType tokenType) {
        notificationTokensService.addNotificationToken(AuthenticationProvider.getUser().getId(), token, tokenType);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/{userId}")
    @ApiOperation(value = "Add token to list",
            notes = "Description: Adds application token to notification tokens list")

    public ResponseEntity<Void> addUserNotificationTokenById(@PathVariable Long userId, @RequestParam String token,
                                                         @RequestParam NotificationTokenType tokenType) {
        notificationTokensService.addNotificationToken(userId, token, tokenType);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "Remove app token from list",
            notes = "Description: Removes application token from notification tokens list of currently logged in user")
    @DeleteMapping(path = "/")
    public ResponseEntity<Void> removeNotificationToken(@RequestParam String token) {
        notificationTokensService.removeNotificationToken(token);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
