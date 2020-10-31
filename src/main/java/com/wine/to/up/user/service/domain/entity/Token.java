package com.wine.to.up.user.service.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
@Table(name = "tokens")
public class Token implements AbstractEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User user;

    @Column(name = "token_access")
    private String accessToken;

    @Column(name = "token_refresh")
    private String refreshToken;

    @Column(name = "token_refresh_date")
    private String tokenRefreshDate;
}
