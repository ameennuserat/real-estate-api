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
public class FaqResponse {

    private Long id;
    private String question;
    private String answer;
    private FaqCategoryResponse category;
    private LocalDateTime createdAt;

}