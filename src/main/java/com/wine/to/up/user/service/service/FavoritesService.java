package com.wine.to.up.user.service.service;

import com.wine.to.up.user.service.api.dto.UserResponse;
import com.wine.to.up.user.service.api.dto.UserTokens;
import com.wine.to.up.user.service.api.dto.WinePriceUpdatedResponse;
import com.wine.to.up.user.service.domain.dto.ItemDto;
import com.wine.to.up.user.service.domain.dto.UserDto;
import com.wine.to.up.user.service.domain.dto.UserFavoritesDto;
import com.wine.to.up.user.service.domain.entity.Item;
import com.wine.to.up.user.service.domain.entity.User;
import com.wine.to.up.user.service.domain.entity.UserFavorites;
import com.wine.to.up.user.service.exception.EntityNotFoundException;
import com.wine.to.up.user.service.repository.UserFavoritesRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class FavoritesService
    extends AbstractService<String, UserFavoritesDto, UserFavorites, UserFavoritesRepository> {
    private final UserService userService;
    private final ItemService itemService;

    @Autowired
    public FavoritesService(UserFavoritesRepository repository, ModelMapper modelMapper,
                            UserService userService, ItemService itemService) {
        super(repository, modelMapper);
        this.userService = userService;
        this.itemService = itemService;
    }

    @Override
    public Class<UserFavorites> getEntityClass() {
        return UserFavorites.class;
    }

    @Override
    public Class<UserFavoritesDto> getDTOClass() {
        return UserFavoritesDto.class;
    }

    public List<UserFavoritesDto> findUsersByWineId(String id) {
        List<UserFavoritesDto> listSubscriptionDtoList = new ArrayList<>();
        for (UserFavorites listSubscriptionDto : repository.findAllByItemId(id)) {
            listSubscriptionDtoList.add(modelMapper.map(listSubscriptionDto, getDTOClass()));
        }
        return listSubscriptionDtoList;
    }

    // todo remove and add getFCM & get IOS tokens
    public WinePriceUpdatedResponse getPushTokensByWineId(String id) {
        WinePriceUpdatedResponse response = new WinePriceUpdatedResponse();
        List<UserFavoritesDto> listSubscription = this.findUsersByWineId(id);
        List<UserTokens> users = new ArrayList<>();
        response.setWineId(id);
        for (UserFavoritesDto listSubscriptionDto : listSubscription) {
            UserTokens userTokens = new UserTokens();
            userTokens.setUserId(listSubscriptionDto.getUser().getId());
            users.add(userTokens);
        }
        response.setUserTokens(users);
        return response;
    }

    public List<Long> findUserIdsByWineId(String id) {
        List<Long> userIds = new ArrayList<>();
        for (UserFavorites listSubscriptionDto : repository.findAllByItemId(id)) {
            userIds.add(listSubscriptionDto.getUser().getId());
        }
        return userIds;
    }

    public List<UserResponse> getUsersByItemId(String id) {
        List<UserResponse> userDtoList = new ArrayList<>();
        for (UserFavorites userFavorites : repository.findAllByItemId(id)) {
            userDtoList.add(modelMapper.map(userFavorites.getUser(), UserResponse.class));
        }
        return userDtoList;
    }

    public void removeUserFavoritesItem(String itemId, Long userId) {
        User user = userService.getUserById(userId);
        Item item = itemService.getItemById(itemId);
        repository.deleteByItemAndUser(item, user);
    }

    public void clearUserFavorites(Long userId) {
        User user = userService.getUserById(userId);
        repository.deleteAllByUser(user);
    }

    public void addUserFavoritesItem(String itemId, Long userId) {
        UserDto user = userService.getById(userId);
        ItemDto item;
        try {
            item = itemService.getById(itemId);
        } catch (EntityNotFoundException e) {
            item = itemService.create(new ItemDto().setId(itemId));
        }

        create(new UserFavoritesDto(user, item));
    }

    public List<ItemDto> getItemsByUserId(Long userId) {
        List<ItemDto> itemDtoList = new ArrayList<>();
        for (UserFavorites userFavorites : repository.findAllByUserId(userId)) {
            itemDtoList.add(modelMapper.map(userFavorites.getItem(), itemService.getDTOClass()));
        }
        return itemDtoList;
    }

    public List<String> getItemsIdsByUserId(Long userId) {
        List<String> itemIdsList = new ArrayList<>();
        for (UserFavorites userFavorites : repository.findAllByUserId(userId)) {
            itemIdsList.add(userFavorites.getItem().getId());
        }
        return itemIdsList;
    }
}
