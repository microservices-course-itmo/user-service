package com.wine.to.up.user.service.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.ByteString;
import com.wine.to.up.catalog.service.api.domain.NotificationServiceMessage;
import com.wine.to.up.commonlib.annotations.InjectEventLogger;
import com.wine.to.up.commonlib.logging.EventLogger;
import com.wine.to.up.commonlib.messaging.KafkaMessageHandler;
import com.wine.to.up.commonlib.messaging.KafkaMessageSender;
import com.wine.to.up.user.service.api.dto.UserServiceMessage;
import com.wine.to.up.user.service.api.dto.WineResponse;
import com.wine.to.up.user.service.api.message.KafkaMessageHeaderOuterClass;
import com.wine.to.up.user.service.api.message.KafkaMessageSentEventOuterClass.KafkaMessageSentEvent;
import com.wine.to.up.user.service.logging.UserServiceNotableEvents;
import com.wine.to.up.user.service.service.ListSubscriptionService;
import java.util.Collections;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CatalogTopicKafkaMessageHandler implements KafkaMessageHandler<KafkaMessageSentEvent> {
    public final ListSubscriptionService listSubscriptionService;
    private final KafkaMessageSender<KafkaMessageSentEvent> kafkaSendMessageService;
    private final ObjectMapper objectMapper;

    @InjectEventLogger
    private EventLogger eventLogger;

    @Autowired
    public CatalogTopicKafkaMessageHandler(KafkaMessageSender<KafkaMessageSentEvent> kafkaSendMessageService,
                                           ListSubscriptionService listSubscriptionService,
                                           ObjectMapper objectMapper) {
        this.kafkaSendMessageService = kafkaSendMessageService;
        this.listSubscriptionService = listSubscriptionService;
        this.objectMapper = objectMapper;
    }

    @Override
    public void handle(KafkaMessageSentEvent message) {
        NotificationServiceMessage notificationServiceMessage;

        try {
            notificationServiceMessage = objectMapper.readValue(message.getMessage(), NotificationServiceMessage.class);
        } catch (JsonProcessingException e) {
            eventLogger.warn(UserServiceNotableEvents.W_KAFKA_MESSAGE_DESERIALIZATION_FAILURE);
            return;
        }

        WineResponse response;
        try {
            response = listSubscriptionService.getUserTokens(
                Long.parseLong(notificationServiceMessage.getId())
            );
        } catch (NumberFormatException ex) {
            eventLogger.warn(UserServiceNotableEvents.W_KAFKA_MESSAGE_DESERIALIZATION_FAILURE);
            return;
        }

        response.setWineName(notificationServiceMessage.getName());
        response.setNewWinePrice((float) notificationServiceMessage.getPrice());

        UserServiceMessage responseMessage = new UserServiceMessage(Collections.emptyMap(), response.toString());
        kafkaSendMessageService.sendMessage(KafkaMessageSentEvent.newBuilder()
            .addAllHeaders(responseMessage.getHeaders().entrySet().stream()
                .map(entry -> KafkaMessageHeaderOuterClass.KafkaMessageHeader.newBuilder()
                    .setKey(entry.getKey())
                    .setValue(ByteString.copyFrom(entry.getValue()))
                    .build())
                .collect(Collectors.toList()))
            .setMessage(responseMessage.getMessage())
            .build());
    }
}
