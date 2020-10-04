package com.wine.to.up.user.service.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class UserDto implements AbstractDto<Long> {
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "UTC")
    private Instant createDate;

    private String sex;
    private String email;
    private String phoneNumber;
    private CityDto city;
    private Boolean isActivated;
    private CompanyDto company;
    private RoleDto role;
    private String password;
}
