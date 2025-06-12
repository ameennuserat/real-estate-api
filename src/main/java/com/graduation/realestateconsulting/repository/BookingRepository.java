package com.graduation.realestateconsulting.repository;

import com.graduation.realestateconsulting.model.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import com.graduation.realestateconsulting.model.entity.Booking;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByExpertIdAndStartTimeBeforeAndEndTimeAfterAndBookingStatusNot(Long expertId, LocalDateTime startDate, LocalDateTime endDate, BookingStatus status);
    List<Booking> findAllByExpertIdAndBookingStatus(Long expertId,BookingStatus status);
    @Query("SELECT COUNT(b) FROM Booking b WHERE b.expert.id = :expertId " +
            "AND b.bookingStatus = :status " +
            "AND b.startTime < :requestedSlotEnd " +
            "AND b.endTime > :requestedSlotStart")
    long countConflictingBookings(
            @Param("expertId") Long expertId,
            @Param("requestedSlotStart") LocalDateTime requestedSlotStart,
            @Param("requestedSlotEnd") LocalDateTime requestedSlotEnd,
            @Param("status") BookingStatus status
    );
}