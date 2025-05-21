package com.graduation.realestateconsulting.services;

import com.graduation.realestateconsulting.model.dto.request.ExpertImageRequest;
import com.graduation.realestateconsulting.model.dto.request.ExpertRequest;
import com.graduation.realestateconsulting.model.dto.response.ExpertResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;

public interface ExpertService {

    Page<ExpertResponse> findAll(Pageable pageable);

    ExpertResponse findById(Long id);

    ExpertResponse getMe();

    ExpertResponse updateMe(ExpertRequest request);

    void uploadImage(ExpertImageRequest request) throws IOException;

}