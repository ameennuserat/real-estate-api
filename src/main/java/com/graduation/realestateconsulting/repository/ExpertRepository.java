package com.graduation.realestateconsulting.repository;

import com.graduation.realestateconsulting.model.entity.Expert;
import com.graduation.realestateconsulting.model.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExpertRepository extends JpaRepository<Expert, Long> {
    Optional<Expert> findByUserId(Long userId);

    List<Expert> findAllByUserStatus(UserStatus status);
}