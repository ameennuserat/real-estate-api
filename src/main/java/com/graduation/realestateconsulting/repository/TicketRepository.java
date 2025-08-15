package com.graduation.realestateconsulting.repository;

import com.graduation.realestateconsulting.model.entity.Ticket;
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

public interface TicketRepository extends JpaRepository<Ticket, Long> , JpaSpecificationExecutor<Ticket> {

    Page<Ticket> findAllByClientId(Pageable pageable, Long clientId);

    @Query("SELECT t FROM Ticket t WHERE " +
           "((:lowPrice IS NULL And :highPrice IS NULL) OR (t.lowPrice >= :lowPrice) AND (t.highPrice <= :highPrice))" +
           "AND ((:lowArea IS NULL And :highArea IS NULL) OR (t.area >= :lowArea) AND (t.area <= :highArea))" +
           "AND (:serviceType IS Null OR t.serviceType = :serviceType)" +
           "AND (:houseType IS Null OR t.houseType = :houseType)" +
           "AND (:location IS Null OR t.location = :location)"
    )
    List<Ticket> findByFilters(@Param("lowPrice") Double lowPrice,
                               @Param("highPrice") Double highPrice,
                               @Param("serviceType") ServiceType serviceType,
                               @Param("houseType") HouseType houseType,
                               @Param("lowArea") String lowArea,
                               @Param("highArea") String highArea,
                               @Param("location") String location
    );

    @Query("SELECT COUNT(u) FROM Ticket u WHERE u.createdAt BETWEEN :startDate AND :endDate")
    long countBetweenDates(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

}