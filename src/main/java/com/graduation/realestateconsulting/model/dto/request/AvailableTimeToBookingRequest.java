package com.graduation.realestateconsulting.model.dto.request;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class AvailableTimeToBookingRequest {
    private long expertId;
    private LocalDate date;
    private int duration;
}
