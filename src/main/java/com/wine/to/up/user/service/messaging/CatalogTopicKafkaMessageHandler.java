package com.wine.to.up.user.service.messaging;

import com.google.gson.Gson;
import com.google.protobuf.ByteString;
import com.wine.to.up.catalog.service.api.domain.NotificationServiceMessage;
import com.wine.to.up.commonlib.messaging.KafkaMessageHandler;
import com.wine.to.up.commonlib.messaging.KafkaMessageSender;
import com.wine.to.up.user.service.api.dto.UserServiceMessage;
import com.wine.to.up.user.service.api.message.KafkaMessageHeaderOuterClass;
import com.wine.to.up.user.service.api.message.KafkaMessageSentEventOuterClass.KafkaMessageSentEvent;
import com.wine.to.up.user.service.domain.response.WineNotificationMessage;
import com.wine.to.up.user.service.service.ListSubscriptionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;

import static java.util.stream.Collectors.toList;

@Component
@Slf4j
public class CatalogTopicKafkaMessageHandler implements KafkaMessageHandler<KafkaMessageSentEvent> {
    private final KafkaMessageSender<KafkaMessageSentEvent> kafkaSendMessageService;
    public final ListSubscriptionService listSubscriptionService;

    @Autowired
    public CatalogTopicKafkaMessageHandler(KafkaMessageSender<KafkaMessageSentEvent> kafkaSendMessageService,
                                           ListSubscriptionService listSubscriptionService) {
        this.kafkaSendMessageService = kafkaSendMessageService;
        this.listSubscriptionService = listSubscriptionService;
    }

    @Override
    public void handle(KafkaMessageSentEvent message) {
        Gson gson = new Gson();
        WineNotificationMessage reply = listSubscriptionService.getUserTokens(
                Long.parseLong(gson.fromJson(message.getMessage(), NotificationServiceMessage.class).getId()));
        reply.setName(gson.fromJson(message.getMessage(), NotificationServiceMessage.class).getId());
        reply.setPrice(Double.parseDouble(gson.fromJson(message.getMessage(), NotificationServiceMessage.class).getId()));
        UserServiceMessage msg = new UserServiceMessage(Collections.emptyMap(), reply.toString());
        kafkaSendMessageService.sendMessage(KafkaMessageSentEvent.newBuilder()
                .addAllHeaders(msg.getHeaders().entrySet().stream()
                        .map(entry -> KafkaMessageHeaderOuterClass.KafkaMessageHeader.newBuilder()
                                .setKey(entry.getKey())
                                .setValue(ByteString.copyFrom(entry.getValue()))
                                .build())
                        .collect(toList()))
                .setMessage(gson.toJson(reply.toString()))
                .build());
    }
}
