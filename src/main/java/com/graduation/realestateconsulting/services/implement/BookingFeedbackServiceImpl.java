package com.graduation.realestateconsulting.services.implement;

import com.graduation.realestateconsulting.model.dto.request.FeedbackRequest;
import com.graduation.realestateconsulting.model.dto.request.NotificationRequest;
import com.graduation.realestateconsulting.model.dto.request.SentEmailMessageRequest;
import com.graduation.realestateconsulting.model.dto.response.FeedbackResponse;
import com.graduation.realestateconsulting.model.entity.Booking;
import com.graduation.realestateconsulting.model.entity.BookingFeedback;
import com.graduation.realestateconsulting.model.entity.Client;
import com.graduation.realestateconsulting.model.entity.User;
import com.graduation.realestateconsulting.model.mapper.FeedbackMapper;
import com.graduation.realestateconsulting.observer.events.GmailNotificationEvent;
import com.graduation.realestateconsulting.repository.BookingFeedbackRepository;
import com.graduation.realestateconsulting.repository.BookingRepository;
import com.graduation.realestateconsulting.repository.ClientRepository;
import com.graduation.realestateconsulting.services.BookingFeedbackService;
import com.graduation.realestateconsulting.services.NotificationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingFeedbackServiceImpl implements BookingFeedbackService {
    private final BookingRepository bookingRepository;
    private final ClientRepository clientRepository;
    private final  BookingFeedbackRepository repository;
    private final FeedbackMapper mapper;
    private final NotificationService notificationService;
    private ApplicationEventPublisher publisher;

    @Transactional
    @Override
    public FeedbackResponse save(FeedbackRequest request) {
        Booking booking = bookingRepository.findById(request.getBookingId()).orElseThrow(()->new RuntimeException("Booking not found"));
        Client client = clientRepository.findById(request.getClientId()).orElseThrow(()->new RuntimeException("Client not found"));
        BookingFeedback savedFeedback = repository.save(mapper.toEntity(request,booking,client));
        // 3. Prepare data
        User clientUser = client.getUser();
        User expertUser = booking.getExpert().getUser();

        // --- Notifications for the EXPERT (Push + Email) ---
        // Push Notification
        String expertTitle = "You've received new feedback!";
        String expertMessage = String.format("%s has left feedback. Rating: %d/5.", clientUser.getFirstName(), savedFeedback.getRating());
        notificationService.createAndSendNotification(NotificationRequest.builder().title(expertTitle).message(expertMessage).user(expertUser).build());

        // Email
        String expertEmailSubject = String.format("New Feedback from %s", clientUser.getFirstName());
        String expertEmailBody = String.format("Hello %s,\n\nYou have received new feedback for your session with %s.\n\nRating: %d/5\nComment: \"%s\"", expertUser.getFirstName(), clientUser.getFirstName(), savedFeedback.getRating(), savedFeedback.getReview());
        publisher.publishEvent(new GmailNotificationEvent(this, SentEmailMessageRequest.builder().to(expertUser.getEmail()).subject(expertEmailSubject).body(expertEmailBody).build()));

        // --- Confirmation for the CLIENT (Push ONLY) ---
        String clientTitle = "Feedback Submitted";
        String clientMessage = "Thank you! Your feedback has been successfully submitted.";
        notificationService.createAndSendNotification(NotificationRequest.builder().title(clientTitle).message(clientMessage).user(clientUser).build());
        return mapper.toDto(savedFeedback);
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
