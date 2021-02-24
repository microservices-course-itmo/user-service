package com.wine.to.up.user.service.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wine.to.up.user.service.api.dto.UserResponse;
import com.wine.to.up.user.service.domain.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GeneralPurposesBeansConfig {

    /**
     * Model mapper bean
     */
    @Bean
    public ModelMapper getModelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.createTypeMap(User.class, UserResponse.class)
            .addMapping(m -> m.getRole().getName(), UserResponse::setRole);

        return modelMapper;
    }

    /**
     * Object mapper bean
     */
    @Bean
    public ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }
}
