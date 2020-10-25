package com.wine.to.up.user.service.domain.dto;

import com.wine.to.up.user.service.domain.entity.Catalog;
import com.wine.to.up.user.service.domain.entity.Item;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class CatalogItemsDto implements AbstractDto<Long> {
    private Catalog catalog;
    private Item item;
}
