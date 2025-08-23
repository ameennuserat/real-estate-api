package com.graduation.realestateconsulting.repository;

import com.graduation.realestateconsulting.model.entity.Client;
import com.graduation.realestateconsulting.model.entity.Expert;
import com.graduation.realestateconsulting.model.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import com.graduation.realestateconsulting.model.entity.Booking;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByExpertIdAndStartTimeBeforeAndEndTimeAfterAndBookingStatusNot(Long expertId, LocalDateTime startDate, LocalDateTime endDate, BookingStatus status);
    //List<Booking> findAllByBookingStatus(BookingStatus status);
    @Query("SELECT b FROM Booking b WHERE b.bookingStatus = 'CONFIRMED'  AND b.startTime BETWEEN :now AND :oneHourFromNow")
    List<Booking> findUpcomingBookingsForReminder(
            @Param("now") LocalDateTime now,
            @Param("oneHourFromNow") LocalDateTime oneHourFromNow
    );

    @Query("SELECT b FROM Booking b WHERE b.bookingStatus = 'CONFIRMED' AND b.endTime <= :now")
    List<Booking> findFinishedConfirmedBookings(@Param("now") LocalDateTime now);

    List<Booking> findAllByExpertIdAndBookingStatus(Long expertId, BookingStatus status);
    List<Booking> findAllByClientIdAndBookingStatus(Long clientId, BookingStatus status);

    @Query("SELECT COUNT(b) FROM Booking b WHERE b.expert.id = :expertId " +
            "AND b.bookingStatus IN  :status " +
            "AND b.startTime < :requestedSlotEnd " +
            "AND b.endTime > :requestedSlotStart")
    long countConflictingBookings(
            @Param("expertId") Long expertId,
            @Param("requestedSlotStart") LocalDateTime requestedSlotStart,
            @Param("requestedSlotEnd") LocalDateTime requestedSlotEnd,
            @Param("status") List<BookingStatus> status
    );

    @Query("SELECT coalesce(SUM(b.finalPrice),0) FROM Booking b " +
            "WHERE b.bookingStatus = :status " +
            "AND b.scheduled_at BETWEEN :startDate AND :endDate")
    double getAllRevenues(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("status") BookingStatus status
    );

    Long countByClientAndExpertAndBookingStatus(Client client, Expert expert, BookingStatus status);

    @Query("SELECT COUNT(u) FROM Booking u WHERE u.scheduled_at BETWEEN :startDate AND :endDate")
    long countBetweenDates(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT b FROM Booking b WHERE b.endTime < :currentTime AND b.bookingStatus = :status")
    List<Booking> findFinishedBookingsWithStatus(
            @Param("currentTime") Instant currentTime,
            @Param("status") BookingStatus status
    );
}