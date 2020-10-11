package com.wine.to.up.user.service.repository;

import com.wine.to.up.user.service.domain.entity.ListFavorite;
import com.wine.to.up.user.service.domain.entity.ListSubscription;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ListSubscriptionRepository extends CrudRepository<ListSubscription, Long> {
    ListSubscription findByItemId(Long itemId);
}
