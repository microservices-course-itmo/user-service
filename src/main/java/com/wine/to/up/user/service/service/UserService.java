package com.wine.to.up.user.service.service;

import com.wine.to.up.user.service.domain.dto.UserDto;
import com.wine.to.up.user.service.domain.dto.UserRegistrationDto;
import com.wine.to.up.user.service.domain.entity.User;
import com.wine.to.up.user.service.exception.EntityNotFoundException;
import com.wine.to.up.user.service.repository.UserRepository;
import java.time.Instant;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        userDto.setRole(roleService.getByName("USER"));
        userDto.setPhoneNumber(userRegistrationDto.getPhoneNumber());
        userDto.setIsActivated(true);
        userDto.setCreateDate(Instant.now());
        return this.create(userDto);
    }

    @Transactional(readOnly = true)
    public UserDto getByPhoneNumber(String phoneNumber) {
        User user = repository.findByPhoneNumber(phoneNumber);
        if (user == null) {
            throw new EntityNotFoundException("User", phoneNumber);
        }

        return modelMapper.map(user, getDTOClass());
    }

    public boolean existsByPhoneNumber(String phoneNumber) {
        return repository.findByPhoneNumber(phoneNumber) != null;
    }
}
