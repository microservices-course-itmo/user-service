package com.wine.to.up.user.service.domain.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ListFavoriteId implements Serializable {

    @Column(name = "item_id")
    private Long itemId;

    @Column(name = "user_id")
    private Long userId;

    public ListFavoriteId() {

    }

    public ListFavoriteId(Long itemId, Long userId) {
        this.itemId = itemId;
        this.userId = userId;
    }

    public Long getItemId() {
        return itemId;
    }

    public Long getUserId() {
        return userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ListFavoriteId)) return false;
        ListFavoriteId that = (ListFavoriteId) o;
        return Objects.equals(getItemId(), that.getItemId()) &&
                Objects.equals(getUserId(), that.getUserId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getItemId(), getUserId());
    }
}
