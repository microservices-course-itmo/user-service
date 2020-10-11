package com.wine.to.up.user.service.service;

import com.wine.to.up.user.service.domain.dto.ListSubscriptionDto;
import com.wine.to.up.user.service.domain.dto.ListWineUserDto;
import com.wine.to.up.user.service.domain.entity.ListSubscription;
import com.wine.to.up.user.service.repository.ListSubscriptionRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ListSubscriptionService extends AbstractService<Long, ListSubscriptionDto, ListSubscription, ListSubscriptionRepository> {
    private final ListSubscriptionRepository listSubscriptionRepository;

    @Autowired
    public ListSubscriptionService(ListSubscriptionRepository repository, ModelMapper modelMapper, ListSubscriptionRepository listSubscriptionRepository) {
        super(repository, modelMapper);
        this.listSubscriptionRepository = listSubscriptionRepository;
    }

    @Override
    public Class<ListSubscription> getEntityClass() {
        return ListSubscription.class;
    }

    @Override
    public Class<ListSubscriptionDto> getDTOClass() {
        return ListSubscriptionDto.class;
    }

    public ListSubscription findByItemId(Long id) {
        System.out.println(id);
        return listSubscriptionRepository.findByItemId(id);
    }
    public ListWineUserDto getUserTokens(ListSubscription listSubscription) {
        ListWineUserDto listWineUserDto = new ListWineUserDto();
        listWineUserDto.setWineId(listSubscription.getItem().getId());
        listWineUserDto.setUser(listSubscription.getUser());
        return listWineUserDto;
    }
}
