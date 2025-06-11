package com.graduation.realestateconsulting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.graduation.realestateconsulting.model.entity.WorkingTimes;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

public interface WorkingTimesRepository extends JpaRepository<WorkingTimes, Long> {
    List<WorkingTimes> findByExpert_Id(Long id);
     Optional<WorkingTimes> findByExpertIdAndDayOfWeek(Long expertId, DayOfWeek dayOfWeek);
}