package com.wine.to.up.user.service.controller;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.wine.to.up.commonlib.annotations.InjectEventLogger;
import com.wine.to.up.commonlib.logging.EventLogger;
import com.wine.to.up.user.service.api.dto.AuthenticationResponse;
import com.wine.to.up.user.service.api.dto.UserResponse;
import com.wine.to.up.user.service.domain.dto.AuthenticationRequestDto;
import com.wine.to.up.user.service.domain.dto.TokenDto;
import com.wine.to.up.user.service.domain.dto.UserDto;
import com.wine.to.up.user.service.domain.dto.UserRegistrationDto;
import com.wine.to.up.user.service.domain.entity.User;
import com.wine.to.up.user.service.logging.UserServiceNotableEvents;
import com.wine.to.up.user.service.security.JwtTokenProvider;
import com.wine.to.up.user.service.service.TokenService;
import com.wine.to.up.user.service.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@Slf4j
@RequiredArgsConstructor
public class AuthenticationController {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final TokenService tokenService;

    @InjectEventLogger
    private EventLogger eventLogger;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequestDto requestDto) {
        String phoneNumber;

        try {
            String idToken = requestDto.getFireBaseToken();
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
            phoneNumber = (String) decodedToken.getClaims().get("phone_number");
        } catch (FirebaseAuthException e) {
            eventLogger.debug(UserServiceNotableEvents.W_AUTH_FAILURE, "Cannot verify firebase token");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        UserDto user;
        if (!userService.existsByPhoneNumber(phoneNumber)) {
            UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
            userRegistrationDto.setPhoneNumber(phoneNumber);
            user = userService.signUp(userRegistrationDto);
        } else {
            user = userService.getByPhoneNumber(phoneNumber);
        }

        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        TokenDto tokenDto = new TokenDto();
        Date date = new Date();

        String accessToken = jwtTokenProvider.createToken(user, true);
        String refreshToken = jwtTokenProvider.createToken(user, false);

        tokenDto.setAccessToken(accessToken);
        tokenDto.setRefreshToken(refreshToken);
        tokenDto.setUser(modelMapper.map(user, User.class));
        tokenDto.setTokenRefreshDate(String.valueOf(date.getTime()));
        tokenService.create(tokenDto);

        authenticationResponse.setAccessToken(accessToken);
        authenticationResponse.setRefreshToken(refreshToken);
        authenticationResponse.setUser(modelMapper.map(user, UserResponse.class));

        return ResponseEntity.ok(authenticationResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthenticationResponse> refresh(@RequestParam String refreshToken) {
        String tokenType;
        String phoneNumber;
        UserDto user;

        try {
            tokenType = jwtTokenProvider.getTokenType(refreshToken);
            phoneNumber = jwtTokenProvider.getPhoneNumber(refreshToken);
            user = userService.getByPhoneNumber(phoneNumber);
        } catch (Exception ex) {
            eventLogger.debug(UserServiceNotableEvents.W_AUTH_FAILURE, ex.getMessage());
            return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
        }

        if (!tokenType.equals("REFRESH_TOKEN") || !jwtTokenProvider.validateToken(refreshToken, user, tokenType)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        TokenDto tokenDto = tokenService.getTokenDto(refreshToken);
        Date date = new Date();

        String accessToken = jwtTokenProvider.createToken(user, true);
        String newRefreshToken = jwtTokenProvider.createToken(user, false);

        tokenDto.setAccessToken(accessToken);
        tokenDto.setRefreshToken(refreshToken);
        tokenDto.setUser(modelMapper.map(user, User.class));
        tokenDto.setTokenRefreshDate(String.valueOf(date.getTime()));
        tokenService.update(tokenDto);

        AuthenticationResponse authenticationResponse =
            new AuthenticationResponse()
                .setAccessToken(accessToken)
                .setRefreshToken(newRefreshToken)
                .setUser(modelMapper.map(user, UserResponse.class));

        return ResponseEntity.ok(authenticationResponse);
    }

    @PostMapping("/validate")
    public ResponseEntity<Void> validate(@RequestParam String token) {
        String tokenType;
        String phoneNumber;
        UserDto user;

        try {
            tokenType = jwtTokenProvider.getTokenType(token);
            phoneNumber = jwtTokenProvider.getPhoneNumber(token);
            user = userService.getByPhoneNumber(phoneNumber);
        } catch (Exception ex) {
            eventLogger.debug(UserServiceNotableEvents.W_AUTH_FAILURE, ex.getMessage());
            return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
        }

        if (jwtTokenProvider.validateToken(token, user, tokenType)) {
            return ResponseEntity.ok().build();
        }

        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}
