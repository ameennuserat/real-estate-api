package com.graduation.realestateconsulting.model.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.graduation.realestateconsulting.model.enums.CallType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
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
    @Schema(type = "string", example = "12:00", description = "Start time in HH:mm format")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime startDate;
}
