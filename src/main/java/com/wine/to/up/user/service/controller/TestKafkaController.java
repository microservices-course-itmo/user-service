package com.wine.to.up.user.service.controller;

import com.wine.to.up.catalog.service.api.message.UpdatePriceMessageSentEventOuterClass;
import com.wine.to.up.commonlib.messaging.KafkaMessageSender;
import com.wine.to.up.user.service.api.message.EntityUpdatedMetaOuterClass.EntityUpdatedMeta;
import com.wine.to.up.user.service.api.message.FavoritesUpdatedEventOuterClass;
import com.wine.to.up.user.service.api.message.UserUpdatedEventOuterClass;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequiredArgsConstructor
@Api(value = "Kafka controller", description = "Endpoints for sending test Kafka messages")
public class TestKafkaController {
    private static final int FAKE_USER_ID = 999999;

    private final KafkaMessageSender<UpdatePriceMessageSentEventOuterClass.UpdatePriceMessageSentEvent> updatePriceMessageSender;
    private final KafkaMessageSender<UserUpdatedEventOuterClass.UserUpdatedEvent> userUpdatedMessageSender;
    private final KafkaMessageSender<FavoritesUpdatedEventOuterClass.FavoritesUpdatedEvent> favoritesUpdatedMessageSender;

    @ApiOperation(value = "Send UpdatePriceMessageSentEvent",
        notes = "Send message UpdatePriceMessageSentEvent to Kafka from catalog-service topic")
    @GetMapping("/catalog/send")
    public void sendPriceUpdatedMessage() {
        updatePriceMessageSender.sendMessage(
            UpdatePriceMessageSentEventOuterClass.UpdatePriceMessageSentEvent.newBuilder()
                .setPrice(42)
                .setName("test")
                .setId("ceff04b2-106c-48e7-a264-75df434c6d96")
                .build()
        );
    }

    @ApiOperation(value = "Send UserUpdatedEvent",
        notes = "Send message that user was created to Kafka to user-service-user-updated topic")
    @PostMapping("/user/userCreated")
    public void sendUserCreatedMessage() {
        userUpdatedMessageSender.sendMessage(
            UserUpdatedEventOuterClass.UserUpdatedEvent.newBuilder()
                .setUserId(FAKE_USER_ID)
                .setPhoneNumber("+79111234567")
                .setName("Vasya")
                .setBirthdate("01.01.1990")
                .setCityId(1)
                .setMeta(EntityUpdatedMeta.newBuilder()
                    .setOperationTime(new Date().getTime())
                    .setOperationType(EntityUpdatedMeta.Operation.CREATE)
                    .build())
                .build()
        );
    }

    @ApiOperation(value = "Send UserUpdatedEvent",
        notes = "Send message that user was updated to Kafka to user-service-user-updated topic")
    @PatchMapping("/user/userUpdated")
    public void sendUserUpdatedMessage() {
        userUpdatedMessageSender.sendMessage(
            UserUpdatedEventOuterClass.UserUpdatedEvent.newBuilder()
                .setUserId(FAKE_USER_ID)
                .setPhoneNumber("+79111234567")
                .setName("Ivan")
                .setBirthdate("01.01.1990")
                .setCityId(2)
                .setMeta(EntityUpdatedMeta.newBuilder()
                    .setOperationTime(new Date().getTime())
                    .setOperationType(EntityUpdatedMeta.Operation.UPDATE)
                    .build())
                .build()
        );
    }

    @ApiOperation(value = "Send UserUpdatedEvent",
        notes = "Send message that user was deleted to Kafka to user-service-user-updated topic")
    @DeleteMapping("/user/userDeleted")
    public void sendUserDeletedMessage() {
        userUpdatedMessageSender.sendMessage(
            UserUpdatedEventOuterClass.UserUpdatedEvent.newBuilder()
                .setUserId(FAKE_USER_ID)
                .setMeta(EntityUpdatedMeta.newBuilder()
                    .setOperationTime(new Date().getTime())
                    .setOperationType(EntityUpdatedMeta.Operation.DELETE)
                    .build())
                .build()
        );
    }

    @ApiOperation(value = "Send FavoritesUpdated",
        notes = "Send message that item was added to favorites to Kafka to user-service-favorites-updated topic")
    @PostMapping("/favorites/favoriteAdded/{itemId}")
    public void sendFavoriteAddedMessage(@PathVariable String itemId) {
        favoritesUpdatedMessageSender.sendMessage(
            FavoritesUpdatedEventOuterClass.FavoritesUpdatedEvent.newBuilder()
                .setUserId(FAKE_USER_ID)
                .setWineId(itemId)
                .setMeta(EntityUpdatedMeta.newBuilder()
                    .setOperationTime(new Date().getTime())
                    .setOperationType(EntityUpdatedMeta.Operation.CREATE)
                    .build())
                .build()
        );
    }

    @ApiOperation(value = "Send FavoritesUpdated",
        notes = "Send message that item was deleted from favorites to Kafka to user-service-favorites-updated topic")
    @DeleteMapping("/favorites/favoriteDeleted/{itemId}")
    public void sendFavoriteDeletedMessage(@PathVariable String itemId) {
        favoritesUpdatedMessageSender.sendMessage(
            FavoritesUpdatedEventOuterClass.FavoritesUpdatedEvent.newBuilder()
                .setUserId(FAKE_USER_ID)
                .setWineId(itemId)
                .setMeta(EntityUpdatedMeta.newBuilder()
                    .setOperationTime(new Date().getTime())
                    .setOperationType(EntityUpdatedMeta.Operation.DELETE)
                    .build())
                .build()
        );
    }

    @ApiOperation(value = "Send FavoritesUpdated",
        notes = "Send message that all items were deleted from favorites to Kafka to user-service-favorites-updated topic")
    @DeleteMapping("/favorites/allFavoritesDeleted")
    public void sendAllFavoritesDeletedMessage() {
        favoritesUpdatedMessageSender.sendMessage(
            FavoritesUpdatedEventOuterClass.FavoritesUpdatedEvent.newBuilder()
                .setUserId(FAKE_USER_ID)
                .setMeta(EntityUpdatedMeta.newBuilder()
                    .setOperationTime(new Date().getTime())
                    .setOperationType(EntityUpdatedMeta.Operation.CLEAR)
                    .build())
                .build()
        );
    }
}
