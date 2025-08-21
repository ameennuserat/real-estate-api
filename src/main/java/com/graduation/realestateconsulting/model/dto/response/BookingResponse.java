package com.graduation.realestateconsulting.model.dto.response;

import com.graduation.realestateconsulting.model.entity.User;
import com.graduation.realestateconsulting.model.enums.BookingStatus;
import com.graduation.realestateconsulting.model.enums.CallType;
import com.graduation.realestateconsulting.model.enums.RefundStatus;
import lombok.Builder;
import lombok.Data;
import org.mapstruct.control.MappingControl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
public class BookingResponse {
    private Long id;
    private UserResponse client;
    private ExpertResponse expert;
    private String clientSecret;
    private CallType callType;
    private int duration;

    private BigDecimal originalPrice;
    private BigDecimal discountAmount;
    private BigDecimal finalPrice;

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
