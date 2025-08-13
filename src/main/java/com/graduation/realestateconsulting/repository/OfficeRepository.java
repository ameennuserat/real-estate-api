package com.graduation.realestateconsulting.repository;

import com.graduation.realestateconsulting.model.entity.Expert;
import com.graduation.realestateconsulting.model.enums.UserStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.graduation.realestateconsulting.model.entity.Office;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OfficeRepository extends JpaRepository<Office, Long>, JpaSpecificationExecutor<Office> {

    Optional<Office> findByUserId(Long userId);

    List<Office> findAllByUserStatus(UserStatus userStatus);

    @Query("SELECT o FROM Office o " +
           "WHERE o.rateCount > 0 " +
           "ORDER BY (o.totalRate / o.rateCount) DESC")
    List<Office> findTop20ByAverageRating(Pageable pageable);
}