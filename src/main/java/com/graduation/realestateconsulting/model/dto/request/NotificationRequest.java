package com.graduation.realestateconsulting.model.dto.request;

import com.graduation.realestateconsulting.model.entity.User;
import lombok.Builder;

@Builder
public record NotificationRequest(
        User user,
        String message,
        String title
) {
}
