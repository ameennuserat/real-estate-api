package com.graduation.realestateconsulting.services;

import com.graduation.realestateconsulting.model.dto.request.AvailabilityExceptionRequest;
import com.graduation.realestateconsulting.model.dto.request.UpdateAvailabilityExceptionRequest;
import com.graduation.realestateconsulting.model.dto.response.AvailabilityExceptionResponse;
import com.graduation.realestateconsulting.model.entity.AvailabilityExceptions;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AvailabilityExceptionService {
    AvailabilityExceptionResponse create(AvailabilityExceptionRequest availabilityExceptions);
    AvailabilityExceptionResponse update(UpdateAvailabilityExceptionRequest availabilityExceptions,Long id);
    List<AvailabilityExceptionResponse> getAll();
    void delete(Long id);
}