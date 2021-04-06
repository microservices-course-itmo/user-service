package com.wine.to.up.user.service.configuration;

import com.wine.to.up.user.service.api.dto.UserResponse;
import com.wine.to.up.user.service.domain.dto.UserDto;
import com.wine.to.up.user.service.domain.dto.UserFavoritesDto;
import com.wine.to.up.user.service.domain.entity.User;
import com.wine.to.up.user.service.domain.entity.UserFavorites;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
@RequiredArgsConstructor
public class MappingsConfiguration {
    private final ModelMapper modelMapper;

    @PostConstruct
    private void configure() {
        modelMapper.getConfiguration().setAmbiguityIgnored(true)
                                      .setMatchingStrategy(MatchingStrategies.STRICT);
        this.configureUserResponseMapping();
        this.configureFavoritesMapping();
        this.configureUserMapping();
    }

    private void configureUserResponseMapping() {
        TypeMap<UserDto, UserResponse> typeMap =
            modelMapper.createTypeMap(UserDto.class, UserResponse.class);

        typeMap.addMapping(src -> src.getRole().getName(), UserResponse::setRole);
        typeMap.addMapping(src -> src.getCity().getId(), UserResponse::setCityId);
    }

    private void configureFavoritesMapping() {
        TypeMap<UserFavoritesDto, UserFavorites> typeMap =
                modelMapper.createTypeMap(UserFavoritesDto.class, UserFavorites.class);
        typeMap.addMapping(src -> src.getItem().getId(), UserFavorites::setItemId);
        typeMap.addMapping(src -> src.getUser().getId(), UserFavorites::setUserId);
    }

    private void configureUserMapping() {
        TypeMap<User, UserResponse> typeMap =
            modelMapper.createTypeMap(User.class, UserResponse.class);

        typeMap.addMapping(User::getId, UserResponse::setId);
        typeMap.addMapping(src -> src.getCity().getId(), UserResponse::setCityId);
        typeMap.addMapping(src -> src.getRole().getName(), UserResponse::setRole);
    }
}
