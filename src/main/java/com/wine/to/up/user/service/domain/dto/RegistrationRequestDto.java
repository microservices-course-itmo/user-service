package com.wine.to.up.user.service.domain.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class RegistrationRequestDto {
    private String name;
    private LocalDate birthday;
    private Long cityId;
    private String fireBaseToken;
}
