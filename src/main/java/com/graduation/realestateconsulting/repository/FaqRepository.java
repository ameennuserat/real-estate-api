package com.graduation.realestateconsulting.repository;

import com.graduation.realestateconsulting.model.entity.Faq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface FaqRepository extends JpaRepository<Faq, Long>, JpaSpecificationExecutor<Faq> {

    Page<Faq> findAllByFaqCategoryId(Pageable pageable, Long categoryId);
}