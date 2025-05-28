package com.graduation.realestateconsulting.services;

import com.graduation.realestateconsulting.model.dto.request.OfficeImageRequest;
import com.graduation.realestateconsulting.model.dto.request.OfficeRequest;
import com.graduation.realestateconsulting.model.dto.response.ExpertResponse;
import com.graduation.realestateconsulting.model.dto.response.OfficeResponse;
import com.graduation.realestateconsulting.model.enums.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

public interface OfficeService {

    Page<OfficeResponse> findAll(Pageable pageable);

    List<OfficeResponse> findAllByUserStatus(UserStatus status);

    OfficeResponse findById(Long id);

    OfficeResponse getMe();

    OfficeResponse updateMe(OfficeRequest request);

    void uploadImage(OfficeImageRequest request) throws IOException;
}