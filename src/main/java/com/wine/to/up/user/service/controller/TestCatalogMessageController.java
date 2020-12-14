package com.wine.to.up.user.service.controller;

import com.wine.to.up.catalog.service.api.message.UpdatePriceMessageSentEventOuterClass;
import com.wine.to.up.commonlib.messaging.KafkaMessageSender;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class TestCatalogMessageController {
    private final KafkaMessageSender<UpdatePriceMessageSentEventOuterClass.UpdatePriceMessageSentEvent> messageSender;

    @GetMapping("/catalog/send")
    public void sendMessage() {
        messageSender.sendMessage(
            UpdatePriceMessageSentEventOuterClass.UpdatePriceMessageSentEvent.newBuilder()
                .setPrice(42)
                .setName("test")
                .setId(UUID.randomUUID().toString())
                .build()
        );
    }
}
