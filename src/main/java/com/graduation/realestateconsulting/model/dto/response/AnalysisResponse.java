package com.graduation.realestateconsulting.model.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AnalysisResponse {

    long expertCount;
    long ClientCount;
    long officeCount;
    long propertyCount;
    long propertyImageCount;
    long ticketCount;
    long postsCount;
    long bookingCount;
    long reportCount;

}
