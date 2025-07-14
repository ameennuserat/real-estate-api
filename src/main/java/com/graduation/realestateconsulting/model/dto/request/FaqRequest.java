package com.graduation.realestateconsulting.model.dto.request;

import lombok.*;

@Data
@Builder
public class FaqRequest {

    private Long categoryId;
    private String question;
    private String answer;

}