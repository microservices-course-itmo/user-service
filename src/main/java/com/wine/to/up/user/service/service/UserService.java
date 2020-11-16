package com.wine.to.up.user.service.service;

import com.wine.to.up.user.service.domain.dto.UserDto;
import com.wine.to.up.user.service.domain.dto.UserRegistrationDto;
import com.wine.to.up.user.service.domain.entity.User;
import com.wine.to.up.user.service.exception.EntityAlreadyExistsException;
import com.wine.to.up.user.service.exception.EntityNotFoundException;
import com.wine.to.up.user.service.repository.UserRepository;
import com.wine.to.up.user.service.security.JwtTokenProvider;
import java.time.Instant;
import javax.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService extends AbstractService<Long, UserDto, User, UserRepository> {
    private final RoleService roleService;
    private final JwtTokenProvider jwtTokenProvider;

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
        if (repository.findByPhoneNumber(userRegistrationDto.getPhoneNumber()) != null) {
            throw new EntityAlreadyExistsException("User", "phone number", userRegistrationDto.getPhoneNumber());
        }

        UserDto userDto = new UserDto();
        userDto.setRole(roleService.getByName("USER"));
        userDto.setPhoneNumber(userRegistrationDto.getPhoneNumber());
        userDto.setBirthDate(userRegistrationDto.getBirthDate());
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
        User user = repository.findByPhoneNumber(phoneNumber);
        if (user == null) {
            throw new EntityNotFoundException("User", phoneNumber);
        }

        return modelMapper.map(user, getDTOClass());
    }

    public boolean existsByPhoneNumber(String phoneNumber) {
        return repository.findByPhoneNumber(phoneNumber) != null;
    }

    public User getUserById(Long id) {
        return repository.findUserById(id);
    }
}
