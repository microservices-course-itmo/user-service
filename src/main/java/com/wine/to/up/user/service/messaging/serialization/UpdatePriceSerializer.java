package com.wine.to.up.user.service.messaging.serialization;

import com.wine.to.up.catalog.service.api.message.UpdatePriceEventOuterClass;
import org.apache.kafka.common.serialization.Serializer;

public class UpdatePriceSerializer implements Serializer<UpdatePriceEventOuterClass.UpdatePriceEvent> {
    @Override
    public byte[] serialize(String topic, UpdatePriceEventOuterClass.UpdatePriceEvent data) {
        return data.toByteArray();
    }
}
