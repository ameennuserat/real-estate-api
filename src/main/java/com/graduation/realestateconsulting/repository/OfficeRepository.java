package com.graduation.realestateconsulting.repository;

import com.graduation.realestateconsulting.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import com.graduation.realestateconsulting.model.entity.Office;

import java.util.List;
import java.util.Optional;

public interface OfficeRepository extends JpaRepository<Office, Long> {

    Optional<Office> findByUserId(Long userId);
}