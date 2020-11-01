package com.wine.to.up.user.service.messaging.serialization;

import com.wine.to.up.user.service.api.message.WinePriceUpdatedWithTokensEventOuterClass.WinePriceUpdatedWithTokensEvent;
import org.apache.kafka.common.serialization.Serializer;

public class WinePriceUpdatedWithTokensSerializer implements Serializer<WinePriceUpdatedWithTokensEvent> {
    @Override
    public byte[] serialize(String topic, WinePriceUpdatedWithTokensEvent data) {
        return data.toByteArray();
    }
}
