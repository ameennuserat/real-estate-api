package com.graduation.realestateconsulting.services;


import com.graduation.realestateconsulting.model.dto.request.FaqCategoryRequest;
import com.graduation.realestateconsulting.model.dto.response.FaqCategoryResponse;

import java.util.List;

public interface FaqCategoryService {

    List<FaqCategoryResponse> findAll();

    FaqCategoryResponse findById(Long id);

    FaqCategoryResponse save(FaqCategoryRequest request);

    FaqCategoryResponse update(Long id, FaqCategoryRequest request);

    void delete(Long id);

}