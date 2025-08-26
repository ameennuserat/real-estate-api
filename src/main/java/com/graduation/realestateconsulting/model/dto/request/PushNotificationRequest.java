package com.graduation.realestateconsulting.model.dto.request;

import com.graduation.realestateconsulting.model.enums.TargetPlatform;
import lombok.Builder;

@Builder
public record PushNotificationRequest(
        String title,
        String message,
        String token,
        TargetPlatform platform
        ){
}
