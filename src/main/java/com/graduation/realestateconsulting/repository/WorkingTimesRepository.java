package com.graduation.realestateconsulting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.graduation.realestateconsulting.model.entity.WorkingTimes;

import java.util.List;

public interface WorkingTimesRepository extends JpaRepository<WorkingTimes, Long> {
    List<WorkingTimes> findByExpert_Id(Long id);
}