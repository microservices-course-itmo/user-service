package com.wine.to.up.user.service.messaging.serialization;

import com.wine.to.up.user.service.api.message.NewUserCreatedEventOuterClass;
import org.apache.kafka.common.serialization.Serializer;

public class NewUserCreatedSerializer implements Serializer<NewUserCreatedEventOuterClass.NewUserCreatedEvent> {
    @Override
    public byte[] serialize(String topic, NewUserCreatedEventOuterClass.NewUserCreatedEvent data) {
        return data.toByteArray();
    }
}