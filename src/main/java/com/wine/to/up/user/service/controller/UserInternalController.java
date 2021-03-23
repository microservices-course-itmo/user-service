package com.wine.to.up.user.service.controller;

import com.wine.to.up.user.service.api.dto.UserResponse;
import com.wine.to.up.user.service.controller.util.ApiPageParams;
import com.wine.to.up.user.service.domain.dto.ItemDto;
import com.wine.to.up.user.service.domain.dto.NotificationTokenDto;
import com.wine.to.up.user.service.domain.dto.PagedUserResponse;
import com.wine.to.up.user.service.domain.dto.UserAllFavoritesDto;
import com.wine.to.up.user.service.service.FavoritesService;
import com.wine.to.up.user.service.service.NotificationTokensService;
import com.wine.to.up.user.service.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/internal/users")
@RequiredArgsConstructor
public class UserInternalController {
    private final UserService userService;
    private final NotificationTokensService tokensService;
    private final FavoritesService favoritesService;

    @GetMapping
    @ApiOperation(value = "Get all users batch")
    @ApiPageParams
    public PagedUserResponse batchGet(@RequestParam Integer size, @RequestParam Integer page) {
        final PageRequest pageRequest = PageRequest.of(page, size);

        final Page<UserResponse> userResponsePage = userService.findAll(pageRequest);
        final PagedUserResponse response = new PagedUserResponse();
        response.setContent(userResponsePage.getContent());
        response.setSize(userResponsePage.getSize());
        response.setPage(userResponsePage.getPageable().getPageNumber());
        response.setTotal(userResponsePage.getTotalElements());
        response.setTotalPages(userResponsePage.getTotalPages());

        return response;
    }

    @GetMapping("/{id}/tokens")
    @ApiOperation(value = "Get all users' tokens")
    public List<NotificationTokenDto> getUserTokens(@PathVariable Long id) {
        return tokensService.getAllByUserId(id);
    }


    @ApiOperation(value = "All users with their favourites items",
        notes = "Description: Returns all users with their favourites items",
        response = UserAllFavoritesDto.class,
        responseContainer = "List"
    )
    @GetMapping(path = "/favorites")
    public ResponseEntity<List<UserAllFavoritesDto>> getFavoritesForAllUsers() {
        List<UserAllFavoritesDto> favoritesForAllUsers = userService.getAll().stream()
            .map(userDto -> {
                final UserAllFavoritesDto favoritesDto = new UserAllFavoritesDto();
                favoritesDto.setUserId(userDto.getId());
                favoritesDto.setFavoriteIds(
                    favoritesService.getItemsByUserId(userDto.getId()).stream()
                        .map(ItemDto::getId)
                        .collect(Collectors.toList())
                );
                return favoritesDto;
            })
            .collect(Collectors.toList());

        return new ResponseEntity<>(favoritesForAllUsers, HttpStatus.OK);
    }
}
