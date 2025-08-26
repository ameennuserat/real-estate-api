package com.graduation.realestateconsulting.trait;


import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.graduation.realestateconsulting.model.dto.request.PushNotificationRequest;
import com.graduation.realestateconsulting.model.enums.TargetPlatform;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class FirebaseMessagingService {

    private final FirebaseMessaging mobileFirebaseMessaging;
    private final FirebaseMessaging webFirebaseMessaging;

    public FirebaseMessagingService(
            @Qualifier("mobileFirebaseMessaging") FirebaseMessaging mobileFirebaseMessaging,
            @Qualifier("webFirebaseMessaging") FirebaseMessaging webFirebaseMessaging) {
        this.mobileFirebaseMessaging = mobileFirebaseMessaging;
        this.webFirebaseMessaging = webFirebaseMessaging;
    }


    public void sendNotification(PushNotificationRequest request, TargetPlatform platform) throws FirebaseMessagingException {
        Notification notification = Notification.builder()
                .setTitle(request.title())
                .setBody(request.message())
                .build();

        Message message = Message.builder()
                .setToken(request.token())
                .setNotification(notification)
                .build();

        if (platform == TargetPlatform.WEB) {
            webFirebaseMessaging.send(message);
        } else {
            mobileFirebaseMessaging.send(message);
        }
    }
}