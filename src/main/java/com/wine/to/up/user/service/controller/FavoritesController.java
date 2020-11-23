package com.wine.to.up.user.service.controller;

import com.wine.to.up.user.service.domain.dto.ItemDto;
import com.wine.to.up.user.service.domain.dto.UserDto;
import com.wine.to.up.user.service.service.ItemService;
import com.wine.to.up.user.service.service.FavoritesService;
import com.wine.to.up.user.service.service.UserService;
import java.util.List;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/favorites")
@Slf4j
@Api(value="UserController", description="Operations with favorites lists")
public class FavoritesController {
    public final ItemService itemService;
    public final UserService userService;
    public final FavoritesService favoritesService;
    public final ModelMapper modelMapper;

    @ApiOperation(value = "Users with wine position in favorites",
            notes = "Description: Returns users having in their favorites list wine position with ID",
            response = List.class,
            responseContainer = "ResponseEntity")
    @GetMapping(path = "/{itemId}/users", produces = "application/json")
    public ResponseEntity<List<UserDto>> findUsersByItemID(
            @PathVariable String itemId) {
        List<UserDto> favoritesList = favoritesService.getUsersByItemId(itemId);
        return new ResponseEntity<>(favoritesList, HttpStatus.OK);
    }


    @ApiOperation(value = "User's favorites",
            notes = "Description: Returns favorites list of user by his ID",
            response = List.class,
            responseContainer = "ResponseEntity")
    @GetMapping("/{userId}/favorites")
    public ResponseEntity<List<ItemDto>> findUsersFavorites(@PathVariable Long userId) {
        return new ResponseEntity<>(favoritesService.getItemsByUserId(userId), HttpStatus.OK);
    }

    @ApiOperation(value = "Remove wine from favorites list",
            notes = "Description: Removes wine position with ID itemId from user's favorites list with ID userID")
    @PostMapping(path = "/{userId}/remove/{itemId}")
    public ResponseEntity<Void> removeUserFavoritesItem(
            @PathVariable Long userId,
            @PathVariable String itemId) {
        favoritesService.removeUserFavoritesItem(itemId, userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "Add wine to favorites list",
            notes = "Description: Adds wine position with ID itemId to user's favorites list with ID userID")
    @PostMapping(path = "/{userId}/add/{itemId}")
    public ResponseEntity<Void> addUserFavoritesItem(
            @PathVariable Long userId,
            @PathVariable String itemId) {
        favoritesService.addUserFavoritesItem(itemId, userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
