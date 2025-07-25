package com.graduation.realestateconsulting.model.dto.request;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
public class PostsRequest {

    private Long expertId;
    private String content;
    private MultipartFile image;

}