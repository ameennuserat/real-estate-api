package com.graduation.realestateconsulting.services.implement;

import com.graduation.realestateconsulting.model.dto.request.NotificationRequest;
import com.graduation.realestateconsulting.model.dto.request.SentEmailMessageRequest;
import com.graduation.realestateconsulting.model.entity.Booking;
import com.graduation.realestateconsulting.model.entity.CouponEntity;
import com.graduation.realestateconsulting.model.entity.User;
import com.graduation.realestateconsulting.model.enums.BookingStatus;
import com.graduation.realestateconsulting.observer.events.GmailNotificationEvent;
import com.graduation.realestateconsulting.repository.BookingRepository;
import com.graduation.realestateconsulting.repository.CouponRepository;
import com.graduation.realestateconsulting.services.NotificationService;
import com.graduation.realestateconsulting.services.WebhookService;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.juli.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class WebhookServiceImpl implements WebhookService {

    private final BookingRepository bookingRepository;
    private final CouponRepository couponRepository;
    private final NotificationService notificationService;
    private final ApplicationEventPublisher publisher;
    private static final Logger logger = LoggerFactory.getLogger(WebhookServiceImpl.class);

    @Override
    @Transactional
    public void handleFinalPaymentStatus(Event event, String eventType) {
        PaymentIntent paymentIntent = (PaymentIntent) event.getDataObjectDeserializer()
                .getObject()
                .orElse(null);

        if (paymentIntent == null) {
            return;
        }

        String bookingIdStr = paymentIntent.getMetadata().get("bookingId");
        if (bookingIdStr == null) {
            return;
        }

        Long bookingId = Long.parseLong(bookingIdStr);
        Booking booking = bookingRepository.findById(bookingId).orElse(null);

        if (booking != null) {

            if (booking.getBookingStatus() == BookingStatus.PENDING) {

                if ("payment_intent.succeeded".equals(eventType)) {
                    booking.setBookingStatus(BookingStatus.CONFIRMED);
                    System.out.println("Booking " + booking.getId() + " has been confirmed!");
                    if (booking.getCoupon() != null) {
                        CouponEntity usedCoupon = booking.getCoupon();
                        usedCoupon.setTimesUsed(usedCoupon.getTimesUsed() + 1);
                        couponRepository.save(usedCoupon);
                        System.out.println("Coupon " + usedCoupon.getCode() + " usage incremented.");
                    }

                    User clientUser = booking.getClient();
                    User expertUser = booking.getExpert().getUser();
                    Integer duration = booking.getDuration();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy 'at' h:mm a", Locale.ENGLISH);
                    String formattedTime = booking.getStartTime().format(formatter);
                    try {
                        // Client
                        String clientPushTitle = "Your booking is confirmed!";
                        String clientPushMessage = String.format("Your %d-minute booking with expert %s for %s has been confirmed.", duration, expertUser.getFirstName(), formattedTime);
                        notificationService.createAndSendNotification(NotificationRequest.builder().title(clientPushTitle).message(clientPushMessage).user(clientUser).build());

                        String clientEmailSubject = String.format("Booking Confirmation: Your appointment with %s", expertUser.getFirstName());
                        String clientEmailBody = String.format("Dear %s,\n\nThis email confirms your booking for a %d-minute session with %s on %s.\n\nThank you for using our platform.", clientUser.getFirstName(), duration, expertUser.getFirstName(), formattedTime);
                        publisher.publishEvent(new GmailNotificationEvent(this, SentEmailMessageRequest.builder().to(clientUser.getEmail()).subject(clientEmailSubject).body(clientEmailBody).build()));

                        //  to Expert
                        String expertPushTitle = "You have a new booking!";
                        String expertPushMessage = String.format("%s has booked a %d-minute appointment with you for %s.", clientUser.getFirstName(), duration, formattedTime);
                        notificationService.createAndSendNotification(NotificationRequest.builder().title(expertPushTitle).message(expertPushMessage).user(expertUser).build());

                        String expertEmailSubject = String.format("New Booking Notification: Appointment with %s", clientUser.getFirstName());
                        String expertEmailBody = String.format("Hello %s,\n\nYou have a new %d-minute booking with %s scheduled for %s.", expertUser.getFirstName(), duration, clientUser.getFirstName(), formattedTime);
                        publisher.publishEvent(new GmailNotificationEvent(this, SentEmailMessageRequest.builder().to(expertUser.getEmail()).subject(expertEmailSubject).body(expertEmailBody).build()));
                    } catch (Exception e) {
                        logger.error(e.getMessage());
                    }
                } else {
                    booking.setBookingStatus(BookingStatus.CANCELED);
                    try {
                        User clientUser = booking.getClient();
                        User expertUser = booking.getExpert().getUser();


                        String failedPushTitle = "Booking Failed";
                        String failedPushMessage = String.format("Your booking with expert %s could not be confirmed due to a payment issue.", expertUser.getFirstName());
                        notificationService.createAndSendNotification(NotificationRequest.builder().title(failedPushTitle).message(failedPushMessage).user(clientUser).build());

                        String failedEmailSubject = "Booking Attempt Failed";
                        String failedEmailBody = String.format("Dear %s,\n\nWe're writing to inform you that your booking attempt with %s failed due to a payment issue. Please try making the booking again.", clientUser.getFirstName(), expertUser.getFirstName());
                        publisher.publishEvent(new GmailNotificationEvent(this, SentEmailMessageRequest.builder().to(clientUser.getEmail()).subject(failedEmailSubject).body(failedEmailBody).build()));

                        String reason = "Payment failed. Stripe charge status: " + paymentIntent.getStatus();
                        if (paymentIntent.getLastPaymentError() != null) {
                            reason = "Payment failed: " + paymentIntent.getLastPaymentError().getMessage();
                        }
                        booking.setCancellationReason(reason);
                        System.out.println("Booking " + booking.getId() + " has been canceled due to payment failure.");
                    }catch (Exception e) {
                        logger.error(e.getMessage());
                    }
                }
                bookingRepository.save(booking);
            }
        }
    }
}
