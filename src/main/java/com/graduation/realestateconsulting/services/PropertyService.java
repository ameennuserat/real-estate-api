package com.graduation.realestateconsulting.services;

import com.graduation.realestateconsulting.model.dto.request.PropertyRequest;
import com.graduation.realestateconsulting.model.dto.response.PropertyResponse;
import com.graduation.realestateconsulting.model.entity.Property;
import com.graduation.realestateconsulting.model.enums.HouseType;
import com.graduation.realestateconsulting.model.enums.ServiceType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.io.IOException;
import java.util.List;

public interface PropertyService {

    Page<PropertyResponse> findAll(Pageable pageable);
    List<PropertyResponse> findByFilters(String name, Double lowPrice, Double highPrice, ServiceType serviceType, HouseType houseType, String lowArea, String highArea, String location);
    Page<PropertyResponse> findAllByOfficeId(Pageable pageable, Long officeId);
    PropertyResponse findById(Long id);
    PropertyResponse save(PropertyRequest request);
    PropertyResponse update(Long id,PropertyRequest request);
    void delete(Long id) throws IOException;

    Page<PropertyResponse> filterProperty(Specification<Property> propertySpecification, Pageable pageable);
}