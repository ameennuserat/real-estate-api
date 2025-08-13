package com.graduation.realestateconsulting.repository;

import com.graduation.realestateconsulting.model.entity.Expert;
import com.graduation.realestateconsulting.model.enums.UserStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ExpertRepository extends JpaRepository<Expert, Long>, JpaSpecificationExecutor<Expert> {
    Optional<Expert> findByUserId(Long userId);

    List<Expert> findAllByUserStatus(UserStatus status);

    @Query("SELECT e FROM Expert e " +
           "WHERE e.rateCount > 0 " +
           "ORDER BY (e.totalRate / e.rateCount) DESC")
    List<Expert> findTop20ByAverageRating(Pageable pageable);
}