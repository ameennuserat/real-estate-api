package com.graduation.realestateconsulting.services.implement;

import com.graduation.realestateconsulting.model.dto.request.CreateReportRequest;
import com.graduation.realestateconsulting.model.dto.request.ReportSearchCriteria;
import com.graduation.realestateconsulting.model.dto.response.ReportCategoryResponse;
import com.graduation.realestateconsulting.model.dto.response.ReportResponse;
import com.graduation.realestateconsulting.model.entity.Report;
import com.graduation.realestateconsulting.model.entity.ReportCategory;
import com.graduation.realestateconsulting.model.entity.User;
import com.graduation.realestateconsulting.model.mapper.ReportMapper;
import com.graduation.realestateconsulting.repository.ReportCategoryRepository;
import com.graduation.realestateconsulting.repository.ReportRepository;
import com.graduation.realestateconsulting.repository.UserRepository;
import com.graduation.realestateconsulting.services.UserManagementService;
import com.graduation.realestateconsulting.trait.ReportSpecification;
import jakarta.xml.bind.SchemaOutputResolver;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.graduation.realestateconsulting.services.ReportService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final ReportCategoryRepository reportCategoryRepository;
    private final UserRepository userRepository;
    private final ReportMapper reportMapper;
    private final UserManagementService userManagementService;

    @Override
    @Transactional
    public ReportResponse createReport(CreateReportRequest request, User reporter) {

        User reportedUser = userRepository.findById(request.reportedUserId())
                .orElseThrow(() -> new IllegalArgumentException("User to be reported not found."));

        if (reporter.getId().equals(reportedUser.getId())) {
            throw new IllegalArgumentException("You cannot report yourself.");
        }
        ReportCategory category = reportCategoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid report category ID."));

        return reportMapper.toDto(reportRepository.save(reportMapper.toEntity(request, category, reporter, reportedUser)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReportResponse> getAllReports() {
        List<Report> reports = reportRepository.findAll();
        return reportMapper.toDtos(reports);
    }

    @Override
    @Transactional(readOnly = true)
    public ReportResponse getReportById(Long id) {
        Report report = reportRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Report not found."));
        return reportMapper.toDto(report);
    }

    @Override
    public List<ReportCategoryResponse> getReportCategories() {
        List<ReportCategory> reportCategories = reportCategoryRepository.findAll();
        return reportMapper.toCategoryResponses(reportCategories);
    }

    @Override
    public void deleteReportCategory(Long id) {
        ReportCategory reportCategory = reportCategoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Report category not found."));
        reportCategoryRepository.delete(reportCategory);
    }

    @Override
    @Transactional
    public void processBlockAction(Long reportId) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new IllegalArgumentException("Report not found."));

        User reportedUser = report.getReportedUser();

        userManagementService.blockUser(report.getReportedUser().getId(), 15);

        List<Report> allReportsForThisUser = reportRepository.findByReportedUser(reportedUser);

        if (!allReportsForThisUser.isEmpty()) {
            reportRepository.deleteAll(allReportsForThisUser);
        }
    }


    @Override
    @Transactional
    public void processWarnAction(Long reportId) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new IllegalArgumentException("Report not found."));
        User reportedUser = report.getReportedUser();
        System.out.println("////////////////////////////////////////////////////////////////////////////////////");
        System.out.println(report.getReportedUser().getEmail());
        System.out.println("////////////////////////////////////////////////////////////////////////////////////");

        userManagementService.warnUser(report.getReportedUser().getId());

        List<Report> allReportsForThisUser = reportRepository.findByReportedUser(reportedUser);
        if (!allReportsForThisUser.isEmpty()) {
            reportRepository.deleteAll(allReportsForThisUser);
        }
    }


    @Override
    @Transactional
    public void processDeleteAction(Long reportId) {
        Report report = reportRepository.findById(reportId).orElseThrow(() -> new IllegalArgumentException("Report not found."));
        User reportedUser = report.getReportedUser();
        List<Report> allReportsForThisUser = reportRepository.findByReportedUser(reportedUser);
        if (!allReportsForThisUser.isEmpty()) {
            reportRepository.deleteAll(allReportsForThisUser);
        }
        userRepository.delete(reportedUser);
    }


    @Override
    @Transactional(readOnly = true)
    public Page<ReportResponse> searchReports(ReportSearchCriteria criteria, Pageable pageable) {
        Specification<Report> spec = ReportSpecification.findByCriteria(criteria);

        Page<Report> reportPage = reportRepository.findAll(spec, pageable);

        return reportPage.map(reportMapper::toDto);
    }
}