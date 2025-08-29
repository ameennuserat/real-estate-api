package com.graduation.realestateconsulting.model.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackResponse {
    private Long id;
    private int rating;
    private String review;
    private Long booking;
    private Long client;
    private LocalDateTime created_at;
}
