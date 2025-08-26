package com.graduation.realestateconsulting.model.dto.request;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatFileRequest {
    private Long roomId;
    private Long senderId;
    private String fileName;
    private String fileType;
    private byte[] filaData;
}
