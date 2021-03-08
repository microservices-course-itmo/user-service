package com.wine.to.up.user.service.messaging.serialization;

import com.google.protobuf.InvalidProtocolBufferException;
import com.wine.to.up.user.service.api.message.UserUpdatedEventOuterClass.UserUpdatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Deserializer;

@Slf4j
public class UserUpdatedDeserializer implements Deserializer<UserUpdatedEvent> {
    @Override
    public UserUpdatedEvent deserialize(String topic, byte[] data) {
        try {
            return UserUpdatedEvent.parseFrom(data);
        } catch (InvalidProtocolBufferException e) {
            log.error("Failed to deserialize message from topic: {}. {}", topic, e);
            return null;
        }
    }
}