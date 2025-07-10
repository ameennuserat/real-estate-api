package com.graduation.realestateconsulting.model.dto.response;

import java.util.List;

public record ExpertReportSummaryResponse (
        UserResponse expertDetails,
        long totalReports,
        List<ReportResponse> reports
){
}
