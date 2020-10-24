package com.wine.to.up.user.service.repository;

import com.wine.to.up.user.service.domain.entity.UserSubscriptions;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSubscriptionsRepository extends CrudRepository<UserSubscriptions, Long> {
    Iterable<UserSubscriptions> findAllByItemId(Long itemId);
}
