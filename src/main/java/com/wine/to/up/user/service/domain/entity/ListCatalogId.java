package com.wine.to.up.user.service.domain.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ListCatalogId implements Serializable {

    @Column(name = "item_id")
    private Long item_id;

    @Column(name = "catalog_id")
    private Long catalog_id;

    public ListCatalogId() {

    }

    public ListCatalogId(Long item_id, Long catalog_id) {
        this.item_id = item_id;
        this.catalog_id = catalog_id;
    }

    public Long getItem_id() {
        return item_id;
    }

    public Long getCatalog_id() {
        return catalog_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ListCatalogId)) return false;
        ListCatalogId that = (ListCatalogId) o;
        return Objects.equals(getItem_id(), that.getItem_id()) &&
                Objects.equals(getCatalog_id(), that.getCatalog_id());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getItem_id(), getCatalog_id());
    }
}
