package com.wine.to.up.user.service.domain.dto;

import com.wine.to.up.user.service.domain.entity.City;
import com.wine.to.up.user.service.domain.entity.Company;
import java.time.Instant;

import com.wine.to.up.user.service.domain.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class UserDto implements AbstractDto<Long> {
    private Long id;
    private String firstName;
    private String lastName;
    private Instant birthDate;
    private String sex;
    private String email;
    private String phoneNumber;
    private City city;
    private Boolean isActivated;
    private Instant createDate;
    private Company company;
    private Role role;
}
