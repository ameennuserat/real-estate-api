package com.graduation.realestateconsulting.model.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostsExpertResponse {

    private Long id;
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String profession;
    private String experience;
    private double rating;
    private double rateCount;
    private String imageUrl;

}