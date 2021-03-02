package com.wine.to.up.user.service.exception;

import com.google.firebase.auth.FirebaseAuthException;
import com.wine.to.up.user.service.domain.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFound(EntityNotFoundException exception) {
        return composeResponse(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleEntityAlreadyExists(EntityAlreadyExistsException exception) {
        return composeResponse(exception.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(FirebaseAuthException.class)
    public ResponseEntity<ErrorResponse> handleFirebaseAuthException(FirebaseAuthException exception) {
        return composeResponse(exception.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        return composeResponse(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ErrorResponse> composeResponse(String message, HttpStatus statusCode) {
        return new ResponseEntity<>(
            new ErrorResponse()
                .setStatus(statusCode.value())
                .setTimestamp(String.valueOf(Instant.now().getEpochSecond()))
                .setMessage(message),
            statusCode
        );
    }
}
