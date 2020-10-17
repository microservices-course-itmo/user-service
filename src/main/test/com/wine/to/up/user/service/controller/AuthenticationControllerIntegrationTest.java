package com.wine.to.up.user.service.controller;

import static org.junit.Assert.assertEquals;

import com.wine.to.up.user.service.security.JwtTokenProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
@TestPropertySource("classpath:test.properties")
public class AuthenticationControllerIntegrationTest {
    @Autowired
    private AuthenticationController authenticationController;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Test
    public void testValidateCorrect() {
        String token = jwtTokenProvider.createToken("+71234567890", true);

        ResponseEntity<Void> response = this.authenticationController.validate(token);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testValidateIncorrect() {
        ResponseEntity<Void> response = this.authenticationController.validate("invalid_token");
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
}
