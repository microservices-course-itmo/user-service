package com.wine.to.up.user.service.service;

import com.wine.to.up.user.service.domain.dto.NotificationTokenDto;
import com.wine.to.up.user.service.domain.entity.NotificationToken;
import com.wine.to.up.user.service.domain.entity.NotificationTokenType;
import com.wine.to.up.user.service.repository.NotificationTokensRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class NotificationTokensService extends AbstractService<String, NotificationTokenDto, NotificationToken, NotificationTokensRepository> {
    @Autowired
    public NotificationTokensService(NotificationTokensRepository repository, ModelMapper modelMapper) {
        super(repository, modelMapper);
    }

    @Override
    public Class<NotificationToken> getEntityClass() {
        return NotificationToken.class;
    }

    @Override
    public Class<NotificationTokenDto> getDTOClass() {
        return NotificationTokenDto.class;
    }

    public void addNotificationToken(Long userId, String token,
                                     NotificationTokenType tokenType) {
        this.create(new NotificationTokenDto(userId, token, tokenType));
    }

    public void removeNotificationToken(String token) {
        repository.deleteByToken(token);
    }

    public List<String> getAllTokensByTypeAndUserId(Long userId, NotificationTokenType tokenType) {
        List<String> tokens = new ArrayList<>();
        for (NotificationToken notificationToken : repository.findAllByUserIdAndTokenType(userId, tokenType)) {
            tokens.add(notificationToken.getToken());
        }
        return tokens;
    }
}
