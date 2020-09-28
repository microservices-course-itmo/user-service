package com.wine.to.up.user.service.domain.entity;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
@Table(name = "dim_role")
public class Role implements AbstractEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RoleId")
    private Long id;

    @Column(name = "RoleName")
    private String name;

    @Column(name = "AccessToCatalog")
    private Boolean accessToCatalog;

    @Column(name = "CanComment")
    private Boolean canComment;

    @Column(name = "CanCreateCatalog")
    private Boolean canCreateCatalog;

    @Column(name = "CanAddItem")
    private Boolean canAddItem;

    @Column(name = "CanLike")
    private Boolean canLike;
}
