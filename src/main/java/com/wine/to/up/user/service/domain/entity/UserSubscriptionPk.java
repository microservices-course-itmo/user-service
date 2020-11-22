package com.wine.to.up.user.service.domain.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
public class UserSubscriptionPk implements Serializable {
    protected Long userId;
    protected String itemId;
}
