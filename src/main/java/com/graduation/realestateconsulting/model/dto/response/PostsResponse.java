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
public class PostsResponse {

    private Long id;

    private PostsExpertResponse expert;

    private String content;

    private String imageUrl;

    private LocalDateTime createdAt;

}