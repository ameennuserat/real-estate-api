package com.graduation.realestateconsulting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.graduation.realestateconsulting.model.entity.Consultation;

public interface ConsultationRepository extends JpaRepository<Consultation, Long> {

}