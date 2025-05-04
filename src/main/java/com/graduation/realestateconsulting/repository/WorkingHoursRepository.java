package com.graduation.realestateconsulting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.graduation.realestateconsulting.model.entity.WorkingHours;

public interface WorkingHoursRepository extends JpaRepository<WorkingHours, Long> {

}