package com.graduation.realestateconsulting.model.dto.response;

import com.graduation.realestateconsulting.model.enums.Role;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String fcmToken;
    private boolean enabled;
    private Role role;
    private String imageUrl;
}
