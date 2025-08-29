package com.graduation.realestateconsulting.model.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRoomResponse {
    private Long id;
    private UserResponse otherUser;
    private LocalDate createdAt;
}
