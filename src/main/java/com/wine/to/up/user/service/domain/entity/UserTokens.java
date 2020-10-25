package com.wine.to.up.user.service.domain.entity;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class UserTokens {
    private Long userId;
    private List<String> iosTokens;
    private List<String> fcmTokens;
}
