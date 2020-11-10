package com.wine.to.up.user.service.controller;

import com.wine.to.up.user.service.domain.dto.UserDto;
import com.wine.to.up.user.service.service.ItemService;
import com.wine.to.up.user.service.service.SubscriptionService;
import com.wine.to.up.user.service.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/subscriptions")
@Slf4j
public class SubscriptionController {
    public final ItemService itemService;
    public final UserService userService;
    public final SubscriptionService subscriptionService;
    public final ModelMapper modelMapper;

    @GetMapping(path = "/{itemId}", produces = "application/json")
    public ResponseEntity<List<UserDto>> findUsersByItemID(
            @PathVariable String itemId) {
        List<UserDto> subscriptionList = subscriptionService.getUsersByItemId(itemId);
        return new ResponseEntity<>(subscriptionList, HttpStatus.OK);
    }
}
