package com.wine.to.up.user.service.configuration;

import com.wine.to.up.user.service.api.dto.UserResponse;
import com.wine.to.up.user.service.domain.dto.UserDto;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class MappingsConfiguration {
    private final ModelMapper modelMapper;

    @PostConstruct
    private void configure() {
        this.configureUserResponseMapping();
    }

    private void configureUserResponseMapping() {
        TypeMap<UserDto, UserResponse> typeMap =
            modelMapper.createTypeMap(UserDto.class, UserResponse.class);

        typeMap.addMapping(src -> src.getRole().getName(), UserResponse::setRole);
    }
}
