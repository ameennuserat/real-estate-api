package com.graduation.realestateconsulting.services;

import com.graduation.realestateconsulting.model.dto.request.PropertyRequest;
import com.graduation.realestateconsulting.model.dto.response.PropertyResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;

public interface PropertyService {

    Page<PropertyResponse> findAll(Pageable pageable);
    Page<PropertyResponse> findAllByOfficeId(Pageable pageable, Long officeId);
    PropertyResponse findById(Long id);
    PropertyResponse save(PropertyRequest request);
    PropertyResponse update(Long id,PropertyRequest request);
    void delete(Long id) throws IOException;

}