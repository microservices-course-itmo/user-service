package com.wine.to.up.user.service.service;

import com.wine.to.up.user.service.domain.dto.UserDto;
import com.wine.to.up.user.service.domain.dto.UserRegistrationDto;
import com.wine.to.up.user.service.domain.entity.User;
import com.wine.to.up.user.service.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class UserService extends AbstractService<Long, UserDto, User, UserRepository> {
    private final CityService cityService;
    private final CompanyService companyService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository repository, ModelMapper modelMapper, CityService cityService, CompanyService companyService, RoleService roleService, PasswordEncoder passwordEncoder) {
        super(repository, modelMapper);
        this.cityService = cityService;
        this.companyService = companyService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
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
        userDto.setEmail(userRegistrationDto.getEmail());
        userDto.setCity(cityService.getById(userRegistrationDto.getCityId()));
        userDto.setCompany(companyService.getById(userRegistrationDto.getCompanyId()));
        userDto.setRole(roleService.getById(1L));
        userDto.setSex(userRegistrationDto.getSex());
        userDto.setPhoneNumber(userRegistrationDto.getPhoneNumber());
        userDto.setPassword(passwordEncoder.encode(userRegistrationDto.getPassword()));
        userDto.setBirthDate(userRegistrationDto.getBirthDate());
        userDto.setIsActivated(false);
        userDto.setCreateDate(Instant.now());
        return userDto;
    }
}
