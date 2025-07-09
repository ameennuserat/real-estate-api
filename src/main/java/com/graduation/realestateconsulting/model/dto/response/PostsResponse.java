package com.graduation.realestateconsulting.model.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PostsResponse {

    private Long id;

    private PostsExpertResponse expert;

    private String content;

    private String imageUrl;

    private LocalDateTime createdAt;

}