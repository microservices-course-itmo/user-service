package com.wine.to.up.user.service.security;

import com.wine.to.up.user.service.domain.entity.User;
import com.wine.to.up.user.service.security.jwt.JwtUser;
import com.wine.to.up.user.service.security.jwt.JwtUserFactory;
import com.wine.to.up.user.service.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class JwtUserDetailsService implements UserDetailsService {
    private final UserService userService;

    @Autowired
    public JwtUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    public UserDetails loadUserByPhoneNumber(String phoneNumber) throws UsernameNotFoundException {
        User user = userService.getByPhoneNumber(phoneNumber);

        JwtUser jwtUser = JwtUserFactory.create(user);
        return jwtUser;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
