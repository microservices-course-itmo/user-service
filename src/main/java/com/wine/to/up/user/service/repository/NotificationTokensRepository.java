package com.wine.to.up.user.service.repository;

import com.wine.to.up.user.service.domain.entity.NotificationToken;
import com.wine.to.up.user.service.domain.entity.NotificationTokenType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationTokensRepository extends CrudRepository<NotificationToken, String> {
    Iterable<NotificationToken> findAllByUserIdAndTokenType(Long userId, NotificationTokenType tokenType);
    void deleteByToken(String token);
}
