package com.wine.to.up.user.service.controller;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.wine.to.up.user.service.domain.dto.AuthenticationRequestDto;
import com.wine.to.up.user.service.domain.dto.UserDto;
import com.wine.to.up.user.service.domain.dto.UserRegistrationDto;
import com.wine.to.up.user.service.domain.entity.User;
import com.wine.to.up.user.service.domain.response.AuthenticationResponse;
import com.wine.to.up.user.service.exception.EntityNotFoundException;
import com.wine.to.up.user.service.security.JwtTokenProvider;
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
        try {
            String idToken = requestDto.getFireBaseToken();
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
            String phoneNumber = (String) decodedToken.getClaims().get("phone_number");

            User user = userService.getByPhoneNumber(phoneNumber);

            if(user == null){
                UserRegistrationDto userRegistrationDto = new UserRegistrationDto();

                userRegistrationDto.setPhoneNumber(phoneNumber);
                userService.signUp(userRegistrationDto);

                user = userService.getByPhoneNumber(phoneNumber);
            }

            AuthenticationResponse authenticationResponse = new AuthenticationResponse();

            authenticationResponse.setAccessToken(jwtTokenProvider.createToken(phoneNumber, true));
            authenticationResponse.setRefreshToken(jwtTokenProvider.createToken(phoneNumber, false));

            authenticationResponse.setUser(userService.getUserResponse(user));

            return ResponseEntity.ok(authenticationResponse);
        } catch (FirebaseAuthException e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok().build();
    }

    @PostMapping("/registration")
    public ResponseEntity<UserDto> registration(@RequestBody UserRegistrationDto userRegistrationDto){
        UserDto userDto = userService.signUp(userRegistrationDto);

        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/validate")
    public ResponseEntity<Void> validate(@RequestParam String token) {
        try {
            jwtTokenProvider.validateToken(token);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
