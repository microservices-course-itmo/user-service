package com.wine.to.up.user.service.controller;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.wine.to.up.commonlib.annotations.InjectEventLogger;
import com.wine.to.up.commonlib.logging.EventLogger;
import com.wine.to.up.user.service.api.dto.AuthenticationResponse;
import com.wine.to.up.user.service.api.dto.UserResponse;
import com.wine.to.up.user.service.domain.dto.AuthenticationRequestDto;
import com.wine.to.up.user.service.domain.dto.RegistrationRequestDto;
import com.wine.to.up.user.service.domain.dto.UserDto;
import com.wine.to.up.user.service.domain.dto.UserRegistrationDto;
import com.wine.to.up.user.service.logging.UserServiceNotableEvents;
import com.wine.to.up.user.service.security.JwtTokenProvider;
import com.wine.to.up.user.service.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${default.jwt.token.stub}")
    private String TOKEN_STUB;

    @InjectEventLogger
    private EventLogger eventLogger;

    @ApiOperation(value = "User registration",
            notes = "Description: Creates new user and returns authenticationResponse with token pair",
            response = AuthenticationResponse.class,
            responseContainer = "ResponseEntity")
    @ApiResponses(
            @ApiResponse(code = 418, message = "Cannot verify token"))
    @PostMapping("/registration")
    public ResponseEntity<AuthenticationResponse> registration(@RequestBody RegistrationRequestDto requestDto) {
        String phoneNumber;

        try {
            String idToken = requestDto.getFireBaseToken();
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
            phoneNumber = (String) decodedToken.getClaims().get("phone_number");
        } catch (FirebaseAuthException e) {
            eventLogger.debug(UserServiceNotableEvents.W_AUTH_FAILURE, "Cannot verify firebase token");
            return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
        }

        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setPhoneNumber(phoneNumber);
        UserDto user = userService.signUp(userRegistrationDto);

        AuthenticationResponse authenticationResponse = new AuthenticationResponse();

        authenticationResponse.setAccessToken(jwtTokenProvider.createToken(user, true));
        authenticationResponse.setRefreshToken(jwtTokenProvider.createToken(user, false));
        authenticationResponse.setUser(modelMapper.map(user, UserResponse.class));

        return ResponseEntity.ok(authenticationResponse);
    }

    @ApiOperation(value = "Login user",
            notes = "Description: Authenticates and returns authenticationResponse with tokens pair",
            response = AuthenticationResponse.class,
            responseContainer = "ResponseEntity")
    @ApiResponses({
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 418, message = "Cannot verify token")})
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequestDto requestDto) {
        String phoneNumber;

        try {
            String idToken = requestDto.getFireBaseToken();
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
            phoneNumber = (String) decodedToken.getClaims().get("phone_number");
        } catch (FirebaseAuthException e) {
            eventLogger.debug(UserServiceNotableEvents.W_AUTH_FAILURE, "Cannot verify firebase token");
            return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
        }

        if (!userService.existsByPhoneNumber(phoneNumber)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        UserDto user = userService.getByPhoneNumber(phoneNumber);

        AuthenticationResponse authenticationResponse = new AuthenticationResponse();

        authenticationResponse.setAccessToken(jwtTokenProvider.createToken(user, true));
        authenticationResponse.setRefreshToken(jwtTokenProvider.createToken(user, false));
        authenticationResponse.setUser(modelMapper.map(user, UserResponse.class));

        return ResponseEntity.ok(authenticationResponse);
    }

    @ApiOperation(value = "refresh",
            notes = "Description: Returns new tokens pair",
            response = AuthenticationResponse.class,
            responseContainer = "ResponseEntity")
    @ApiResponses({
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 418, message = "Cannot verify token")})
    @PostMapping(path = "/refresh")
    public ResponseEntity<AuthenticationResponse> refresh(
            @ApiParam(name = "refreshToken", value = "Token for refreshing", required = true)
            @RequestParam String refreshToken) {
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

    @ApiOperation(value = "validate",
            notes = "Description: Validates token and returns OK status",
            responseContainer = "ResponseEntity")
    @ApiResponses(
            @ApiResponse(code = 401, message = "Unauthorized"))
    @PostMapping("/validate")
    public ResponseEntity<Void> validate(
            @ApiParam(name = "token", value = "Token for validation", required = true)
            @RequestParam String token) {
        if (token.equals(TOKEN_STUB) ||
                jwtTokenProvider.validateToken(token)) {
            return ResponseEntity.ok().build();
        }

        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}
