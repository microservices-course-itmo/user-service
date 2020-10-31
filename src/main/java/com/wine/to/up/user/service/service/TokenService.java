package com.wine.to.up.user.service.service;

import com.wine.to.up.user.service.domain.dto.TokenDto;
import com.wine.to.up.user.service.domain.dto.UserDto;
import com.wine.to.up.user.service.domain.entity.Token;
import com.wine.to.up.user.service.repository.TokenRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TokenService extends AbstractService<Long, TokenDto, Token, TokenRepository> {
    @Autowired
    TokenService(TokenRepository repository, ModelMapper modelMapper) {
        super(repository, modelMapper);
    }

    @Override
    public Class<Token> getEntityClass() {
        return Token.class;
    }

    @Override
    public Class<TokenDto> getDTOClass() {
        return TokenDto.class;
    }

    public List<TokenDto> getTokenDtoList(UserDto userDto) {
        List<Token> tokenList = repository.findByUserId(userDto.getId());

        return mapList(tokenList);
    }

    public TokenDto getTokenDto(String token) {
        Token tokenEntity = repository.findByAccessToken(token);

        if (tokenEntity != null){
            return modelMapper.map(tokenEntity, getDTOClass());
        } else {
            tokenEntity = repository.findByRefreshToken(token);
            return modelMapper.map(tokenEntity, getDTOClass());
        }
    }

    public List<String> getTokensList(UserDto user, boolean getAccessTokens) {
        List<TokenDto> tokenDtoList = getTokenDtoList(user);
        List<String> tokensList = new ArrayList<>();

        for (TokenDto element : tokenDtoList) {
            String token;

            if (getAccessTokens) {
                token = element.getAccessToken();
            } else {
                token = element.getRefreshToken();
            }
            tokensList.add(token);
        }
        return tokensList;
    }

    public boolean isExistsAccessToken(UserDto user, String token) {
        return getTokensList(user, true).contains(token);
    }

    public boolean isExistsRefreshToken(UserDto user, String token) {
        return getTokensList(user, false).contains(token);
    }
}
