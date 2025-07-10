package com.graduation.realestateconsulting.repository;

import com.graduation.realestateconsulting.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import com.graduation.realestateconsulting.model.entity.Report;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> , JpaSpecificationExecutor<Report> {

    List<Report> findByReportedUser(User reportedUser);
    @Query("SELECT r.reportedUser, COUNT(r) as reportCount " +
            "FROM Report r " +
            "WHERE r.reportedUser.role = 'EXPERT' " +
            "GROUP BY r.reportedUser " +
            "ORDER BY reportCount DESC")
    Page<Object[]> findFrequentlyReportedExperts(Pageable pageable);

    List<Report> findByReportedUserIn(List<User> users);
}
