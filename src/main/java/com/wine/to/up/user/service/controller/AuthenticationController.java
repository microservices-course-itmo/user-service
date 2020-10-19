package com.wine.to.up.user.service.controller;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.wine.to.up.user.service.domain.dto.AuthenticationRequestDto;
import com.wine.to.up.user.service.domain.dto.UserDto;
import com.wine.to.up.user.service.domain.dto.UserRegistrationDto;
import com.wine.to.up.user.service.domain.response.AuthenticationResponse;
import com.wine.to.up.user.service.security.JwtTokenProvider;
import com.wine.to.up.user.service.domain.response.mapper.UserDtoToUserResponseMapper;
import com.wine.to.up.user.service.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequestDto requestDto){
        String phoneNumber = null;

        try {
            String idToken = requestDto.getFireBaseToken();
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
            phoneNumber = (String) decodedToken.getClaims().get("phone_number");
        } catch (FirebaseAuthException e) {
            e.printStackTrace();
        }

        UserDto user = userService.getByPhoneNumber(phoneNumber);
        if(user == null){
            UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
            userRegistrationDto.setPhoneNumber(phoneNumber);
            user = userService.signUp(userRegistrationDto);
        }

        AuthenticationResponse authenticationResponse = new AuthenticationResponse();

        authenticationResponse.setAccessToken(jwtTokenProvider.createToken(user, true));
        authenticationResponse.setRefreshToken(jwtTokenProvider.createToken(user, false));
        authenticationResponse.setUser(UserDtoToUserResponseMapper.getUserResponse(user));

        return ResponseEntity.ok(authenticationResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthenticationResponse> refresh(@RequestParam String refreshToken){
        String tokenType = jwtTokenProvider.getTokenType(refreshToken);

        if (!jwtTokenProvider.validateToken(refreshToken) && tokenType.equals("REFRESH_TOKEN")) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        if (!tokenType.equals("REFRESH_TOKEN")) {
            return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
        }

        String phoneNumber = jwtTokenProvider.getPhoneNumber(refreshToken);

        UserDto user = userService.getByPhoneNumber(phoneNumber);

        AuthenticationResponse authenticationResponse = new AuthenticationResponse();

        authenticationResponse.setAccessToken(jwtTokenProvider.createToken(user, true));
        authenticationResponse.setRefreshToken(jwtTokenProvider.createToken(user, false));
        authenticationResponse.setUser(UserDtoToUserResponseMapper.getUserResponse(user));

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
