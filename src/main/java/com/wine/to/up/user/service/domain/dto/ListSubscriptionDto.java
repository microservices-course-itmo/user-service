package com.wine.to.up.user.service.domain.dto;

import com.wine.to.up.user.service.domain.entity.Item;
import com.wine.to.up.user.service.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class ListSubscriptionDto implements AbstractDto<Long>{

    private User user;
    private Item item;

}
