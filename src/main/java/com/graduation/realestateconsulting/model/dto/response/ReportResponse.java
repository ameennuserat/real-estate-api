package com.graduation.realestateconsulting.model.dto.response;

import com.graduation.realestateconsulting.model.entity.ReportCategory;
import com.graduation.realestateconsulting.model.enums.ReportReason;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ReportResponse(
        Long id,
        UserResponse reporterUser,
        UserResponse reportedUser,
        ReportCategoryResponse category,
        String description,
        LocalDateTime createdAt
) {
}
