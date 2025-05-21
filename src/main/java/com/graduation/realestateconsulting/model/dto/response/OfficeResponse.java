package com.graduation.realestateconsulting.model.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OfficeResponse {

    private Long id;
    private UserResponse user;
    private String bio;
    private String location;
    private double latitude;
    private double longitude;
    private String commercialRegisterImage;

}