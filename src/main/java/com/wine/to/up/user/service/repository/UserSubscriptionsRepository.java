package com.wine.to.up.user.service.repository;

import com.wine.to.up.user.service.domain.entity.UserSubscription;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSubscriptionsRepository extends CrudRepository<UserSubscription, String> {
    Iterable<UserSubscription> findAllByItemId(String itemId);
}
