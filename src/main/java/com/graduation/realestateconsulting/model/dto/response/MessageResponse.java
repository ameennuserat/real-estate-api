package com.graduation.realestateconsulting.model.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponse {

    private Long id;
    private UserResponse sender;
    private String content;
    private LocalDateTime createdAt;

    // for file
    private String fileName;
    private String fileType;
    private String fileUrl;

}
