package com.wine.to.up.user.service.controller;

import com.wine.to.up.catalog.service.api.message.UpdatePriceMessageSentEventOuterClass;
import com.wine.to.up.commonlib.messaging.KafkaMessageSender;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
                        .setId("ceff04b2-106c-48e7-a264-75df434c6d96")
                        .build()
        );
    }
}
