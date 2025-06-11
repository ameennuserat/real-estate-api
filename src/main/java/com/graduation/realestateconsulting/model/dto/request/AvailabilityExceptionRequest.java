package com.graduation.realestateconsulting.model.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.security.Timestamp;
import java.time.LocalDateTime;

@Data
@Builder
public class AvailabilityExceptionRequest {

    @NotNull(message = "start time required")
    //@Schema(description = "The start time of the exception", example = "2025-05-28T10:30:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime startTime;

    @NotNull(message = "end time required")
   // @Schema(description = "The end time of the exception", example = "2025-05-28T12:30:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime endTime;

    @Schema(description = "Indicates if the resource is available during this period", example = "false")
    private boolean available;
}