package com.graduation.realestateconsulting.model.dto.response;

import com.graduation.realestateconsulting.model.entity.ReportCategory;
import com.graduation.realestateconsulting.model.enums.ReportReason;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.A;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportResponse {
    private Long id;
    private UserResponse reporterUser;
    private UserResponse reportedUser;
    private ReportCategoryResponse category;
    private String description;
    private LocalDateTime createdAt;
}
