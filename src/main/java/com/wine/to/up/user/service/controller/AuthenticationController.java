package com.wine.to.up.user.service.controller;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.wine.to.up.user.service.domain.dto.AuthenticationRequestDto;
import com.wine.to.up.user.service.domain.entity.User;
import com.wine.to.up.user.service.exception.EntityNotFoundException;
import com.wine.to.up.user.service.security.jwt.JwtTokenProvider;
import com.wine.to.up.user.service.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class AuthenticationController {

    private final JwtTokenProvider jwtTokenProvider;

    private final UserService userService;

    @Autowired
    public AuthenticationController(JwtTokenProvider jwtTokenProvider,
                                    UserService userService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody AuthenticationRequestDto requestDto){
        try {
            String idToken = requestDto.getFireBaseToken();
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
            String phoneNumber = (String) decodedToken.getClaims().get("phoneNumber");

            User user = userService.getByPhoneNumber(phoneNumber);

            if(user == null){
                throw new EntityNotFoundException(userService.getEntityClass().getName(), phoneNumber);
            }

            String accessToken = jwtTokenProvider.createToken(phoneNumber, true);
            String refreshToken = jwtTokenProvider.createToken(phoneNumber, false);

            Map<Object, Object> response = new HashMap<>();

            response.put("accessToken", accessToken);
            response.put("refreshToken", refreshToken);

            return ResponseEntity.ok(response);
        } catch (FirebaseAuthException e) {
            e.printStackTrace();
        }

        return null;
    }
}
