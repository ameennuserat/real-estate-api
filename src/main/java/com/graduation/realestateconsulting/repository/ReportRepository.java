package com.graduation.realestateconsulting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.graduation.realestateconsulting.model.entity.Report;

public interface ReportRepository extends JpaRepository<Report, Long> {

}