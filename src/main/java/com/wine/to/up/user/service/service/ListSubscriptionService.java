package com.wine.to.up.user.service.service;

import com.wine.to.up.user.service.domain.dto.ListSubscriptionDto;
import com.wine.to.up.user.service.domain.dto.ListWineUserDto;
import com.wine.to.up.user.service.domain.entity.ListSubscription;
import com.wine.to.up.user.service.domain.entity.UserTokens;
import com.wine.to.up.user.service.repository.ListSubscriptionRepository;
import com.wine.to.up.user.service.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@Transactional
public class ListSubscriptionService extends AbstractService<Long, ListSubscriptionDto, ListSubscription, ListSubscriptionRepository> {
    private final ListSubscriptionRepository listSubscriptionRepository;
    private final UserRepository userRepository;

    @Autowired
    public ListSubscriptionService(ListSubscriptionRepository repository, ModelMapper modelMapper, ListSubscriptionRepository listSubscriptionRepository, UserRepository userRepository) {
        super(repository, modelMapper);
        this.listSubscriptionRepository = listSubscriptionRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Class<ListSubscription> getEntityClass() {
        return ListSubscription.class;
    }

    @Override
    public Class<ListSubscriptionDto> getDTOClass() {
        return ListSubscriptionDto.class;
    }

    public ArrayList<ListSubscriptionDto> findUsersByItemId(Long id) {
        ArrayList<ListSubscriptionDto> listSubscriptionDtoList = new ArrayList<ListSubscriptionDto>();
        for (ListSubscription listSubscriptionDto:
                listSubscriptionRepository.findAllByItemId(id)) {
            listSubscriptionDtoList.add(modelMapper.map(listSubscriptionDto, getDTOClass()));
        }
        return listSubscriptionDtoList;
    }
    public ListWineUserDto getUserTokens(Long id) {
        ListWineUserDto listWineUserDto = new ListWineUserDto();
        ArrayList<ListSubscriptionDto> listSubscription = this.findUsersByItemId(id);
        ArrayList<UserTokens> users = new ArrayList<>();
        listWineUserDto.setWineId(id);
        for (ListSubscriptionDto listSubscriptionDto:
             listSubscription) {
            UserTokens userTokens = new UserTokens();
            userTokens.setUserId(listSubscriptionDto.getUser().getId());
            users.add(userTokens);
        }
        listWineUserDto.setUsers(users);
        return listWineUserDto;
    }
}
