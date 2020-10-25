package com.wine.to.up.user.service.configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.wine.to.up.commonlib.annotations.InjectEventLogger;
import com.wine.to.up.commonlib.logging.EventLogger;
import com.wine.to.up.user.service.logging.UserServiceNotableEvents;
import java.io.FileInputStream;
import java.io.IOException;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FirebaseConfiguration {
    @Value("${firebase.url}")
    private String firebaseUrl;

    @InjectEventLogger
    private EventLogger eventLogger;

    @PostConstruct
    private void init() {
        try {
            FileInputStream serviceAccount =
                new FileInputStream("firebase.json");
            FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl(firebaseUrl)
                .build();

            FirebaseApp.initializeApp(options);
        } catch (IOException e) {
            eventLogger.error(UserServiceNotableEvents.F_FIREBASE_CONFIG_LOAD_FAILURE);
        }
    }
}
