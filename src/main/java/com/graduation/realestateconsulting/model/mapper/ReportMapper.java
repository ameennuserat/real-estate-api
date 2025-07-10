package com.graduation.realestateconsulting.model.mapper;

import com.graduation.realestateconsulting.model.dto.request.CreateReportRequest;
import com.graduation.realestateconsulting.model.dto.response.ReportCategoryResponse;
import com.graduation.realestateconsulting.model.dto.response.ReportResponse;
import com.graduation.realestateconsulting.model.entity.Report;
import com.graduation.realestateconsulting.model.entity.ReportCategory;
import com.graduation.realestateconsulting.model.entity.User;
import com.graduation.realestateconsulting.repository.ReportRepository;
import com.graduation.realestateconsulting.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportMapper {
    private final UserMapper userMapper;
    public Report toEntity(CreateReportRequest createReportRequest, ReportCategory category, User reporter, User reported) {
        return Report.builder()
                .reportedUser(reported)
                .reporterUser(reporter)
                .category(category)
                .description(createReportRequest.description())
                .build();
    }

    public ReportResponse toDto(Report report) {
        return ReportResponse.builder()
                .id(report.getId())
                .category(toCategoryResponse(report.getCategory()))
                .description(report.getDescription())
                .reporterUser(userMapper.toDto(report.getReporterUser()))
                .reportedUser(userMapper.toDto(report.getReportedUser()))
                .createdAt(report.getCreatedAt())
                .build();
    }

    public List<ReportResponse> toDtos(List<Report> reports) {
        return reports.stream().map(this::toDto).collect(Collectors.toList());
    }

    public ReportCategoryResponse toCategoryResponse(ReportCategory category) {
        return ReportCategoryResponse.builder()
                .id(category.getId())
                .title(category.getTitle())
                .build();
    }

    public List<ReportCategoryResponse> toCategoryResponses(List<ReportCategory> reportCategories) {
        return reportCategories.stream().map(this::toCategoryResponse).collect(Collectors.toList());
    }
}
