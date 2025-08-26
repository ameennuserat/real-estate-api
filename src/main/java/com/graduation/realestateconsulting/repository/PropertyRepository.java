package com.graduation.realestateconsulting.repository;

import com.graduation.realestateconsulting.model.entity.Property;
import com.graduation.realestateconsulting.model.enums.HouseType;
import com.graduation.realestateconsulting.model.enums.ServiceType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PropertyRepository extends JpaRepository<Property, Long>, JpaSpecificationExecutor<Property> {

    Page<Property> findAllByOfficeId(Pageable pageable, Long officeId);

    @Query("SELECT p FROM Property p WHERE " +
           "(:name IS Null OR p.serviceType = :name)" +
           "AND ((:lowPrice IS NULL And :highPrice IS NULL) OR (p.price >= :lowPrice) AND (p.price <= :highPrice))" +
           "AND ((:lowArea IS NULL And :highArea IS NULL) OR (p.area >= :lowArea) AND (p.area <= :highArea))" +
           "AND (:serviceType IS Null OR p.serviceType = :serviceType)" +
           "AND (:houseType IS Null OR p.houseType = :houseType)" +
           "AND (:location IS Null OR p.location = :location)"
    )
    List<Property> findByFilters(@Param("name") String name,
                                 @Param("lowPrice") Double lowPrice,
                               @Param("highPrice") Double highPrice,
                               @Param("serviceType") ServiceType serviceType,
                               @Param("houseType") HouseType houseType,
                               @Param("lowArea") String lowArea,
                               @Param("highArea") String highArea,
                               @Param("location") String location
    );

    List<Property> findTop20ByOrderByViewsCountDesc();

    @Query("SELECT COUNT(u) FROM Property u WHERE u.createdAt BETWEEN :startDate AND :endDate")
    long countBetweenDates(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

}