package com.graduation.realestateconsulting.services;


import com.graduation.realestateconsulting.model.dto.request.FaqRequest;
import com.graduation.realestateconsulting.model.dto.response.FaqResponse;

import java.util.List;

public interface FaqService {

    List<FaqResponse> findAll();

    List<FaqResponse> findAllByCategoryId(Long categoryId);

    FaqResponse findById(Long id);

    FaqResponse save(FaqRequest request);

    FaqResponse update(Long id, FaqRequest request);

    void delete(Long id);


}