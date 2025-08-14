package com.graduation.realestateconsulting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.graduation.realestateconsulting.model.entity.PropertyImage;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface PropertyImageRepository extends JpaRepository<PropertyImage, Long> {
    @Query("SELECT COUNT(u) FROM PropertyImage u WHERE u.createdAt BETWEEN :startDate AND :endDate")
    long countBetweenDates(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

}