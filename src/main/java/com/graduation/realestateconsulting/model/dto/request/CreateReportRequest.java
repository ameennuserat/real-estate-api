package com.graduation.realestateconsulting.model.dto.request;

import com.graduation.realestateconsulting.model.enums.ReportReason;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateReportRequest(
        @NotNull(message = "You must specify the user you are reporting.")
        Long reportedUserId,

        @NotNull(message = "You must select a reason for the report.")
        Long categoryId,

        @NotBlank(message = "Description cannot be empty.")
        String description
) {
}
