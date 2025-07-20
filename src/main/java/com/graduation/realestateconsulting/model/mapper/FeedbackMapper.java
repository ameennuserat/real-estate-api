package com.graduation.realestateconsulting.model.mapper;

import com.graduation.realestateconsulting.model.dto.request.FeedbackRequest;
import com.graduation.realestateconsulting.model.dto.response.FeedbackResponse;
import com.graduation.realestateconsulting.model.entity.Booking;
import com.graduation.realestateconsulting.model.entity.BookingFeedback;
import com.graduation.realestateconsulting.model.entity.Client;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
//@Mapper(uses = {ClientMapper.class, BookingMapper.class})
public class FeedbackMapper {


    public BookingFeedback toEntity(FeedbackRequest feedbackRequest,Booking booking,Client client) {
        return BookingFeedback.builder()
                .rating(feedbackRequest.getRating())
                .review(feedbackRequest.getReview())
                .client(client)
                .booking(booking)
                .build();
    }

    public FeedbackResponse toDto(BookingFeedback bookingFeedback) {
        return FeedbackResponse.builder()
                .id(bookingFeedback.getId())
                .rating(bookingFeedback.getRating())
                .review(bookingFeedback.getReview())
                .booking(bookingFeedback.getBooking().getId())
                .client(bookingFeedback.getClient().getId())
                .created_at(bookingFeedback.getCreated_at())
                .build();
    }

    public List<FeedbackResponse> toDtos(List<BookingFeedback> bookingFeedbacks) {
        return bookingFeedbacks.stream().map(this::toDto).collect(Collectors.toList());
    }
//    @Mappings({
//            @Mapping(target = "id", ignore = true),
//            @Mapping(target = "created_at", ignore = true),
//            @Mapping(target = "booking", source = "booking"),
//            @Mapping(target = "client", source = "client"),
//            @Mapping(target = "rating", source = "request.rating"),
//            @Mapping(target = "review", source = "request.review")
//    })
//    BookingFeedback toEntity(FeedbackRequest request, Booking booking, Client client);
//    FeedbackResponse toDto(BookingFeedback feedback);
//    List<FeedbackResponse> toDtoList(List<BookingFeedback> requests);
}