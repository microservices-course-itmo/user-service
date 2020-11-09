package com.wine.to.up.user.service.domain.dto;

import lombok.Data;

@Data
public class RegistrationRequestDto {
    private String name;
    private String birthday;
    private String city;
    private String fireBaseToken;
}
