package com.graduation.realestateconsulting.jobs;

import com.graduation.realestateconsulting.model.dto.request.NotificationRequest;
import com.graduation.realestateconsulting.model.dto.request.SentEmailMessageRequest;
import com.graduation.realestateconsulting.model.entity.Booking;
import com.graduation.realestateconsulting.model.entity.Client;
import com.graduation.realestateconsulting.model.entity.Expert;
import com.graduation.realestateconsulting.model.entity.SessionActivityLog;
import com.graduation.realestateconsulting.model.enums.BookingStatus;
import com.graduation.realestateconsulting.model.enums.EventType;
import com.graduation.realestateconsulting.model.enums.Role;
import com.graduation.realestateconsulting.observer.events.GmailNotificationEvent;
import com.graduation.realestateconsulting.repository.BookingRepository;
import com.graduation.realestateconsulting.repository.SessionActivityLogRepository;
import com.graduation.realestateconsulting.services.NotificationService;
import com.graduation.realestateconsulting.services.PaymentService;
import com.graduation.realestateconsulting.services.StripeTransferService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingScheduledTasks {

    private final BookingRepository bookingRepository;
    private final NotificationService notificationService;
    private final ApplicationEventPublisher publisher;
    private final SessionActivityLogRepository activityLogRepository;
    private final StripeTransferService stripeService;
    private final PaymentService paymentService;
    private final Set<Long> sentBookingIds = new HashSet<>();
    private static final double SUCCESS_THRESHOLD = 0.85;
//    @Scheduled(cron = "0 * * * * *")
//    public void alertBookingTime() {
//        LocalDateTime oneHourFromNow = LocalDateTime.now().plusHours(1);
//        List<Booking> bookings = bookingRepository.findUpcomingBookingsForReminder(LocalDateTime.now(), oneHourFromNow);
//        for (Booking booking : bookings) {
//            if (sentBookingIds.contains(booking.getId())) {
//                continue;
//            }
//            LocalDateTime pre1Hour = booking.getStartTime().minusHours(1);
//            if (!LocalDateTime.now().isBefore(pre1Hour) && LocalDateTime.now().isBefore(booking.getStartTime())){
//                Client client = booking.getClient();
//                Expert expert = booking.getExpert();
//
//                long minutesLeft = Duration.between(LocalDateTime.now(), booking.getStartTime()).toMinutes();
//                //// Send Real Time Notification To Expert And Client
//                String titleForFcm = "Upcoming Session Reminder";
//                String messageForClientFcm = String.format(
//                        "Only %d minutes left until your session with expert %s starts",
//                        minutesLeft,
//                        expert.getUser().getFirstName() + " " + expert.getUser().getLastName()
//                );
//
//                String messageForExpertFcm = String.format(
//                        "Only %d minutes left until your session with client %s starts",
//                        minutesLeft,
//                        client.getUser().getFirstName() + " " + client.getUser().getLastName()
//                );
//                // to expert
//                notificationService.createAndSendNotification(NotificationRequest.builder()
//                        .title(titleForFcm)
//                        .message(messageForExpertFcm)
//                        .user(expert.getUser())
//                        .build());
//                // to client
//                notificationService.createAndSendNotification(NotificationRequest.builder()
//                        .title(titleForFcm)
//                        .message(messageForClientFcm)
//                        .user(client.getUser())
//                        .build());
//
//
//                //// Send Gmail Notification To Expert And Client
//                String bodyForClient = String.format(
//                        "Hello %s,\n\nOnly %d minutes left until your session with expert %s starts.\nPlease be ready on time.\n\nBest regards,\nYour Real Estate Team",
//                        client.getUser().getFirstName(),
//                        minutesLeft,
//                        expert.getUser().getFirstName() + " " + expert.getUser().getLastName()
//                );
//
//                String bodyForExpert = String.format(
//                        "Hello %s,\n\nOnly %d minutes left until your session with client %s starts.\nPlease be ready on time.\n\nBest regards,\nYour Real Estate Team",
//                        expert.getUser().getFirstName(),
//                        minutesLeft,
//                        client.getUser().getFirstName() + " " + client.getUser().getLastName()
//                );
//
//                // to expert
//                publisher.publishEvent(
//                        new GmailNotificationEvent(this ,
//                                SentEmailMessageRequest.builder()
//                                        .subject(titleForFcm)
//                                        .body(bodyForExpert)
//                                        .to(expert.getUser().getEmail())
//                                        .build()
//                        )
//                );
//
//                // to client
//                publisher.publishEvent(
//                        new GmailNotificationEvent(this ,
//                                SentEmailMessageRequest.builder()
//                                        .subject(titleForFcm)
//                                        .body(bodyForClient)
//                                        .to(client.getUser().getEmail())
//                                        .build()
//                        )
//                );
//                sentBookingIds.add(booking.getId());
//            }
//        }
//    }

//    @Scheduled(cron = "0 0 * * * *")
//    @Transactional
//    public void updateStatusForCompletedBookings() {
//
//        List<Booking> finishedBookings = bookingRepository.findFinishedConfirmedBookings(LocalDateTime.now());
//
//        if (finishedBookings.isEmpty()) {
//            return;
//        }
//
//        for (Booking booking : finishedBookings) {
//            booking.setBookingStatus(BookingStatus.COMPLETED);
//        }
//        bookingRepository.saveAll(finishedBookings);
//    }


    @Scheduled(fixedRate = 300000)
    @Transactional
    public void processFinishedBookings() {
        log.info("Task Start: Looking for finished bookings to process...");

        List<Booking> bookingsToProcess = bookingRepository.findFinishedBookingsWithStatus(
                Instant.now(),
                BookingStatus.CONFIRMED
        );

        if (bookingsToProcess.isEmpty()) {
            log.info("Task End: No finished bookings found to process.");
            return;
        }

        for (Booking booking : bookingsToProcess) {
            log.info("Processing booking ID: {}", booking.getId());
            try {
                List<SessionActivityLog> expertEvents = activityLogRepository.findByBookingIdAndRoleInSessionOrderByEventTimestampAsc(
                        booking.getId(),
                        Role.EXPERT
                );


                long totalPresenceSeconds = calculateTotalPresence(expertEvents);

                long scheduledDurationSeconds = Duration.between(booking.getStartTime(), booking.getEndTime()).getSeconds();

                if (scheduledDurationSeconds <= 0) {
                    log.warn("Booking {} has an invalid duration. Skipping.", booking.getId());
                    continue;
                }

                double presenceRatio = (double) totalPresenceSeconds / scheduledDurationSeconds;

                if (presenceRatio >= SUCCESS_THRESHOLD) {
                    log.info("Booking {} SUCCEEDED. Presence ratio: {}. Initiating transfer.", booking.getId(), presenceRatio);


                    stripeService.transferToExpert(booking);
                    booking.setBookingStatus(BookingStatus.COMPLETED);
                } else {
                    log.warn("Booking {} FAILED. Presence ratio: {}. Initiating refund.", booking.getId(), presenceRatio);

                    paymentService.createRefund(booking.getPaymentIntentId());
                    booking.setBookingStatus(BookingStatus.CANCELED);
                    booking.setCancellationReason("Expert did not meet minimum presence time.");
                }
                bookingRepository.save(booking);

            } catch (Exception e) {
                log.error("Failed to process booking completion for ID: {}", booking.getId(), e);
            }
        }
        log.info("Task End: Finished processing {} bookings.", bookingsToProcess.size());
    }

    private long calculateTotalPresence(List<SessionActivityLog> events) {
        long totalSeconds = 0;
        Instant joinTime = null;

        for (SessionActivityLog event : events) {
            if (event.getEventType() == EventType.JOIN) {
                if (joinTime == null) {
                    joinTime = event.getEventTimestamp();
                }
            } else if (event.getEventType() == EventType.LEAVE) {
                if (joinTime != null) {
                    totalSeconds += Duration.between(joinTime, event.getEventTimestamp()).getSeconds();
                    joinTime = null;
                }
            }
        }

        return totalSeconds;
    }
}

