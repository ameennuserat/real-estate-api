package com.graduation.realestateconsulting.model.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExpertRequest {
    private String profession;
    private String experience;
    private String bio;
    private Double perMinuteVideo;
    private Double perMinuteAudio;
}