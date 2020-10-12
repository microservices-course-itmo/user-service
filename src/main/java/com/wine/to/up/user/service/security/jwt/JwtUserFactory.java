package com.wine.to.up.user.service.security.jwt;

import com.wine.to.up.user.service.domain.entity.Role;
import com.wine.to.up.user.service.domain.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;

public final class JwtUserFactory {

    public JwtUserFactory() {
    }

    public static JwtUser create(User user) {
        return new JwtUser(user.getId(),
                           user.getPhoneNumber(),
                           user.getPassword(),
                           user.getEmail(),
                           user.getIsActivated(),
                           mapToGrantedAuthority(user.getRole()));
    }

    private static List<GrantedAuthority> mapToGrantedAuthority(Role userRole){
        return Collections.singletonList(new SimpleGrantedAuthority(userRole.getName()));
    }
}
