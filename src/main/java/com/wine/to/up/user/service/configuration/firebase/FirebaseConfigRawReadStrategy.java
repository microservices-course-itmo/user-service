package com.wine.to.up.user.service.configuration.firebase;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
@Order(1)
public class FirebaseConfigRawReadStrategy implements FirebaseConfigReadStrategy {
    @Value("${firebase.configFileName}")
    private String path;

    @Override
    public boolean validate() {
        return this.getClass().getClassLoader().getResourceAsStream(path) != null;
    }

    @Override
    public InputStream read() {
        return this.getClass().getClassLoader().getResourceAsStream(path);
    }
}
