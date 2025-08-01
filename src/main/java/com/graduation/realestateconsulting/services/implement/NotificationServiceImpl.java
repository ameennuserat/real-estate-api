package com.graduation.realestateconsulting.services.implement;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.graduation.realestateconsulting.model.dto.request.NotificationRequest;
import com.graduation.realestateconsulting.model.dto.request.PushNotificationRequest;
import com.graduation.realestateconsulting.model.dto.response.NotificationResponse;
import com.graduation.realestateconsulting.model.entity.Notification;
import com.graduation.realestateconsulting.model.entity.User;
import com.graduation.realestateconsulting.model.enums.Role;
import com.graduation.realestateconsulting.model.enums.TargetPlatform;
import com.graduation.realestateconsulting.model.mapper.NotificationMapper;
import com.graduation.realestateconsulting.repository.NotificationRepository;
import com.graduation.realestateconsulting.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import com.graduation.realestateconsulting.services.NotificationService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final RabbitTemplate rabbitTemplate;
    private final UserRepository userRepository;
    private final NotificationMapper notificationMapper;

    @Value("${rabbitmq.queue.notification}")
    private String notificationQueueName;

    @Override
    public Page<NotificationResponse> getNotifications(User user, Pageable pageable) {
        Page<Notification> notifications = notificationRepository.findByUserOrderByCreatedAtDesc(user, pageable);
        return notifications.map(notificationMapper::toDto);
    }

    @Override
    @Transactional
    public void createAndSendNotification(NotificationRequest notificationRequest) {
        Notification notification = Notification.builder()
                .user(notificationRequest.user())
                .title(notificationRequest.title())
                .message(notificationRequest.message())
                .build();
        notificationRepository.save(notification);

        if (notification.getUser().getFcmToken() != null && !notification.getUser().getFcmToken().isEmpty()) {

            TargetPlatform platform = TargetPlatform.MOBILE; //notification.getUser().getRole() == Role.ADMIN  ?
                                        //TargetPlatform.WEB : TargetPlatform.MOBILE;

            PushNotificationRequest pushRequest = PushNotificationRequest.builder()
                    .title(notificationRequest.title())
                    .message(notificationRequest.message())
                    .platform(platform)
                    .token(notification.getUser().getFcmToken())
                    .build();

            // add message to Queue
            rabbitTemplate.convertAndSend(notificationQueueName, pushRequest);
        }
    }
}