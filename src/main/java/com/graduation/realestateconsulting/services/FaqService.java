package com.graduation.realestateconsulting.services;


import com.graduation.realestateconsulting.model.dto.request.FaqRequest;
import com.graduation.realestateconsulting.model.dto.response.FaqResponse;
import com.graduation.realestateconsulting.model.entity.Faq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;


public interface FaqService {

    Page<FaqResponse> findAll(Pageable pageable);

    Page<FaqResponse> findAllByCategoryId(Pageable pageable,Long categoryId);

    FaqResponse findById(Long id);

    FaqResponse save(FaqRequest request);

    FaqResponse update(Long id, FaqRequest request);

    void delete(Long id);


    Page<FaqResponse> filterFaq(Specification<Faq> faqSpecification, Pageable pageable);
}