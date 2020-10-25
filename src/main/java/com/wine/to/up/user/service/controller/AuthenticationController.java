package com.wine.to.up.user.service.controller;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.wine.to.up.commonlib.annotations.InjectEventLogger;
import com.wine.to.up.commonlib.logging.EventLogger;
import com.wine.to.up.user.service.api.dto.AuthenticationResponse;
import com.wine.to.up.user.service.api.dto.UserResponse;
import com.wine.to.up.user.service.domain.dto.AuthenticationRequestDto;
import com.wine.to.up.user.service.domain.dto.UserDto;
import com.wine.to.up.user.service.domain.dto.UserRegistrationDto;
import com.wine.to.up.user.service.logging.UserServiceNotableEvents;
import com.wine.to.up.user.service.security.JwtTokenProvider;
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

@RestController
@Slf4j
@RequiredArgsConstructor
public class AuthenticationController {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final ModelMapper modelMapper;

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

        authenticationResponse.setAccessToken(jwtTokenProvider.createToken(user, true));
        authenticationResponse.setRefreshToken(jwtTokenProvider.createToken(user, false));
        authenticationResponse.setUser(modelMapper.map(user, UserResponse.class));

        return ResponseEntity.ok(authenticationResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthenticationResponse> refresh(@RequestParam String refreshToken) {
        String tokenType = jwtTokenProvider.getTokenType(refreshToken);

        if (!jwtTokenProvider.validateToken(refreshToken) && tokenType.equals("REFRESH_TOKEN")) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        if (!tokenType.equals("REFRESH_TOKEN")) {
            return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
        }

        String phoneNumber = jwtTokenProvider.getPhoneNumber(refreshToken);
        UserDto user = userService.getByPhoneNumber(phoneNumber);

        AuthenticationResponse authenticationResponse =
            new AuthenticationResponse()
                .setAccessToken(jwtTokenProvider.createToken(user, true))
                .setRefreshToken(jwtTokenProvider.createToken(user, false))
                .setUser(modelMapper.map(user, UserResponse.class));

        return ResponseEntity.ok(authenticationResponse);
    }

    @PostMapping("/validate")
    public ResponseEntity<Void> validate(@RequestParam String token) {
        if (jwtTokenProvider.validateToken(token)) {
            return ResponseEntity.ok().build();
        }

        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}
