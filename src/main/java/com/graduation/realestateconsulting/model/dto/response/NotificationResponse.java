package com.graduation.realestateconsulting.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
public record NotificationResponse(
         String tittle,
         String message
) {
}
