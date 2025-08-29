package com.graduation.realestateconsulting.model.dto.response;


import com.graduation.realestateconsulting.model.enums.RoomStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomResponse {
    private Long id;
    private UserResponse user1;
    private UserResponse user2;
    private RoomStatus status;
    private LocalDate createdAt;
}
