package com.wine.to.up.user.service.domain.dto;

import com.wine.to.up.user.service.domain.entity.NotificationTokenType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class NotificationTokenDto implements AbstractDto<String> {
    private Long userId;
    private String token;
    private NotificationTokenType tokenType;
}
