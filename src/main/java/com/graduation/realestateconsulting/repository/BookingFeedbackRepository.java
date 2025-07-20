package com.graduation.realestateconsulting.repository;

import com.graduation.realestateconsulting.model.dto.request.FeedbackRequest;
import com.graduation.realestateconsulting.model.entity.BookingFeedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookingFeedbackRepository extends JpaRepository<BookingFeedback,Long> {
   Optional<BookingFeedback> findByBookingId(Long id);
}
