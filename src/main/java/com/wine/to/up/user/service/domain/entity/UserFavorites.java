package com.wine.to.up.user.service.domain.entity;


import java.io.Serializable;
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
@Table(name = "list_subscriptions")
@IdClass(UserFavoritesPk.class)
public class UserFavorites implements AbstractEntity<String>, Serializable {
    @Id
    @Column(name = "user_id")
    protected Long userId;

    @Id
    @Column(name = "item_id")
    protected String itemId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    protected User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "item_id", referencedColumnName = "id", insertable = false, updatable = false)
    protected Item item;

    public UserFavorites(User user, Item item) {
        this.user = user;
        this.userId = user.getId();
        this.item = item;
        this.itemId = item.getId();
    }
}
