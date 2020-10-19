package com.wine.to.up.user.service.service;

import com.wine.to.up.user.service.domain.dto.UserDto;
import com.wine.to.up.user.service.domain.dto.UserRegistrationDto;
import com.wine.to.up.user.service.domain.entity.User;
import com.wine.to.up.user.service.repository.UserRepository;
import com.wine.to.up.user.service.security.JwtTokenProvider;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;

@Service
public class UserService extends AbstractService<Long, UserDto, User, UserRepository> {
    private final RoleService roleService;
    private final JwtTokenProvider jwtTokenProvider;
    @Value("${user.role.user.id}")
    private Long ROLE_USER_ID;

    @Autowired
    public UserService(UserRepository repository,
                       ModelMapper modelMapper,
                       RoleService roleService,
                       JwtTokenProvider jwtTokenProvider) {
        super(repository, modelMapper);
        this.roleService = roleService;
        this.jwtTokenProvider = jwtTokenProvider;
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
        userDto.setRole(roleService.getById(ROLE_USER_ID));
        userDto.setPhoneNumber(userRegistrationDto.getPhoneNumber());
        userDto.setIsActivated(true);
        userDto.setCreateDate(Instant.now());
        return this.create(userDto);
    }

    public UserDto getCurrentUserInfo(HttpServletRequest httpServletRequest) {
        return this.getByPhoneNumber(jwtTokenProvider.getPhoneNumber(
                jwtTokenProvider.resolveToken(httpServletRequest)));
    }

    @Transactional(readOnly = true)
    public UserDto getByPhoneNumber(String phoneNumber) {
        return modelMapper.map(repository.findByPhoneNumber(phoneNumber), getDTOClass());
    }
}
