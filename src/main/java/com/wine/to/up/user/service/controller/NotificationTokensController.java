package com.wine.to.up.user.service.controller;

import com.wine.to.up.commonlib.security.AuthenticationProvider;
import com.wine.to.up.user.service.domain.dto.NotificationTokenDto;
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
import org.springframework.web.bind.annotation.*;

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

    @ApiOperation(value = "Remove app token from list",
            notes = "Description: Removes application token from notification tokens list of currently logged in user",
            authorizations = {@Authorization(value = "jwtToken")})
    @DeleteMapping(path = "/")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> removeNotificationToken(@RequestParam String token) {
        final NotificationTokenDto byId = notificationTokensService.getById(token);
        if (!byId.getUserId().equals(AuthenticationProvider.getUser().getId())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        notificationTokensService.removeNotificationToken(token);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
