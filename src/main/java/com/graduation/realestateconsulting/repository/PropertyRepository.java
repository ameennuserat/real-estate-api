package com.graduation.realestateconsulting.repository;

import com.graduation.realestateconsulting.model.dto.response.PropertyResponse;
import com.graduation.realestateconsulting.model.entity.Ticket;
import com.graduation.realestateconsulting.model.enums.HouseType;
import com.graduation.realestateconsulting.model.enums.ServiceType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.graduation.realestateconsulting.model.entity.Property;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PropertyRepository extends JpaRepository<Property, Long> {

    Page<Property> findAllByOfficeId(Pageable pageable, Long officeId);

    @Query("SELECT p FROM Property p WHERE " +
           "((:lowPrice IS NULL And :highPrice IS NULL) OR (p.price >= :lowPrice) AND (p.price <= :highPrice))" +
           "AND ((:lowArea IS NULL And :highArea IS NULL) OR (p.area >= :lowArea) AND (p.area <= :highArea))" +
           "AND (:serviceType IS Null OR p.serviceType = :serviceType)" +
           "AND (:houseType IS Null OR p.houseType = :houseType)" +
           "AND (:location IS Null OR p.location = :location)"
    )
    List<Property> findByFilters(@Param("lowPrice") Double lowPrice,
                               @Param("highPrice") Double highPrice,
                               @Param("serviceType") ServiceType serviceType,
                               @Param("houseType") HouseType houseType,
                               @Param("lowArea") String lowArea,
                               @Param("highArea") String highArea,
                               @Param("location") String location
    );
}