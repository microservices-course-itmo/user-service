package com.wine.to.up.user.service.domain.response;

import com.wine.to.up.user.service.domain.entity.UserTokens;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Accessors(chain = true)
public class WineNotificationMessage {
    private Long wineId;
    private String name;
    private double price;
    private List<UserTokens> users;
}