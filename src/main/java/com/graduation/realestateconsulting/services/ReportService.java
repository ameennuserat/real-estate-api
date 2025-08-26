package com.graduation.realestateconsulting.services;

import com.graduation.realestateconsulting.model.dto.request.CreateReportRequest;
import com.graduation.realestateconsulting.model.dto.request.ReportSearchCriteria;
import com.graduation.realestateconsulting.model.dto.response.ExpertReportSummaryResponse;
import com.graduation.realestateconsulting.model.dto.response.ReportCategoryResponse;
import com.graduation.realestateconsulting.model.dto.response.ReportResponse;
import com.graduation.realestateconsulting.model.entity.Report;
import com.graduation.realestateconsulting.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ReportService {

    ReportResponse createReport(CreateReportRequest request, User reporter);

    List<ReportResponse> getAllReports();

    ReportResponse getReportById(Long id);

    List<ReportCategoryResponse> getReportCategories();

    void deleteReportCategory(Long id);

    void processBlockAction(Long reportId);

    void processWarnAction(Long reportId);

    void processDeleteAction(Long reportId);

    Page<ReportResponse> searchReports(ReportSearchCriteria criteria, Pageable pageable);

    Page<ExpertReportSummaryResponse> getFrequentlyReportedExperts(Pageable pageable);

    Page<ReportResponse> filterReport(Specification<Report> reportSpecification, Pageable pageable);
}
