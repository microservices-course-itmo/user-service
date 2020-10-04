package com.wine.to.up.user.service.domain.dto;

import com.wine.to.up.user.service.domain.entity.City;
import com.wine.to.up.user.service.domain.entity.Company;
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
    private LocalDate birthDate;
    private String sex;
    private String email;
    private String phoneNumber;
    private CityDto city;
    private Boolean isActivated;
    private Instant createDate;
    private CompanyDto company;
    private RoleDto role;
    private String password;
}
