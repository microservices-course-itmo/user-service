package com.wine.to.up.user.service.configuration;

import com.wine.to.up.user.service.api.dto.UserResponse;
import com.wine.to.up.user.service.domain.dto.UserDto;
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
    }

    private void configureUserResponseMapping() {
        TypeMap<UserDto, UserResponse> typeMap =
            modelMapper.createTypeMap(UserDto.class, UserResponse.class);

        typeMap.addMapping(src -> src.getRole().getName(), UserResponse::setRole);
        typeMap.addMapping(src -> src.getCity().getId(), UserResponse::setCityId);
    }
}
