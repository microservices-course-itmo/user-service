package com.wine.to.up.user.service.messaging.serialization;

import com.wine.to.up.catalog.service.api.message.UpdatePriceMessageSentEventOuterClass;
import org.apache.kafka.common.serialization.Serializer;

public class UpdatePriceSerializer implements Serializer<UpdatePriceMessageSentEventOuterClass.UpdatePriceMessageSentEvent> {
    @Override
    public byte[] serialize(String topic, UpdatePriceMessageSentEventOuterClass.UpdatePriceMessageSentEvent data) {
        return data.toByteArray();
    }
}
