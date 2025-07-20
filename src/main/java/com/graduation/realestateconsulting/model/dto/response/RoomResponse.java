package com.graduation.realestateconsulting.model.dto.response;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class RoomResponse {
    private UserResponse user1;
    private UserResponse user2;
    private String roomKey;
    private LocalDate createdAt;
}
