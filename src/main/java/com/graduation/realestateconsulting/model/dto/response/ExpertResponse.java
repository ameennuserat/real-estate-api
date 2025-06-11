package com.graduation.realestateconsulting.model.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExpertResponse {

    private Long id;
    private UserResponse user;
    private String profession;
    private String experience;
    private double rating;
    private double rateCount;
    private String bio;
    private String idCardImage;
    private String degreeCertificateImage;
    private Double perMinuteVideo;
    private Double perMinuteAudio;

}