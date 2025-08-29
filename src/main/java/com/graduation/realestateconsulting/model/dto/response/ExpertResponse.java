package com.graduation.realestateconsulting.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
    private Integer followersCount;
    private Integer favoritesCount;
    private boolean newExpert;

}