package com.graduation.realestateconsulting.model.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class FaqCategoryResponse {

    private Long id;
    private String name;
    private LocalDateTime createdAt;
}