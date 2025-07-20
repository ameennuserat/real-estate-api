package com.graduation.realestateconsulting.services;

import com.graduation.realestateconsulting.model.dto.request.BookingRequest;
import com.graduation.realestateconsulting.model.dto.request.CancleRequest;
import com.graduation.realestateconsulting.model.dto.response.BookingResponse;
import com.graduation.realestateconsulting.model.enums.BookingStatus;
import com.stripe.exception.StripeException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BookingService {
    BookingResponse initiateBooking(BookingRequest bookingRequest) throws IllegalAccessException, StripeException;
    BookingResponse getBooking(Long id);
    List<BookingResponse> getAllBookings(BookingStatus status);
    BookingResponse cancleBookingWithRefundMony(CancleRequest request) throws IllegalAccessException, StripeException;
    BookingResponse cancleBookingWithoutRefundMony(CancleRequest request) throws IllegalAccessException;
}