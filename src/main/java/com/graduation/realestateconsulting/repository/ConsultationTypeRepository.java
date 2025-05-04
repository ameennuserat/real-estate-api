package com.graduation.realestateconsulting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.graduation.realestateconsulting.model.entity.ConsultationType;

public interface ConsultationTypeRepository extends JpaRepository<ConsultationType, Long> {

}