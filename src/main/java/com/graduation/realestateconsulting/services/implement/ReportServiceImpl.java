package com.graduation.realestateconsulting.services.implement;

import com.graduation.realestateconsulting.model.dto.request.CreateReportRequest;
import com.graduation.realestateconsulting.model.dto.request.ReportSearchCriteria;
import com.graduation.realestateconsulting.model.dto.response.ExpertReportSummaryResponse;
import com.graduation.realestateconsulting.model.dto.response.ReportCategoryResponse;
import com.graduation.realestateconsulting.model.dto.response.ReportResponse;
import com.graduation.realestateconsulting.model.entity.Report;
import com.graduation.realestateconsulting.model.entity.ReportCategory;
import com.graduation.realestateconsulting.model.entity.User;
import com.graduation.realestateconsulting.model.mapper.ReportMapper;
import com.graduation.realestateconsulting.model.mapper.UserMapper;
import com.graduation.realestateconsulting.repository.ReportCategoryRepository;
import com.graduation.realestateconsulting.repository.ReportRepository;
import com.graduation.realestateconsulting.repository.UserRepository;
import com.graduation.realestateconsulting.services.ReportService;
import com.graduation.realestateconsulting.services.UserManagementService;
import com.graduation.realestateconsulting.trait.ReportSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final ReportCategoryRepository reportCategoryRepository;
    private final UserRepository userRepository;
    private final ReportMapper reportMapper;
    private final UserManagementService userManagementService;
    private final UserMapper userMapper;

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


    @Override
    @Transactional(readOnly = true)
    public Page<ExpertReportSummaryResponse> getFrequentlyReportedExperts(Pageable pageable) {
        
        Page<Object[]> expertCountsPage = reportRepository.findFrequentlyReportedExperts(pageable);


        List<User> expertsOnPage = expertCountsPage.getContent().stream()
                .map(result -> (User) result[0])
                .collect(Collectors.toList());

        List<Report> reportsForExpertsOnPage = reportRepository.findByReportedUserIn(expertsOnPage);


        Map<Long, List<ReportResponse>> reportsByExpertId = reportsForExpertsOnPage.stream()
                .collect(Collectors.groupingBy(
                        report -> report.getReportedUser().getId(),
                        Collectors.mapping(reportMapper::toDto, Collectors.toList())
                ));


        return expertCountsPage.map(result -> {
            User expert = (User) result[0];
            long reportCount = (Long) result[1];


            List<ReportResponse> expertReports = reportsByExpertId.getOrDefault(expert.getId(), List.of());

            return new ExpertReportSummaryResponse(
                    userMapper.toDto(expert),
                    reportCount,
                    expertReports
            );
        });
    }

    @Override
    public Page<ReportResponse> filterReport(Specification<Report> reportSpecification, Pageable pageable) {
        return reportRepository.findAll(reportSpecification,pageable).map(reportMapper::toDto);
    }
}