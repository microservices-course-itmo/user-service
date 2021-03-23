package com.wine.to.up.user.service.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class UserAllFavoritesDto implements AbstractDto<Long> {
    private UserDto userDto;
    private List<ItemDto> itemDtos;
}