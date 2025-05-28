package com.graduation.realestateconsulting.model.dto.response;

import lombok.*;

@Data
@Builder
public class LoginResponse {
    private String token;
    private String refreshToken;
    private UserResponse user;
}
