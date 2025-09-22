package com.wise.app.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {

    @Bean
    public Firestore firestore() throws IOException {
        // credentials json이 null이면 명확한 예외
        InputStream serviceAccount = getClass().getClassLoader()
                .getResourceAsStream("wsie-db-firebase-adminsdk-fbsvc-4b52e07dba.json");
        if (serviceAccount == null) {
            throw new IllegalStateException("Firebase credentials file not found in resources");
        }

        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();
            FirebaseApp.initializeApp(options);
        }

        return FirestoreClient.getFirestore();
    }
}
