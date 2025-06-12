package com.graduation.realestateconsulting.model.dto.response;

import lombok.Builder;
import lombok.Data;

import java.security.Timestamp;
import java.time.LocalDateTime;

@Data
@Builder
public class AvailabilityExceptionResponse {

    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Boolean available;
}
