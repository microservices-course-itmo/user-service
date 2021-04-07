package com.wine.to.up.user.service.repository;

import com.wine.to.up.user.service.domain.entity.NotificationToken;
import com.wine.to.up.user.service.domain.entity.NotificationTokenType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationTokensRepository extends JpaRepository<NotificationToken, String> {
    List<NotificationToken> findAllByUserIdAndTokenType(Long userId, NotificationTokenType tokenType);
    void deleteByTokenAndUserId(String token, Long userId);
    List<NotificationToken> findAllByUserId(Long userId);
    boolean existsByTokenAndUserId(String token, Long userId);
}
