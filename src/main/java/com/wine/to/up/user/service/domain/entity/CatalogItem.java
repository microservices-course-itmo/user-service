package com.wine.to.up.user.service.domain.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
@Table(name = "list_catalogs")
@IdClass(CatalogItem.class)
public class CatalogItem implements AbstractEntity<Long>, Serializable {
    @Id
    @JoinColumn(name = "catalog_id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Catalog catalog;

    @Id
    @JoinColumn(name = "item_id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Item item;
}
