package com.wine.to.up.user.service.service;

import com.wine.to.up.user.service.api.dto.UserResponse;
import com.wine.to.up.user.service.domain.dto.UserDto;
import com.wine.to.up.user.service.domain.dto.UserRegistrationDto;
import com.wine.to.up.user.service.domain.entity.User;
import com.wine.to.up.user.service.exception.EntityAlreadyExistsException;
import com.wine.to.up.user.service.exception.EntityNotFoundException;
import com.wine.to.up.user.service.repository.UserRepository;
import com.wine.to.up.user.service.security.JwtTokenProvider;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;

@Service
public class UserService extends AbstractService<Long, UserDto, User, UserRepository> {
    private final RoleService roleService;
    private final JwtTokenProvider jwtTokenProvider;
    private final CityService cityService;

    @Autowired
    public UserService(UserRepository repository,
                       ModelMapper modelMapper,
                       RoleService roleService,
                       JwtTokenProvider jwtTokenProvider, CityService cityService) {
        super(repository, modelMapper);
        this.roleService = roleService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.cityService = cityService;
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
        if (repository.findByFirebaseId(userRegistrationDto.getFirebaseId()) != null) {
            throw new EntityAlreadyExistsException("User", "firebase id", userRegistrationDto.getFirebaseId());
        }

        UserDto userDto = new UserDto();
        userDto.setRole(roleService.getByName("USER"));
        userDto.setPhoneNumber(userRegistrationDto.getPhoneNumber());
        userDto.setBirthdate(userRegistrationDto.getBirthDate());
        userDto.setCity(cityService.getById(userRegistrationDto.getCityId()));
        userDto.setName(userRegistrationDto.getName());
        userDto.setIsActivated(true);
        userDto.setCreateDate(Instant.now());
        userDto.setFirebaseId(userRegistrationDto.getFirebaseId());

        return this.create(userDto);
    }

    public UserDto getCurrentUserInfo(HttpServletRequest httpServletRequest) {
        return this.getByPhoneNumber(jwtTokenProvider.getPhoneNumber(
            jwtTokenProvider.resolveToken(httpServletRequest)));
    }

    public UserDto getByPhoneNumber(String phoneNumber) {
        User user = repository.findByPhoneNumber(phoneNumber);
        if (user == null) {
            throw new EntityNotFoundException("User", phoneNumber);
        }

        return modelMapper.map(user, getDTOClass());
    }

    @Transactional(readOnly = true)
    public UserDto getByFirebaseId(String id) {
        User user = repository.findByFirebaseId(id);
        if (user == null) {
            return null;
        }
        return modelMapper.map(user, getDTOClass());
    }

    public boolean existsByPhoneNumber(String phoneNumber) {
        return repository.findByPhoneNumber(phoneNumber) != null;
    }

    public User getUserById(Long id) {
        return repository.findUserById(id);
    }

    public Page<UserResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable)
                         .map(user -> modelMapper.map(user, UserResponse.class));
    }
}
