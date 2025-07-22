package com.graduation.realestateconsulting.services;

import com.graduation.realestateconsulting.model.dto.request.ExpertImageRequest;
import com.graduation.realestateconsulting.model.dto.request.ExpertRequest;
import com.graduation.realestateconsulting.model.dto.response.ExpertResponse;
import com.graduation.realestateconsulting.model.entity.Expert;
import com.graduation.realestateconsulting.model.enums.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.io.IOException;
import java.util.List;

public interface ExpertService {

    Page<ExpertResponse> findAll(Pageable pageable);

    List<ExpertResponse> findAllByUserStatus(UserStatus status);

    ExpertResponse findById(Long id);

    ExpertResponse getMe();

    ExpertResponse updateMe(ExpertRequest request);

    void uploadImage(ExpertImageRequest request) throws IOException;

    Page<ExpertResponse> filterExpert(Specification<Expert> expertSpecification, Pageable pageable);

//    void updateExpertFollowerCount(Long id,Integer value);
//
//    void updateExpertFavoriteCount(Long id,Integer value);

}