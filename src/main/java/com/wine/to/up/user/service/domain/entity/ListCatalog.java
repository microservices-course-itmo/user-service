package com.wine.to.up.user.service.domain.entity;


import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
@Table(name = "list_catalog")
@IdClass(ListCatalog.class)
public class ListCatalog implements AbstractEntity<Long>, Serializable {
    @Id
    @JoinColumn(name = "catalog_id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    private Catalog catalog;

    @Id
    @JoinColumn(name = "item_id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    private Item item;
}
