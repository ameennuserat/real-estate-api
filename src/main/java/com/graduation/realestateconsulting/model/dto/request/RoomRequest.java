package com.graduation.realestateconsulting.model.dto.request;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoomRequest {
    private Long userId1;
    private Long userId2;
}
