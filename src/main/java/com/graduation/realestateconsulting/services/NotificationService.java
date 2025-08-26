package com.graduation.realestateconsulting.services;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.graduation.realestateconsulting.model.dto.request.NotificationRequest;
import com.graduation.realestateconsulting.model.dto.response.NotificationResponse;
import com.graduation.realestateconsulting.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface NotificationService {
        Page<NotificationResponse> getNotifications(User user, Pageable pageable);
        void createAndSendNotification(NotificationRequest notificationRequest) ;
}