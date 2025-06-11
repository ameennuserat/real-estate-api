package com.graduation.realestateconsulting.model.dto.response;

import com.graduation.realestateconsulting.model.enums.BookingStatus;
import com.graduation.realestateconsulting.model.enums.CallType;
import com.graduation.realestateconsulting.model.enums.RefundStatus;
import lombok.Builder;
import lombok.Data;
import org.mapstruct.control.MappingControl;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
public class BookingResponse {
    private Long id;
    private ClientResponse client;
    private ExpertResponse expert;
    private CallType callType;
    private int duration;
    private Double bookingCost;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BookingStatus bookingStatus;
    private RefundStatus refundStatus;
    private LocalDateTime scheduled_at;
    private LocalDateTime cancelled_at;
    private UserResponse cancelled_by;
    private String cancellationReason;
    private FeedbackResponse feedback;
}
