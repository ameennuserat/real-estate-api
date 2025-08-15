package com.graduation.realestateconsulting.model.mapper;

import com.graduation.realestateconsulting.model.dto.request.BookingRequest;
import com.graduation.realestateconsulting.model.dto.request.DiscountResult;
import com.graduation.realestateconsulting.model.dto.response.BookingResponse;
import com.graduation.realestateconsulting.model.entity.Booking;
import com.graduation.realestateconsulting.model.entity.Client;
import com.graduation.realestateconsulting.model.entity.Expert;
import com.graduation.realestateconsulting.model.enums.BookingStatus;
import com.graduation.realestateconsulting.model.enums.CallType;
import com.graduation.realestateconsulting.services.implement.BookingServiceImpl;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//@Mapper(uses = {ClientMapper.class, ExpertMapper.class, UserMapper.class})
@Data
@Builder
@RequiredArgsConstructor
@Service
public class BookingMapper {
    private final UserMapper userMapper;
    private final ClientMapper clientMapper;
    private final ExpertMapper expertMapper;
    private final FeedbackMapper feedbackMapper;
    // (request, expert, client, startTime, end, price)

    public BookingResponse toDto(Booking booking) {
        return toDto(booking, Optional.empty());
    }

    public Booking toEntity(BookingRequest request, Expert expert, Client client, LocalDateTime startTime, LocalDateTime endTime, BigDecimal originalPrice, DiscountResult discountResult) {
        return Booking.builder()
                .expert(expert)
                .client(client)
                .startTime(startTime)
                .endTime(endTime)
                .duration(request.getDuration())
                .callType(request.getCallType())
                .bookingStatus(BookingStatus.PENDING)
                .originalPrice(originalPrice)
                .discountAmount(discountResult.discountAmount())
                .finalPrice(discountResult.finalPrice())
                .coupon(discountResult.appliedCoupon() != null ?
                        discountResult.appliedCoupon() :
                        null)
                .build();
    }

    public BookingResponse toDto(Booking booking, Optional<String> clientSecret) {
        return BookingResponse.builder()
                .id(booking.getId())
                .callType(booking.getCallType())
                .duration(booking.getDuration())
                .clientSecret(clientSecret.orElse(null))
                .cancellationReason(booking.getCancellationReason())
                .scheduled_at(booking.getScheduled_at())
                .finalPrice(booking.getFinalPrice())
                .discountAmount(booking.getDiscountAmount())
                .originalPrice(booking.getOriginalPrice())
                .cancelled_at(booking.getCancelled_at())
                .refundStatus(booking.getRefundStatus())
                .bookingStatus(booking.getBookingStatus())
                .cancelled_by(Optional.ofNullable(booking.getUser())
                        .map(userMapper::toDto)
                        .orElse(null)
                )
                .expert(expertMapper.toDto(booking.getExpert()))
                .client(clientMapper.toDto(booking.getClient()))
                .startTime(booking.getStartTime())
                .endTime(booking.getEndTime())
                .feedback(Optional.ofNullable(booking.getBookingFeedback())
                        .map(feedbackMapper::toDto)
                        .orElse(null))
                .build();
    }

    public List<BookingResponse> toDtos(List<Booking> bookings) {
        return bookings.stream().map(this::toDto).collect(Collectors.toList());
    }
//    @Mappings({
//            @Mapping(target = "startTime", source = "startTime"),
//            @Mapping(target = "endTime", source = "endTime"),
//            @Mapping(target = "bookingCost", source = "cost"),
//            @Mapping(target = "bookingStatus", constant = "CONFIRMED"),
//            @Mapping(target = "expert", source = "expert"),
//            @Mapping(target = "client", source = "client"),
//            //@Mapping(target = "refundStatus", ignore = true),
//           // @Mapping(target = "scheduled_at", ignore = true),
//            @Mapping(target = "cancelled_at", ignore = true),
//            @Mapping(target = "cancellationReason", ignore = true),
//            @Mapping(target = "user", ignore = true),
//            @Mapping(target = "id", ignore = true),
//            @Mapping(target = "bookingFeedback", ignore = true)
//    })
//    Booking toEntity(
//            BookingRequest request,
//            Expert expert,
//            Client client,
//            LocalDateTime startTime,
//            LocalDateTime endTime,
//            Double cost,
//            BookingStatus status
//    );
//
//
//    @Mappings({
//            @Mapping(target = "bookingFeedback", ignore = true)
//    })
//    BookingResponse toDto(Booking booking);
//
//    List<BookingResponse> toDtos(List<Booking> bookings);
}