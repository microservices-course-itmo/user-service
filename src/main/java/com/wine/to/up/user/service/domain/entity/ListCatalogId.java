package com.wine.to.up.user.service.domain.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ListCatalogId implements Serializable {

    @Column(name = "item_id")
    private Long itemId;

    @Column(name = "catalog_id")
    private Long catalogId;

    public ListCatalogId() {

    }

    public ListCatalogId(Long itemId, Long catalogId) {
        this.itemId = itemId;
        this.catalogId = catalogId;
    }

    public Long getItemId() {
        return itemId;
    }

    public Long getCatalogId() {
        return catalogId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ListCatalogId)) return false;
        ListCatalogId that = (ListCatalogId) o;
        return Objects.equals(getItemId(), that.getItemId()) &&
                Objects.equals(getCatalogId(), that.getCatalogId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getItemId(), getCatalogId());
    }
}
