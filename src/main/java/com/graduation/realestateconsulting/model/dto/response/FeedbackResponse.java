package com.graduation.realestateconsulting.model.dto.response;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class FeedbackResponse {
    private Long id;
    private int rating;
    private String review;
    private Long booking;
    private Long client;
    private LocalDateTime created_at;
}
