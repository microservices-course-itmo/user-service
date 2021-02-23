package com.wine.to.up.user.service.controller;

import com.wine.to.up.commonlib.security.AuthenticationProvider;
import com.wine.to.up.user.service.api.dto.UserResponse;
import com.wine.to.up.user.service.domain.dto.ItemDto;
import com.wine.to.up.user.service.service.FavoritesService;
import com.wine.to.up.user.service.service.ItemService;
import com.wine.to.up.user.service.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/favorites")
@Slf4j
@Api(value = "UserController", description = "Operations with favorites lists")
public class FavoritesController {
    public final ItemService itemService;
    public final UserService userService;
    public final FavoritesService favoritesService;
    public final ModelMapper modelMapper;

    @ApiOperation(value = "Users with wine position in favorites",
            notes = "Description: Returns users having in their favorites list wine position with ID",
            response = UserResponse.class,
            responseContainer = "List",
            authorizations = {@Authorization(value = "jwtToken")})
    @GetMapping(path = "/{itemId}/users", produces = "application/json")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<UserResponse>> findUsersByItemID(
            @PathVariable String itemId) {
        List<UserResponse> favoritesList = favoritesService.getUsersByItemId(itemId);
        return new ResponseEntity<>(favoritesList, HttpStatus.OK);
    }

    @ApiOperation(value = "User's favorites",
            notes = "Description: Returns favorites list of authenticated user",
            response = ItemDto.class,
            responseContainer = "List",
            authorizations = {@Authorization(value = "jwtToken")})
    @GetMapping("/")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ItemDto>> findUsersFavorites() {
        return new ResponseEntity<>(favoritesService.getItemsByUserId(AuthenticationProvider.getUser().getId()),
                HttpStatus.OK);
    }

    @ApiOperation(value = "User's favorites id list",
            notes = "Description: Returns list of favorites ID of authenticated user",
            response = String.class,
            responseContainer = "List",
            authorizations = {@Authorization(value = "jwtToken")})
    @GetMapping("/list")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<String>> findUsersFavoritesIds() {
        return new ResponseEntity<>(favoritesService.getItemsIdsByUserId(AuthenticationProvider.getUser().getId()),
                HttpStatus.OK);
    }

    @ApiOperation(value = "Remove wine from favorites list",
            notes = "Description: Removes wine position with ID itemId from user's favorites list of authenticated user",
            authorizations = {@Authorization(value = "jwtToken")})
    @DeleteMapping(path = "/{itemId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> removeUserFavoritesItem(@PathVariable String itemId) {
        favoritesService.removeUserFavoritesItem(itemId, AuthenticationProvider.getUser().getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "Clear favorites list",
            notes = "Description: Clears user's favorites list of authenticated user",
            authorizations = {@Authorization(value = "jwtToken")})
    @DeleteMapping(path = "/clear")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> clearUserFavorites() {
        favoritesService.clearUserFavorites(AuthenticationProvider.getUser().getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "Add wine to favorites list",
            notes = "Description: Adds wine position with ID itemId to user's favorites list of authenticated user",
            authorizations = {@Authorization(value = "jwtToken")})
    @PostMapping(path = "/{itemId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> addUserFavoritesItem(@PathVariable String itemId) {
        favoritesService.addUserFavoritesItem(itemId, AuthenticationProvider.getUser().getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
