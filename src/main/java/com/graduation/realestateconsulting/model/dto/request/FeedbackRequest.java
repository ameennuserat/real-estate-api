package com.graduation.realestateconsulting.model.dto.request;


import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class FeedbackRequest {
    private Long bookingId;
    private Long clientId;
    private int rating;
    private String review;
}
