package com.graduation.realestateconsulting.repository;

import com.graduation.realestateconsulting.model.entity.ReportCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportCategoryRepository extends JpaRepository<ReportCategory, Long> {
}
