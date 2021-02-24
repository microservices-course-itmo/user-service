package com.wine.to.up.user.service.repository;

import com.wine.to.up.user.service.domain.entity.NotificationToken;
import com.wine.to.up.user.service.domain.entity.NotificationTokenType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationTokensRepository extends JpaRepository<NotificationToken, String> {
    List<NotificationToken> findAllByUserIdAndTokenType(Long userId, NotificationTokenType tokenType);
    void deleteByToken(String token);
}
