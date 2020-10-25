package com.wine.to.up.user.service.service;

import com.wine.to.up.user.service.api.dto.UserTokens;
import com.wine.to.up.user.service.api.dto.WineResponse;
import com.wine.to.up.user.service.domain.dto.UserSubscriptionsDto;
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
    extends AbstractService<Long, UserSubscriptionsDto, UserSubscription, UserSubscriptionsRepository> {
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
    public Class<UserSubscriptionsDto> getDTOClass() {
        return UserSubscriptionsDto.class;
    }

    public List<UserSubscriptionsDto> findUsersByWineId(Long id) {
        List<UserSubscriptionsDto> listSubscriptionDtoList = new ArrayList<>();
        for (UserSubscription listSubscriptionDto : listSubscriptionRepository.findAllByItemId(id)) {
            listSubscriptionDtoList.add(modelMapper.map(listSubscriptionDto, getDTOClass()));
        }
        return listSubscriptionDtoList;
    }

    public WineResponse getPushTokensByWineId(Long id) {
        WineResponse response = new WineResponse();
        List<UserSubscriptionsDto> listSubscription = this.findUsersByWineId(id);
        List<UserTokens> users = new ArrayList<>();
        response.setWineId(id);
        for (UserSubscriptionsDto listSubscriptionDto : listSubscription) {
            UserTokens userTokens = new UserTokens();
            userTokens.setUserId(listSubscriptionDto.getUser().getId());
            users.add(userTokens);
        }
        response.setUserTokens(users);
        return response;
    }
}
