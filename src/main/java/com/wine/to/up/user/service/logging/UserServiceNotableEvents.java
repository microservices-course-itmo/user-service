package com.wine.to.up.user.service.logging;

import com.wine.to.up.commonlib.logging.NotableEvent;

public enum UserServiceNotableEvents implements NotableEvent {
    I_KAFKA_SEND_MESSAGE_SUCCESS("Kafka send message: {}"),
    I_CONTROLLER_RECEIVED_MESSAGE("Message: {}"),
    W_SOME_WARN_EVENT("Warn situation. Description: {}"),
    F_FIREBASE_CONFIG_LOAD_FAILURE("Fatal: firebase config is missing. {}"),
    W_KAFKA_MESSAGE_DESERIALIZATION_FAILURE("Warning: message cannot be deserialized. {}"),
    W_AUTH_FAILURE("Authentication failed: {}");


    private final String template;

    UserServiceNotableEvents(String template) {
        this.template = template;
    }

    public String getTemplate() {
        return template;
    }

    @Override
    public String getName() {
        return name();
    }
}
