package com.wine.to.up.user.service.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateUserInfoRequest {
    private String phoneNumber;
    private String name;
    private Long cityId;
    @JsonFormat(pattern = "dd.MM.yyyy", shape = JsonFormat.Shape.STRING)
    private LocalDate birthday;
}
