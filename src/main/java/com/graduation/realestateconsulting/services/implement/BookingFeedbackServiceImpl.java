package com.graduation.realestateconsulting.services.implement;

import com.graduation.realestateconsulting.model.dto.request.FeedbackRequest;
import com.graduation.realestateconsulting.model.dto.response.FeedbackResponse;
import com.graduation.realestateconsulting.model.entity.Booking;
import com.graduation.realestateconsulting.model.entity.BookingFeedback;
import com.graduation.realestateconsulting.model.entity.Client;
import com.graduation.realestateconsulting.model.mapper.FeedbackMapper;
import com.graduation.realestateconsulting.repository.BookingFeedbackRepository;
import com.graduation.realestateconsulting.repository.BookingRepository;
import com.graduation.realestateconsulting.repository.ClientRepository;
import com.graduation.realestateconsulting.services.BookingFeedbackService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingFeedbackServiceImpl implements BookingFeedbackService {
    private final BookingRepository bookingRepository;
    private final ClientRepository clientRepository;
    private final  BookingFeedbackRepository repository;
    private final FeedbackMapper mapper;

    @Transactional
    @Override
    public FeedbackResponse save(FeedbackRequest request) {
        Booking booking = bookingRepository.findById(request.getBookingId()).orElseThrow(()->new RuntimeException("Booking not found"));
        Client client = clientRepository.findById(request.getClientId()).orElseThrow(()->new RuntimeException("Client not found"));
        System.out.printf("Booking: %s\n", booking);
        return mapper.toDto(repository.save(mapper.toEntity(request,booking,client)));
    }

    @Override
    public List<FeedbackResponse> getAll() {
        List<BookingFeedback> bookingFeedbacks = repository.findAll();
        return mapper.toDtos(bookingFeedbacks);
    }

    @Override
    public FeedbackResponse getByBookingId(long bookingId) {
        BookingFeedback bookingFeedbacks = repository.findByBookingId(bookingId).orElseThrow(()->new RuntimeException("Booking dont has feedback"));
        return mapper.toDto(bookingFeedbacks);
    }

    @Override
    public FeedbackResponse getById(Long id) {
        BookingFeedback bookingFeedback = repository.findById(id).orElseThrow(()->new RuntimeException("Feedback not found"));
        return mapper.toDto(bookingFeedback);
    }
}
