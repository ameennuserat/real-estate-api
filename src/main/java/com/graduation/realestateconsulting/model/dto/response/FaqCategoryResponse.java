package com.graduation.realestateconsulting.model.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class FaqCategoryResponse {

    private Long id;
    private String name;
    private List<FaqResponse> faqs;
    private LocalDateTime createdAt;
}