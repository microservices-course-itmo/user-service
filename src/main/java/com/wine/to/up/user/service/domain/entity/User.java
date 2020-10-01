package com.wine.to.up.user.service.domain.entity;

import java.time.Instant;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
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
@Table(name = "dim_user")
public class User implements AbstractEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UserId")
    private Long id;

    @Column(name = "FirstName")
    private String firstName;

    @Column(name = "LastName")
    private String lastName;

    @Column(name = "BirthDate")
    private LocalDate birthDate;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "Sex")
    private Sex sex;

    @Column(name = "Email")
    private String email;

    @Column(name = "PhoneNumber")
    private String phoneNumber;

    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "CityId")
    private City city;

    @Column(name = "isActivated")
    private Boolean isActivated;

    @Column(name = "CreateDate")
    private Instant createDate;

    @JoinColumn(name = "CompanyId")
    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    private Company company;

    //TODO: optional
    @JoinColumn(name = "RoleId")
    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    private Role role;

    @Column(name = "Password")
    private String password;

    public enum Sex {
        MALE,
        FEMALE
    }
}
