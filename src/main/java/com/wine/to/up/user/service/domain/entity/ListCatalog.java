package com.wine.to.up.user.service.domain.entity;


import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
@Table(name = "list_catalog")
public class ListCatalog implements AbstractEntity<Long> {

    @EmbeddedId
    private ListCatalogId id;

    @JoinColumn(name = "catalog_id")
    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    private Catalog catalog;

    @JoinColumn(name = "item_id")
    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    private Item item;
}
