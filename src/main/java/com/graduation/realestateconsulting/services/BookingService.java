package com.graduation.realestateconsulting.services;

import com.graduation.realestateconsulting.model.dto.request.BookingRequest;
import com.graduation.realestateconsulting.model.dto.request.CancleRequest;
import com.graduation.realestateconsulting.model.dto.response.BookingResponse;
import com.graduation.realestateconsulting.model.enums.BookingStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BookingService {
    BookingResponse book(BookingRequest bookingRequest) throws IllegalAccessException;
    BookingResponse getBooking(Long id);
    List<BookingResponse> getAllBookings(BookingStatus status);
    BookingResponse cancleBookingWithRefundMony(CancleRequest request) throws IllegalAccessException;
    BookingResponse cancleBookingWithoutRefundMony(CancleRequest request) throws IllegalAccessException;
}