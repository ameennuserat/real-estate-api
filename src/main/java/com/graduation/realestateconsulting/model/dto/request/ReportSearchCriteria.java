package com.graduation.realestateconsulting.model.dto.request;

import java.time.LocalDate;

public record ReportSearchCriteria(
        Long reportedUserId,
        Long reporterUserId,
        Long categoryId,
        String descriptionKeyword,
        LocalDate startDate,
        LocalDate endDate
) {
}
