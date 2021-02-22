package com.wine.to.up.user.service.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
@Table(name = "notification_tokens")
@IdClass(NotificationToken.class)
public class NotificationToken implements AbstractEntity<String>, Serializable {
    @Id
    @Column(name = "user_id")
    private Long userId;

    @Id
    @Column(name = "token")
    private String token;

    @Column(name = "token_type")
    @Enumerated(EnumType.STRING)
    private NotificationTokenType tokenType;
}
