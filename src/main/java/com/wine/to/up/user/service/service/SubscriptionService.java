package com.wine.to.up.user.service.service;

import com.wine.to.up.user.service.api.dto.UserTokens;
import com.wine.to.up.user.service.api.dto.WineResponse;
import com.wine.to.up.user.service.domain.dto.UserSubscriptionDto;
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
    extends AbstractService<Long, UserSubscriptionDto, UserSubscription, UserSubscriptionsRepository> {
    private final UserSubscriptionsRepository listSubscriptionRepository;

    @Autowired
    public SubscriptionService(UserSubscriptionsRepository repository, ModelMapper modelMapper,
                               UserSubscriptionsRepository listSubscriptionRepository) {
        super(repository, modelMapper);
        this.listSubscriptionRepository = listSubscriptionRepository;
    }

    @Override
    public Class<UserSubscription> getEntityClass() {
        return UserSubscription.class;
    }

    @Override
    public Class<UserSubscriptionDto> getDTOClass() {
        return UserSubscriptionDto.class;
    }

    public List<UserSubscriptionDto> findUsersByWineId(Long id) {
        List<UserSubscriptionDto> listSubscriptionDtoList = new ArrayList<>();
        for (UserSubscription listSubscriptionDto : listSubscriptionRepository.findAllByItemId(id)) {
            listSubscriptionDtoList.add(modelMapper.map(listSubscriptionDto, getDTOClass()));
        }
        return listSubscriptionDtoList;
    }

    // todo: remove and add getFCM & get IOS tokens
    public WineResponse getPushTokensByWineId(Long id) {
        WineResponse response = new WineResponse();
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

    public List<Long> findUserIdsByWineId(Long id) {
        List<Long> userIds = new ArrayList<>();
        for (UserSubscription listSubscriptionDto : listSubscriptionRepository.findAllByItemId(id)) {
            userIds.add(listSubscriptionDto.getUser().getId());
        }
        return userIds;
    }
}
