package com.wine.to.up.user.service.domain.response.mapper;

import com.wine.to.up.user.service.domain.dto.UserDto;
import com.wine.to.up.user.service.domain.response.UserResponse;

public class UserDtoToUserResponseMapper {
    public static UserResponse getUserResponse(UserDto user) {
        UserResponse userResponse =  new UserResponse();

        userResponse.setId(user.getId());
        userResponse.setNumber(user.getPhoneNumber());
        userResponse.setRole(user.getRole().getName());

        return userResponse;
    }
}
