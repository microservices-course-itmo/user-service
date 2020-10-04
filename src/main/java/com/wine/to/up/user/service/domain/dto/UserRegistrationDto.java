package com.wine.to.up.user.service.domain.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
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
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate birthDate;
    private String sex;
    private String email;
    private String phoneNumber;
    private Long cityId;
    private Long companyId;
    private String password;
}
