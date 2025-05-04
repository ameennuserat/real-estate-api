package com.graduation.realestateconsulting.model.dto.request;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    private String email;
    private String password;
    private String fcmToken;
}
