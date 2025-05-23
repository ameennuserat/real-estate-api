package com.graduation.realestateconsulting.model.dto.response;

import com.graduation.realestateconsulting.model.enums.CallType;
import lombok.Builder;
import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Data
@Builder
public class WorkingTimesResponse {
    private int id;
    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private CallType callType;
}