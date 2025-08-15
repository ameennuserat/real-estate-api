package com.graduation.realestateconsulting.model.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AnalysisResponse {

    long expertCount;
    long expertCurrentMonthCount;
    double expertIncreasePercentage;

    long clientCount;
    long clientCurrentMonthCount;
    double clientIncreasePercentage;

    long officeCount;
    long officeCurrentMonthCount;
    double officeIncreasePercentage;

    long propertyCount;
    long propertyCurrentMonthCount;
    double propertyIncreasePercentage;

    long propertyImageCount;
    long propertyImageCurrentMonthCount;
    double propertyImageIncreasePercentage;

    long ticketCount;
    long ticketCurrentMonthCount;
    double ticketIncreasePercentage;

    long postsCount;
    long postsCurrentMonthCount;
    double postsIncreasePercentage;

    long bookingCount;
    long bookingCurrentMonthCount;
    double bookingIncreasePercentage;

    long reportCount;
    long reportCurrentMonthCount;
    double reportIncreasePercentage;

    double revenuesCurrentMonthCount;
    double revenuesIncreasePercentage;

}
