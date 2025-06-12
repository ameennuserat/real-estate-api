package com.graduation.realestateconsulting.services;

import com.graduation.realestateconsulting.model.dto.request.FeedbackRequest;
import com.graduation.realestateconsulting.model.dto.response.FeedbackResponse;

import java.util.List;

public interface BookingFeedbackService {
    FeedbackResponse save(FeedbackRequest request);
    List<FeedbackResponse> getAll();
    FeedbackResponse getByBookingId(long bookingId);
    FeedbackResponse getById(Long id);
}
