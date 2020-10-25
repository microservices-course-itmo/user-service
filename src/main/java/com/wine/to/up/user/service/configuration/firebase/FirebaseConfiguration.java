package com.wine.to.up.user.service.configuration.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.wine.to.up.commonlib.annotations.InjectEventLogger;
import com.wine.to.up.commonlib.logging.EventLogger;
import com.wine.to.up.user.service.logging.UserServiceNotableEvents;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FirebaseConfiguration {
    private final List<FirebaseConfigReadStrategy> readStrategies;

    @Value("${firebase.url}")
    private String firebaseUrl;

    @InjectEventLogger
    private EventLogger eventLogger;

    @Autowired
    public FirebaseConfiguration(List<FirebaseConfigReadStrategy> readStrategies) {
        this.readStrategies = readStrategies;
    }

    @PostConstruct
    private void init() {
        FirebaseConfigReadStrategy readStrategy = null;

        for (FirebaseConfigReadStrategy strategy : readStrategies) {
            if (strategy.validate()) {
                readStrategy = strategy;
                break;
            }
        }

        if (readStrategy == null) {
            eventLogger.error(
                UserServiceNotableEvents.F_FIREBASE_CONFIG_LOAD_FAILURE,
                "No firebase config file provided"
            );
            return;
        }

        try {
            FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(readStrategy.read()))
                .setDatabaseUrl(firebaseUrl)
                .build();
            FirebaseApp.initializeApp(options);
        } catch (IOException | GeneralSecurityException e) {
            eventLogger.error(UserServiceNotableEvents.F_FIREBASE_CONFIG_LOAD_FAILURE);
        }
    }
}

