package com.graduation.realestateconsulting.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OfficeResponse {

    private Long id;
    private UserResponse user;
    private String bio;
    private double rating;
    private double rateCount;
    private String location;
    private double latitude;
    private double longitude;
    private String commercialRegisterImage;

}