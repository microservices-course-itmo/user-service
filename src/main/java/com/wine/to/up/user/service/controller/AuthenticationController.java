package com.wine.to.up.user.service.controller;

import com.wine.to.up.commonlib.annotations.InjectEventLogger;
import com.wine.to.up.commonlib.logging.EventLogger;
import com.wine.to.up.user.service.api.dto.AuthenticationResponse;
import com.wine.to.up.user.service.api.dto.UserResponse;
import com.wine.to.up.user.service.domain.dto.*;
import com.wine.to.up.user.service.domain.entity.City;
import com.wine.to.up.user.service.security.JwtTokenProvider;
import com.wine.to.up.user.service.service.CityService;
import com.wine.to.up.user.service.service.UserService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
public class AuthenticationController {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final CityService cityService;

    @Value("${default.jwt.token.stub}")
    private String TOKEN_STUB;

    @InjectEventLogger
    private EventLogger eventLogger;

    @ApiOperation(value = "User registration",
            notes = "Description: Creates new user and returns authenticationResponse with token pair. " +
                "City id can be any number, ignored at the moment.",
            response = AuthenticationResponse.class,
            responseContainer = "ResponseEntity")
    @ApiResponses(
            @ApiResponse(code = 418, message = "Invalid token, provided firebase token cannot be verified"))
    @PostMapping(path = "/registration", produces = "application/json")
    public ResponseEntity<AuthenticationResponse> registration(@RequestBody RegistrationRequestDto requestDto) {
        String idToken = requestDto.getFireBaseToken();
        String phoneNumber = jwtTokenProvider.getPhoneNumberFromFirebaseToken(idToken);

        Long cityId = requestDto.getCityId();
        CityDto city = cityService.getById(cityId);

        if (phoneNumber == null ||
                city == null) {
            return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
        }

        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setPhoneNumber(phoneNumber);
        userRegistrationDto.setName(requestDto.getName());
        userRegistrationDto.setBirthDate(requestDto.getBirthday());
        userRegistrationDto.setCityId(cityId);

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
            @ApiResponse(code = 401, message = "Unauthorized, no user found by this token"),
            @ApiResponse(code = 418, message = "Invalid token, provided firebase token cannot be verified")})
    @PostMapping(path = "/login", produces = "application/json")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequestDto requestDto) {
        log.info("Got auth request: " + requestDto);

        String idToken = requestDto.getFireBaseToken();
        String phoneNumber = jwtTokenProvider.getPhoneNumberFromFirebaseToken(idToken);

        log.info("phone number from token: " + phoneNumber);

        if (phoneNumber == null) {
            return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
        }

        if (!userService.existsByPhoneNumber(phoneNumber)) {
            log.info("user does not exist, 401");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        UserDto user = userService.getByPhoneNumber(phoneNumber);
        log.info("found user: " + user);

        AuthenticationResponse authenticationResponse = new AuthenticationResponse();

        authenticationResponse.setAccessToken(jwtTokenProvider.createToken(user, true));
        authenticationResponse.setRefreshToken(jwtTokenProvider.createToken(user, false));
        authenticationResponse.setUser(modelMapper.map(user, UserResponse.class));
        log.info("response: " + authenticationResponse);
        return ResponseEntity.ok(authenticationResponse);
    }

    @ApiOperation(value = "refresh",
            notes = "Description: Returns new tokens pair",
            response = AuthenticationResponse.class,
            responseContainer = "ResponseEntity")
    @ApiResponses({
            @ApiResponse(code = 401, message = "Unauthorized, token can not be validated"),
            @ApiResponse(code = 418, message = "Invalid token type, wrong type's token was provided"),
            @ApiResponse(code = 409, message = "Used is already exists")
    })
    @PostMapping(path = "/refresh", produces = "application/json")
    public ResponseEntity<AuthenticationResponse> refresh(
            @ApiParam(name = "refreshToken", value = "Token for refresh", required = true)
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
            @ApiResponse(code = 401, message = "Unauthorized, token was not validated")
    )
    @PostMapping(path = "/validate", produces = "application/json")
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
