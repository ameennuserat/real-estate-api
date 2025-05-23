package com.graduation.realestateconsulting.services;

import com.graduation.realestateconsulting.model.dto.request.WorkingTimesRequest;
import com.graduation.realestateconsulting.model.dto.response.GlobalResponse;
import com.graduation.realestateconsulting.model.dto.response.WorkingTimesResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface WorkingTimesService {
    List<WorkingTimesResponse> createWorkingTimes(Long id,List<WorkingTimesRequest> workingTimesRequest);
    WorkingTimesResponse getWorkingTimes(Long id);
    List<WorkingTimesResponse> getWorkingTimes();
    WorkingTimesResponse updateWorkingTime(Long id, WorkingTimesRequest workingTimesRequest);
    void deleteWorkingTime(Long id);
}