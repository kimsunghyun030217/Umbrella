package com.example.umbrella.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseInitializer {

    @PostConstruct
    public void initialize() {
        try {
            // JSON 키 파일 경로 (로컬에서만 존재)
            InputStream serviceAccount = new ClassPathResource("firebase/Firebase_Admin_SDK.json").getInputStream();


            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                System.out.println("🔥 Firebase Initialized Successfully");
            }
        } catch (IOException e) {
            throw new RuntimeException("🔥 Firebase Initialization Error: " + e.getMessage(), e);
        }
    }
}
