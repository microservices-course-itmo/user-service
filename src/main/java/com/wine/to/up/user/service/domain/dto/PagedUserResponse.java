package com.wine.to.up.user.service.domain.dto;

import com.wine.to.up.user.service.api.dto.UserResponse;
import lombok.Data;

import java.util.List;

@Data
public class PagedUserResponse {
    private List<UserResponse> content;
    private Integer page;
    private Integer size;
    private Long total;
    private Integer totalPages;
}
