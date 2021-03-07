package com.wine.to.up.user.service.controller;

import com.wine.to.up.catalog.service.api.message.UpdatePriceMessageSentEventOuterClass;
import com.wine.to.up.commonlib.messaging.KafkaMessageSender;
import com.wine.to.up.user.service.api.message.NewUserCreatedEventOuterClass;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Api(value = "Kafka controller", description = "Endpoints for sending test Kafka messages")
public class TestKafkaController {
    private final KafkaMessageSender<UpdatePriceMessageSentEventOuterClass.UpdatePriceMessageSentEvent> updatePriceMessageSender;
    private final KafkaMessageSender<NewUserCreatedEventOuterClass.NewUserCreatedEvent> newUserMessageSender;

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

    @ApiOperation(value = "Send NewUserCreatedEvent",
            notes = "Send message NewUserCreatedEvent to Kafka to user-service-new-user-created topic")
    @GetMapping("/user/newUser")
    public void sendNewUserMessage() {
        newUserMessageSender.sendMessage(
                NewUserCreatedEventOuterClass.NewUserCreatedEvent.newBuilder()
                .setUserId(1)
                .setPhoneNumber("+79111234567")
                .setName("Vasya")
                .setBirthdate("01.01.1990")
                .setCityId(1)
                .build()
        );
    }
}
