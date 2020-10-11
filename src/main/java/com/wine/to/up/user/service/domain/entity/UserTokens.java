package com.wine.to.up.user.service.domain.entity;

import lombok.Data;

import java.util.ArrayList;

@Data
public class UserTokens {
    private Long userId;
    private ArrayList<String> iosTokens;
    private ArrayList<String> fcmTokens;
}
