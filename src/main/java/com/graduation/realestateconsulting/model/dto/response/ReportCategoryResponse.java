package com.graduation.realestateconsulting.model.dto.response;

import lombok.Builder;

@Builder
public record ReportCategoryResponse(
        Long id,
        String title
) {
}
