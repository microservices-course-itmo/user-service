package com.wine.to.up.user.service.domain.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ErrorResponse {
    private String message;
    private int status;
    private String timestamp;
}
