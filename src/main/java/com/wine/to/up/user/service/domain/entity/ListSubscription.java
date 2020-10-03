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
@Table(name = "list_subscription")
public class ListSubscription implements AbstractEntity<Long> {

    @EmbeddedId
    private ListSubscriptionId id;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    private User user;

    @JoinColumn(name = "item_id")
    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    private Item item;
}
