package com.graduation.realestateconsulting.model.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OfficeRequest {


    private String bio;
    private String location;
    private Double latitude;
    private Double longitude;

}