package com.graduation.realestateconsulting.model.dto.response;


import com.graduation.realestateconsulting.model.enums.RoomStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class RoomResponse {
    private Long id;
    private UserResponse user1;
    private UserResponse user2;
    private RoomStatus status;
    private LocalDate createdAt;
}
