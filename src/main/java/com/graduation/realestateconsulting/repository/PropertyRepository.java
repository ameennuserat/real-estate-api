package com.graduation.realestateconsulting.repository;

import com.graduation.realestateconsulting.model.dto.response.PropertyResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.graduation.realestateconsulting.model.entity.Property;

public interface PropertyRepository extends JpaRepository<Property, Long> {

    Page<Property> findAllByOfficeId(Pageable pageable, Long officeId);
}