package com.wine.to.up.user.service.repository;

import com.wine.to.up.user.service.domain.entity.Item;
import com.wine.to.up.user.service.domain.entity.User;
import com.wine.to.up.user.service.domain.entity.UserSubscription;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSubscriptionsRepository extends CrudRepository<UserSubscription, String> {
    Iterable<UserSubscription> findAllByItemId(String itemId);
    Iterable<UserSubscription> findAllByUserId(Long userId);
    void deleteByItemAndUser(Item item, User user);
}
