package com.graduation.realestateconsulting.controller;

import com.graduation.realestateconsulting.filter.dto.FilterRequestDto;
import com.graduation.realestateconsulting.filter.dto.PageRequestDto;
import com.graduation.realestateconsulting.filter.service.FiltersSpecificationService;
import com.graduation.realestateconsulting.model.dto.request.CreateReportRequest;
import com.graduation.realestateconsulting.model.dto.request.ReportSearchCriteria;
import com.graduation.realestateconsulting.model.dto.response.ExceptionResponse;
import com.graduation.realestateconsulting.model.dto.response.ExpertReportSummaryResponse;
import com.graduation.realestateconsulting.model.dto.response.GlobalResponse;
import com.graduation.realestateconsulting.model.dto.response.ReportResponse;
import com.graduation.realestateconsulting.model.entity.Report;
import com.graduation.realestateconsulting.model.entity.User;
import com.graduation.realestateconsulting.services.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/reports")
@Tag(name = "Reports", description = "Operations related to user reports")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;
    private final FiltersSpecificationService<Report> reportFilter;



    @Operation(
            summary = "Create a new report",
            description = "Allows any authenticated user (CLIENT or EXPERT) to create a new report against another user.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Report created successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = GlobalResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request data (e.g., reporting yourself, user not found)",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionResponse.class)))
            }
    )
    @PostMapping
    public ResponseEntity<?> createReport(
            @Valid @RequestBody CreateReportRequest request,
            @AuthenticationPrincipal User currentUser) {

        GlobalResponse globalResponse = GlobalResponse.builder()
                .status("Success")
                .data(reportService.createReport(request, currentUser))
                .build();
        return new ResponseEntity<>(globalResponse, HttpStatus.CREATED);
    }

//    @Operation(
//            summary = "Get all reports (Admin only)",
//            description = "Returns a list of all reports submitted in the system. Accessible only by ADMIN users.",
//            responses = {
//                    @ApiResponse(responseCode = "200", description = "List of reports retrieved successfully",
//                            content = @Content(mediaType = "application/json",
//                                    schema = @Schema(implementation = GlobalResponse.class))),
//                    @ApiResponse(responseCode = "403", description = "Forbidden, user is not an Admin",
//                            content = @Content(mediaType = "application/json",
//                                    schema = @Schema(implementation = ExceptionResponse.class)))
//            }
//    )
//    @GetMapping("/all")
//    @PreAuthorize("hasAuthority('ADMIN')")
//    public ResponseEntity<?> getAllReports() {
//        GlobalResponse globalResponse = GlobalResponse.builder()
//                .status("Success")
//                .data(reportService.getAllReports())
//                .build();
//        return ResponseEntity.ok(globalResponse);
//    }

    @Operation(
            summary = "Get a report by its ID (Admin only)",
            description = "Returns the details of a single report by its unique ID. Accessible only by ADMIN users.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Report details retrieved successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = GlobalResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Report not found",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionResponse.class)))
            }
    )
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> getReportById(@PathVariable Long id) {
        GlobalResponse globalResponse = GlobalResponse.builder()
                .status("Success")
                .data(reportService.getReportById(id))
                .build();
        return ResponseEntity.ok(globalResponse);
    }

    @GetMapping("/categories")
    public ResponseEntity<?> getReportCategories() {
        GlobalResponse globalResponse = GlobalResponse.builder()
                .status("Success")
                .data(reportService.getReportCategories())
                .build();
        return ResponseEntity.ok(globalResponse);
    }

    @DeleteMapping("/categories/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> deleteReportCategoryById(@PathVariable Long id) {
        reportService.deleteReportCategory(id);
        GlobalResponse response = GlobalResponse.builder()
                .status("Success")
                .data("ReportCategory has been deleted successfully. ")
                .build();
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }


    @Operation(summary = "Warn a user based on a report (Admin only)")
    @PostMapping("/{reportId}/warn")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> warnUserFromReport(@PathVariable Long reportId) {
        reportService.processWarnAction(reportId);
        GlobalResponse response = GlobalResponse.builder()
                .status("Success")
                .data("Warning has been sent successfully and the report is now closed.")
                .build();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Block a user for 15 days based on a report (Admin only)")
    @PostMapping("/{reportId}/block")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> blockUserFromReport(@PathVariable Long reportId) {
        reportService.processBlockAction(reportId);
        GlobalResponse response = GlobalResponse.builder()
                .status("Success")
                .data("User has been blocked for 15 days and the report is now closed.")
                .build();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete a user based on a report (Admin only)")
    @DeleteMapping("/{reportId}/delete-user")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> deleteUserFromReport(@PathVariable Long reportId) {
        reportService.processDeleteAction(reportId);
        GlobalResponse response = GlobalResponse.builder()
                .status("Success")
                .data("ReportCategory has been deleted successfully. ")
                .build();
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Search and filter reports (Admin only)")
    @GetMapping("/search")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> searchReports(
            @RequestParam(required = false) Long reportedUserId,
            @RequestParam(required = false) Long reporterUserId,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String descriptionKeyword,

            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,

            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        ReportSearchCriteria criteria = new ReportSearchCriteria(
                reportedUserId, reporterUserId, categoryId, descriptionKeyword, startDate, endDate
        );
        Pageable pageable = PageRequest.of(page, size);

        Page<ReportResponse> reports = reportService.searchReports(criteria, pageable);

        GlobalResponse globalResponse = GlobalResponse.builder()
                .status("Success")
                .data(reports)
                .build();
        return ResponseEntity.ok(globalResponse);
    }


    @PostMapping("/filter")
    public  ResponseEntity<?> filterReport(@RequestBody FilterRequestDto request) {
        Specification<Report> reportSpecification =  reportFilter.getSearchSpecification(request.getFilterItems(),request.getGlobalOperator());
        Pageable pageable = new PageRequestDto().getPageable(request.getPageRequest());
        GlobalResponse response = GlobalResponse.builder()
                .status("Success")
                .data(reportService.filterReport(reportSpecification,pageable))
                .build();
        return ResponseEntity.ok(response);
    }


    @Operation(
            summary = "Get frequently reported experts (Admin only)",
            description = "Returns a paginated list of experts, sorted by the number of reports against them in descending order."
    )
    @GetMapping("/frequently-reported")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> getFrequentlyReportedExperts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size 
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ExpertReportSummaryResponse> result = reportService.getFrequentlyReportedExperts(pageable);

        GlobalResponse globalResponse = GlobalResponse.builder()
                .status("Success")
                .data(result)
                .build();

        return ResponseEntity.ok(globalResponse);
    }
}