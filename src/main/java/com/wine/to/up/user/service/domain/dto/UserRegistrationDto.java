package com.wine.to.up.user.service.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.Instant;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class UserRegistrationDto implements AbstractDto<Long> {
    private LocalDate birthDate;
    private String sex;
    private String email;
    private String phoneNumber;
    private Long cityId;
    private Instant createDate;
    private Long companyId;
    private String password;
}
