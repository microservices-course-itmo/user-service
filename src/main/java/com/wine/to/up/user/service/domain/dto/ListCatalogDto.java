package com.wine.to.up.user.service.domain.dto;

import com.wine.to.up.user.service.domain.entity.Catalog;
import com.wine.to.up.user.service.domain.entity.Item;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class ListCatalogDto implements AbstractDto<Long>{

    private Catalog catalog;
    private Item item;

}
