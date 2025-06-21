package com.graduation.realestateconsulting.model.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.graduation.realestateconsulting.model.enums.CallType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingRequest {
    private Long expertId;
    private Long clientId;
    private CallType callType;
    private int duration;
    @Schema(
            type = "string",
            example = "2025-06-18T12:00",
            description = "Start date and time in yyyy-MM-dd'T'HH:mm format "
    )
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime startDate;
    @Schema(description = "Optional coupon code to apply for a discount.", example = "SAVE20")
    private String couponCode;
}