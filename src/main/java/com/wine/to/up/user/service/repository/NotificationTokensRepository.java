package com.wine.to.up.user.service.repository;

import com.wine.to.up.user.service.domain.entity.NotificationToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationTokensRepository extends CrudRepository<NotificationToken, String> {
    Iterable<NotificationToken> findAllByUserId(Long userId);
    //Iterable<NotificationToken> findTokensByUserIdAndType(Long userId, NotificationTokenType tokenType);
}
