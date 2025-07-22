package com.graduation.realestateconsulting.services;


import com.graduation.realestateconsulting.model.dto.request.FaqRequest;
import com.graduation.realestateconsulting.model.dto.response.FaqResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface FaqService {

    Page<FaqResponse> findAll(Pageable pageable);

    Page<FaqResponse> findAllByCategoryId(Pageable pageable,Long categoryId);

    FaqResponse findById(Long id);

    FaqResponse save(FaqRequest request);

    FaqResponse update(Long id, FaqRequest request);

    void delete(Long id);


}