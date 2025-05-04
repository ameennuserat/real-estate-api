package com.graduation.realestateconsulting.model.dto.response;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private String token;
    private String refreshToken;
    private UserResponse user;
}
