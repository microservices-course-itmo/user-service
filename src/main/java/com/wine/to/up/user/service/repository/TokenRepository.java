package com.wine.to.up.user.service.repository;

import com.wine.to.up.user.service.domain.entity.Token;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TokenRepository extends CrudRepository<Token, Long> {
    List<Token> findByUserId(Long userId);

    Token findByRefreshToken(String refreshToken);

    Token findByAccessToken(String accessToken);
}
