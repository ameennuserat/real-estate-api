package com.graduation.realestateconsulting.services.implement;

import com.graduation.realestateconsulting.model.dto.request.PushNotificationRequest;
import com.graduation.realestateconsulting.trait.FirebaseMessagingService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationConsumer {

    private final FirebaseMessagingService firebaseService;

    @RabbitListener(queues = "${rabbitmq.queue.notification}")
    public void handleNotificationRequest(PushNotificationRequest request) {
        System.out.println("Received notification request from queue for token: " + request.token() + " on platform: " + request.platform());
        try {
            firebaseService.sendNotification(request, request.platform());
        } catch (Exception e) {
          throw new IllegalArgumentException(e);
        }
    }
}
