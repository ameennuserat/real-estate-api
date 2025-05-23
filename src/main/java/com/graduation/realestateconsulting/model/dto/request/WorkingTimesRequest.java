package com.graduation.realestateconsulting.model.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.graduation.realestateconsulting.model.enums.CallType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.cglib.core.Local;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class WorkingTimesRequest {
    private DayOfWeek day;

    @Schema(type = "string", example = "11:00", description = "Start time in HH:mm format")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime startTime;

    @Schema(type = "string", example = "17:30", description = "End time in HH:mm format")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime endTime;

    private CallType callType;
}
