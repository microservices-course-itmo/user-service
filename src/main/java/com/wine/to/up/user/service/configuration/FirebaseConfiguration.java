package com.wine.to.up.user.service.configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import java.io.FileInputStream;
import java.io.IOException;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class FirebaseConfiguration {
    @Value("${firebase.url}")
    private String firebaseUrl;

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
            log.debug("Firebase config is missing");
        }
    }
}
