package com.wine.to.up.user.service.domain.response;

import com.wine.to.up.user.service.domain.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class UserResponse {
    private Long id;
    private String phoneNumber;
    private String role;
}