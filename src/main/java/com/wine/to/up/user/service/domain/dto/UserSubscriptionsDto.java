package com.wine.to.up.user.service.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class UserSubscriptionsDto implements AbstractDto<Long> {
    private UserDto user;
    private ItemDto item;
}
