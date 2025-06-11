package com.graduation.realestateconsulting.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.security.Timestamp;
import java.time.LocalDateTime;

@Data
@Builder
public class UpdateAvailabilityExceptionRequest {

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private boolean available;
}
