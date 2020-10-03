package com.wine.to.up.user.service.domain.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ListFavoriteId implements Serializable {

    @Column(name = "item_id")
    private Long item_id;

    @Column(name = "user_id")
    private Long user_id;

    public ListFavoriteId() {

    }

    public ListFavoriteId(Long item_id, Long user_id) {
        this.item_id = item_id;
        this.user_id = user_id;
    }

    public Long getItem_id() {
        return item_id;
    }

    public Long getUser_id() {
        return user_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ListFavoriteId)) return false;
        ListFavoriteId that = (ListFavoriteId) o;
        return Objects.equals(getItem_id(), that.getItem_id()) &&
                Objects.equals(getUser_id(), that.getUser_id());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getItem_id(), getUser_id());
    }
}
