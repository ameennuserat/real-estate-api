package com.graduation.realestateconsulting.repository;

import com.graduation.realestateconsulting.model.entity.Expert;
import com.graduation.realestateconsulting.model.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface ExpertRepository extends JpaRepository<Expert, Long>, JpaSpecificationExecutor<Expert> {
    Optional<Expert> findByUserId(Long userId);

    List<Expert> findAllByUserStatus(UserStatus status);
}