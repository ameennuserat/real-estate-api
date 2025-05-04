package com.graduation.realestateconsulting.model.dto.request;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VerificationRequest {
    private String email;
    private String verificationCode;
}

