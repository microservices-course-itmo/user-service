package com.wine.to.up.user.service.controller;

import com.wine.to.up.user.service.api.dto.UserResponse;
import com.wine.to.up.user.service.controller.util.ApiPageParams;
import com.wine.to.up.user.service.domain.dto.PagedUserResponse;
import com.wine.to.up.user.service.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/internal/users")
@RequiredArgsConstructor
public class UserInternalController {
    private final UserService userService;

    @GetMapping
    @ApiOperation(value = "Get all users batch")
    @ApiPageParams
    public PagedUserResponse batchGet(@RequestParam Integer size, @RequestParam Integer page) {
        final PageRequest pageRequest = PageRequest.of(page, size);

        final Page<UserResponse> userResponsePage = userService.findAll(pageRequest);
        final PagedUserResponse response = new PagedUserResponse();
        response.setContent(userResponsePage.getContent());
        response.setSize(userResponsePage.getSize());
        response.setPage(userResponsePage.getPageable().getPageNumber());
        response.setTotal(userResponsePage.getTotalElements());
        response.setTotalPages(userResponsePage.getTotalPages());

        return response;
    }
}
