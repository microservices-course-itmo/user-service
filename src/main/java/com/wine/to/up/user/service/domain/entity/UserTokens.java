package com.wine.to.up.user.service.domain.entity;

import java.util.List;
import lombok.Data;

@Data
public class UserTokens {
    private Long userId;
    private List<String> iosTokens;
    private List<String> fcmTokens;
}
