package com.wine.to.up.user.service.service;

import com.wine.to.up.user.service.api.dto.UserTokens;
import com.wine.to.up.user.service.api.dto.WinePriceUpdatedResponse;
import com.wine.to.up.user.service.domain.dto.ItemDto;
import com.wine.to.up.user.service.domain.dto.UserDto;
import com.wine.to.up.user.service.domain.dto.UserSubscriptionDto;
import com.wine.to.up.user.service.domain.entity.Item;
import com.wine.to.up.user.service.domain.entity.User;
import com.wine.to.up.user.service.domain.entity.UserSubscription;
import com.wine.to.up.user.service.repository.UserSubscriptionsRepository;
import java.util.ArrayList;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SubscriptionService
    extends AbstractService<String, UserSubscriptionDto, UserSubscription, UserSubscriptionsRepository> {
    private final UserService userService;
    private final ItemService itemService;

    @Autowired
    public SubscriptionService(UserSubscriptionsRepository repository, ModelMapper modelMapper,
                               UserService userService, ItemService itemService) {
        super(repository, modelMapper);
        this.userService = userService;
        this.itemService = itemService;
    }

    @Override
    public Class<UserSubscription> getEntityClass() {
        return UserSubscription.class;
    }

    @Override
    public Class<UserSubscriptionDto> getDTOClass() {
        return UserSubscriptionDto.class;
    }

    public List<UserSubscriptionDto> findUsersByWineId(String id) {
        List<UserSubscriptionDto> listSubscriptionDtoList = new ArrayList<>();
        for (UserSubscription listSubscriptionDto : repository.findAllByItemId(id)) {
            listSubscriptionDtoList.add(modelMapper.map(listSubscriptionDto, getDTOClass()));
        }
        return listSubscriptionDtoList;
    }

    // todo remove and add getFCM & get IOS tokens
    public WinePriceUpdatedResponse getPushTokensByWineId(String id) {
        WinePriceUpdatedResponse response = new WinePriceUpdatedResponse();
        List<UserSubscriptionDto> listSubscription = this.findUsersByWineId(id);
        List<UserTokens> users = new ArrayList<>();
        response.setWineId(id);
        for (UserSubscriptionDto listSubscriptionDto : listSubscription) {
            UserTokens userTokens = new UserTokens();
            userTokens.setUserId(listSubscriptionDto.getUser().getId());
            users.add(userTokens);
        }
        response.setUserTokens(users);
        return response;
    }

    public List<Long> findUserIdsByWineId(String id) {
        List<Long> userIds = new ArrayList<>();
        for (UserSubscription listSubscriptionDto : repository.findAllByItemId(id)) {
            userIds.add(listSubscriptionDto.getUser().getId());
        }
        return userIds;
    }

    public List<UserDto> getUsersByItemId(String id) {
        List<UserDto> userDtoList = new ArrayList<>();
        for (UserSubscription userSubscription : repository.findAllByItemId(id)) {
            userDtoList.add(modelMapper.map(userSubscription.getUser(), userService.getDTOClass()));
        }
        return userDtoList;
    }

    public void removeUserSubscription(String itemId, Long userId) {
        User user = userService.getUserById(userId);
        Item item = itemService.getItemById(itemId);
        repository.deleteByItemAndUser(item, user);
    }

    public void addUserSubscription(String itemId, Long userId) {
        User user = userService.getUserById(userId);
        Item item = itemService.getItemById(itemId);
        if (item == null) {
            itemService.create(new ItemDto().setId(itemId));
            item = itemService.getItemById(itemId);
        }
        repository.save(new UserSubscription(user, item));
    }

    public List<ItemDto> getItemsByUserId(Long userId) {
        List<ItemDto> itemDtoList = new ArrayList<>();
        for (UserSubscription userSubscription : repository.findAllByUserId(userId)) {
            itemDtoList.add(modelMapper.map(userSubscription.getItem(), itemService.getDTOClass()));
        }
        return itemDtoList;
    }
}
