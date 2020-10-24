package com.wine.to.up.user.service.service;

import com.wine.to.up.user.service.domain.dto.UserSubscriptionsDto;
import com.wine.to.up.user.service.domain.entity.UserSubscriptions;
import com.wine.to.up.user.service.domain.entity.UserTokens;
import com.wine.to.up.user.service.domain.response.WineNotificationMessage;
import com.wine.to.up.user.service.repository.UserSubscriptionsRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ListSubscriptionService extends AbstractService<Long, UserSubscriptionsDto, UserSubscriptions, UserSubscriptionsRepository> {
    private final UserSubscriptionsRepository listSubscriptionRepository;

    @Autowired
    public ListSubscriptionService(UserSubscriptionsRepository repository, ModelMapper modelMapper, UserSubscriptionsRepository listSubscriptionRepository) {
        super(repository, modelMapper);
        this.listSubscriptionRepository = listSubscriptionRepository;
    }

    @Override
    public Class<UserSubscriptions> getEntityClass() {
        return UserSubscriptions.class;
    }

    @Override
    public Class<UserSubscriptionsDto> getDTOClass() {
        return UserSubscriptionsDto.class;
    }

    public List<UserSubscriptionsDto> findUsersByWineId(Long id) {
        List<UserSubscriptionsDto> listSubscriptionDtoList = new ArrayList<>();
        for (UserSubscriptions listSubscriptionDto :
                listSubscriptionRepository.findAllByItemId(id)) {
            listSubscriptionDtoList.add(modelMapper.map(listSubscriptionDto, getDTOClass()));
        }
        return listSubscriptionDtoList;
    }

    public WineNotificationMessage getUserTokens(Long id) {
        WineNotificationMessage wineNotificationMessage = new WineNotificationMessage();
        List<UserSubscriptionsDto> listSubscription = this.findUsersByWineId(id);
        List<UserTokens> users = new ArrayList<>();
        wineNotificationMessage.setWineId(id);
        for (UserSubscriptionsDto listSubscriptionDto :
                listSubscription) {
            UserTokens userTokens = new UserTokens();
            userTokens.setUserId(listSubscriptionDto.getUser().getId());
            users.add(userTokens);
        }
        wineNotificationMessage.setUsers(users);
        return wineNotificationMessage;
    }
}
