package com.graduation.realestateconsulting.repository;

import com.graduation.realestateconsulting.model.entity.Rate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RateRepository extends JpaRepository<Rate, Long>{
    List<Rate> findByClientId(Long clientId);
}