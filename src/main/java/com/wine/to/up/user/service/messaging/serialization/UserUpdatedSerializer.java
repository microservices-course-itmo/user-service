package com.wine.to.up.user.service.messaging.serialization;

import com.wine.to.up.user.service.api.message.UserUpdatedEventOuterClass.UserUpdatedEvent;
import org.apache.kafka.common.serialization.Serializer;

public class UserUpdatedSerializer implements Serializer<UserUpdatedEvent> {
    @Override
    public byte[] serialize(String topic, UserUpdatedEvent data) {
        return data.toByteArray();
    }
}