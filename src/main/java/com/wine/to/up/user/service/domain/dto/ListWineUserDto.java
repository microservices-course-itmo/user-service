package com.wine.to.up.user.service.domain.dto;

import com.wine.to.up.user.service.domain.entity.UserTokens;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.ArrayList;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class ListWineUserDto implements AbstractDto<Long> {
    private Long wineId;
    private ArrayList<UserTokens> users;
}
