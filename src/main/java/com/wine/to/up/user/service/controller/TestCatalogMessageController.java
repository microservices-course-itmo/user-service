package com.wine.to.up.user.service.controller;

import com.wine.to.up.catalog.service.api.message.UpdatePriceEventOuterClass;
import com.wine.to.up.commonlib.messaging.KafkaMessageSender;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class TestCatalogMessageController {
    private final KafkaMessageSender<UpdatePriceEventOuterClass.UpdatePriceEvent> messageSender;

    @GetMapping("/catalog/send")
    public void sendMessage() {
        messageSender.sendMessage(
            UpdatePriceEventOuterClass.UpdatePriceEvent.newBuilder()
                .setPrice(42)
                .setName("test")
                .setId(UUID.randomUUID().toString())
                .build()
        );
    }
}
