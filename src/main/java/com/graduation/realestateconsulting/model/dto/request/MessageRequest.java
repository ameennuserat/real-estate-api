package com.graduation.realestateconsulting.model.dto.request;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MessageRequest {

    private Long roomId;
    private Long senderId;
    private String content;

}
