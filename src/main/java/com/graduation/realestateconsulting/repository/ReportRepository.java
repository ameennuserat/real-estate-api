package com.graduation.realestateconsulting.repository;

import com.graduation.realestateconsulting.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import com.graduation.realestateconsulting.model.entity.Report;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> , JpaSpecificationExecutor<Report> {

    List<Report> findByReportedUser(User reportedUser);
}
