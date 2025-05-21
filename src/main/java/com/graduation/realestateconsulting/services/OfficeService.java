package com.graduation.realestateconsulting.services;

import com.graduation.realestateconsulting.model.dto.request.OfficeImageRequest;
import com.graduation.realestateconsulting.model.dto.request.OfficeRequest;
import com.graduation.realestateconsulting.model.dto.response.OfficeResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;

public interface OfficeService {

    Page<OfficeResponse> findAll(Pageable pageable);

    OfficeResponse findById(Long id);

    OfficeResponse getMe();

    OfficeResponse updateMe(OfficeRequest request);

    void uploadImage(OfficeImageRequest request) throws IOException;
}