package com.wine.to.up.user.service.domain.dto;

import com.wine.to.up.user.service.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class TokenDto implements AbstractDto<Long>{
    private Long id;
    private User user;
    private String accessToken;
    private String refreshToken;
    private String tokenRefreshDate;
}
