package com.wine.to.up.user.service.domain.entity;

import java.time.Instant;
import java.time.LocalDate;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(exclude = {"city", "role", "company"})
@ToString(exclude = {"city", "role", "company"})
@Accessors(chain = true)
@Table(name = "users")
public class User implements AbstractEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate birthDate;

    @Enumerated(EnumType.ORDINAL)
    private Sex sex;

    private String email;

    private String phoneNumber;

    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "city_id")
    private City city;

    @Column(name = "activated")
    private Boolean isActivated;

    @Column(name = "created_at")
    private Instant createDate;

    @JoinColumn(name = "company_id")
    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    private Company company;

    //TODO: optional
    @JoinColumn(name = "role_id")
    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    private Role role;

    private String password;

    public enum Sex {
        MALE,
        FEMALE
    }
}
