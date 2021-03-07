package com.wine.to.up.user.service.messaging.serialization;

import com.google.protobuf.InvalidProtocolBufferException;
import com.wine.to.up.user.service.api.message.NewUserCreatedEventOuterClass.NewUserCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Deserializer;

@Slf4j
public class NewUserCreatedDeserializer implements Deserializer<NewUserCreatedEvent> {
    @Override
    public NewUserCreatedEvent deserialize(String topic, byte[] data) {
        try {
            return NewUserCreatedEvent.parseFrom(data);
        } catch (InvalidProtocolBufferException e) {
            log.error("Failed to deserialize message from topic: {}. {}", topic, e);
            return null;
        }
    }
}