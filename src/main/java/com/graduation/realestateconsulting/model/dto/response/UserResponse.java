package com.graduation.realestateconsulting.model.dto.response;

import com.graduation.realestateconsulting.model.enums.Role;
import com.graduation.realestateconsulting.model.enums.UserStatus;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String fcmToken;
    private boolean enabled;
    private Role role;
    private UserStatus status;
    private String imageUrl;
    private int warnsCount;
    private int blocksCount;
}
