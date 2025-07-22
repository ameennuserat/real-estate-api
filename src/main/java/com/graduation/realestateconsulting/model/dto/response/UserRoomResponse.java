package com.graduation.realestateconsulting.model.dto.response;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class UserRoomResponse {
    private Long id;
    private UserResponse otherUser;
    private LocalDate createdAt;
}
