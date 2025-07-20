package com.graduation.realestateconsulting.model.dto.response;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class MessageResponse {

    private Long id;
    private UserResponse sender;
    private String content;
    private LocalDateTime createdAt;

}
