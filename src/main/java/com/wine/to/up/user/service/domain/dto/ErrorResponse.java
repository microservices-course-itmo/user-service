package com.wine.to.up.user.service.domain.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.Instant;

@Data
@Accessors(chain = true)
public class ErrorResponse {
    private String message;
    private int status;
    private String timestamp;

    public static ErrorResponse of(final int status, final String message) {
        final ErrorResponse response = new ErrorResponse();

        response.setStatus(status);
        response.setMessage(message);
        response.setTimestamp(String.valueOf(Instant.now().getEpochSecond()));

        return response;
    }
}
