package com.graduation.realestateconsulting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.graduation.realestateconsulting.model.entity.AvailabilityExceptions;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface AvailabilityExceptionRepository extends JpaRepository<AvailabilityExceptions, Long> {
    List<AvailabilityExceptions> findAllByExpertIdAndEndTimeAfterAndStartTimeBefore(Long expertId, LocalDateTime startDate, LocalDateTime endDate);
}