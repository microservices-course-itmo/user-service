package com.wine.to.up.user.service.messaging.serialization;

import com.wine.to.up.user.service.api.message.FavoritesUpdatedEventOuterClass.FavoritesUpdatedEvent;
import org.apache.kafka.common.serialization.Serializer;

public class FavoritesUpdatedSerializer implements Serializer<FavoritesUpdatedEvent> {
    @Override
    public byte[] serialize(String topic, FavoritesUpdatedEvent data) {
        return data.toByteArray();
    }
}