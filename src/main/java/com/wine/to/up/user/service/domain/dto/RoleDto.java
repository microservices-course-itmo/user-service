package com.wine.to.up.user.service.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class RoleDto implements AbstractDto<Long>{
    private Long id;
    private String name;
    private Boolean accessToCatalog;
    private Boolean canComment;
    private Boolean canCreateCatalog;
    private Boolean canAddItem;
    private Boolean canLike;
}
