package com.graduation.realestateconsulting.model.mapper;

import com.graduation.realestateconsulting.model.dto.response.NotificationResponse;
import com.graduation.realestateconsulting.model.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationMapper {

    public NotificationResponse toDto(Notification notification) {
        return NotificationResponse.builder()
                .message(notification.getMessage())
                .tittle(notification.getTitle())
                .build();
    }

    public List<NotificationResponse> toDtos(List<Notification> notifications) {
        return notifications.stream().map(this::toDto).collect(Collectors.toList());
    }
}