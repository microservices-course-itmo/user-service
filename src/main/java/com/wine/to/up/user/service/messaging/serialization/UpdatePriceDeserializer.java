package com.wine.to.up.user.service.messaging.serialization;

import com.google.protobuf.InvalidProtocolBufferException;
import com.wine.to.up.catalog.service.api.message.UpdatePriceEventOuterClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Deserializer;

@Slf4j
public class UpdatePriceDeserializer implements Deserializer<UpdatePriceEventOuterClass.UpdatePriceEvent> {
    /**
     * {@inheritDoc}
     */
    @Override
    public UpdatePriceEventOuterClass.UpdatePriceEvent deserialize(String topic, byte[] bytes) {
        try {
            return UpdatePriceEventOuterClass.UpdatePriceEvent.parseFrom(bytes);
        } catch (InvalidProtocolBufferException e) {
            log.error("Failed to deserialize message from topic: {}. {}", topic, e);
            return null;
        }
    }
}
