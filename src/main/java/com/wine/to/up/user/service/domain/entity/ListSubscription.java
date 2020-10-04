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
@Table(name = "list_subscription")
@IdClass(ListSubscription.class)
public class ListSubscription implements AbstractEntity<Long>, Serializable {
    @Id
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    private User user;

    @Id
    @JoinColumn(name = "item_id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    private Item item;
}
