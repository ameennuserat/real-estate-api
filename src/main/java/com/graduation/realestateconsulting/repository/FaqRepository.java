package com.graduation.realestateconsulting.repository;

import com.graduation.realestateconsulting.model.entity.Faq;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FaqRepository extends JpaRepository<Faq, Long> {

    List<Faq> findAllByFaqCategoryId(Long categoryId);
}