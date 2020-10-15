package com.wine.to.up.user.service.service;

import com.wine.to.up.user.service.domain.dto.UserDto;
import com.wine.to.up.user.service.domain.dto.UserRegistrationDto;
import com.wine.to.up.user.service.domain.entity.User;
import com.wine.to.up.user.service.domain.response.UserResponse;
import com.wine.to.up.user.service.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class UserService extends AbstractService<Long, UserDto, User, UserRepository> {
    private final RoleService roleService;

    @Autowired
    public UserService(UserRepository repository,
                       ModelMapper modelMapper,
                       RoleService roleService) {
        super(repository, modelMapper);
        this.roleService = roleService;
    }

    @Override
    public Class<User> getEntityClass() {
        return User.class;
    }

    @Override
    public Class<UserDto> getDTOClass() {
        return UserDto.class;
    }

    public UserDto signUp(UserRegistrationDto userRegistrationDto) {
        UserDto userDto = new UserDto();
        userDto.setRole(roleService.getById(1L));
        userDto.setPhoneNumber(userRegistrationDto.getPhoneNumber());
        userDto.setIsActivated(false);
        userDto.setCreateDate(Instant.now());
        return this.create(userDto);
    }

    public UserResponse getUserResponse(User user) {
        UserResponse userResponse =  new UserResponse();

        userResponse.setId(user.getId());
        userResponse.setNumber(user.getPhoneNumber());
        userResponse.setRole(user.getRole());

        return userResponse;
    }

    @Transactional(readOnly = true)
    public User getByPhoneNumber(String phoneNumber) {
        return repository.findByPhoneNumber(phoneNumber);
    }
}
