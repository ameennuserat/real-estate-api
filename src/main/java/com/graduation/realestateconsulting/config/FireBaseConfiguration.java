package com.graduation.realestateconsulting.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

@Configuration
public class FireBaseConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(FireBaseConfiguration.class);

    // أسماء متغيرات أوضح
    @Value("${firebase.project-mobile.key-path}")
    private String mobileKeyPath;

    @Value("${firebase.project-web.key-path}")
    private String webKeyPath;

    // اسم معبر للمشروع الثاني
    public static final String WEB_APP_NAME = "WebAppDashboard";

    @PostConstruct
    public void initializeFirebaseApps() {
        try {
            initializeProjectMobile();
            initializeProjectWeb();
        } catch (IOException e) {
            logger.error("Could not initialize Firebase projects", e);
        }
    }

    private void initializeProjectMobile() throws IOException {
        if (FirebaseApp.getApps().stream().noneMatch(app -> app.getName().equals(FirebaseApp.DEFAULT_APP_NAME))) {
            GoogleCredentials credentials = GoogleCredentials.fromStream(
                    new ClassPathResource(this.mobileKeyPath).getInputStream()
            );
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(credentials)
                    .build();

            FirebaseApp.initializeApp(options, FirebaseApp.DEFAULT_APP_NAME);

            logger.info("Firebase Default App (Mobile Project) initialized successfully using path: {}", this.mobileKeyPath);
        } else {
            logger.info("Firebase Default App (Mobile Project) already initialized.");
        }
    }

    private void initializeProjectWeb() throws IOException {
        if (FirebaseApp.getApps().stream().noneMatch(app -> app.getName().equals(WEB_APP_NAME))) {
            GoogleCredentials credentials = GoogleCredentials.fromStream(
                    new ClassPathResource(this.webKeyPath).getInputStream()
            );
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(credentials)
                    .build();

            FirebaseApp.initializeApp(options, WEB_APP_NAME);

            logger.info("Firebase Named App (Web Project) initialized successfully using path: {}", this.webKeyPath);
        } else {
            logger.info("Firebase Named App (Web Project) already initialized.");
        }
    }

    @Bean
    @Primary
    public FirebaseMessaging mobileFirebaseMessaging() {
        return FirebaseMessaging.getInstance(FirebaseApp.getInstance(FirebaseApp.DEFAULT_APP_NAME));
    }

    @Bean("webFirebaseMessaging")
    public FirebaseMessaging webFirebaseMessaging() {
        return FirebaseMessaging.getInstance(FirebaseApp.getInstance(WEB_APP_NAME));
    }
}