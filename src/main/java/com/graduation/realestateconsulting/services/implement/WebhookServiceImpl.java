package com.graduation.realestateconsulting.services.implement;

import com.graduation.realestateconsulting.model.entity.Booking;
import com.graduation.realestateconsulting.model.entity.CouponEntity;
import com.graduation.realestateconsulting.model.enums.BookingStatus;
import com.graduation.realestateconsulting.repository.BookingRepository;
import com.graduation.realestateconsulting.repository.CouponRepository;
import com.graduation.realestateconsulting.services.WebhookService;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WebhookServiceImpl implements WebhookService {

    private final BookingRepository bookingRepository;
    private final CouponRepository couponRepository;

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
            // --- الجزء الثاني: منطق العمل (هنا التعديلات) ---

            // تحقق من أننا نعالج الحجز مرة واحدة فقط
            if (booking.getBookingStatus() == BookingStatus.PENDING) { // أو AWAITING_PAYMENT

                if ("payment_intent.succeeded".equals(eventType)) {
                    booking.setBookingStatus(BookingStatus.CONFIRMED);
                    System.out.println("Booking " + booking.getId() + " has been confirmed!");
                    if (booking.getCoupon() != null) {
                        CouponEntity usedCoupon = booking.getCoupon();
                        usedCoupon.setTimesUsed(usedCoupon.getTimesUsed() + 1);
                        couponRepository.save(usedCoupon);
                        System.out.println("Coupon " + usedCoupon.getCode() + " usage incremented.");
                    }
                    // --------------------------------------------------

                    // TODO: send notification to user and expert

                } else {
                    // --- حالة فشل الدفع ---
                    booking.setBookingStatus(BookingStatus.CANCELED);
                    String reason = "Payment failed. Stripe charge status: " + paymentIntent.getStatus();
                    if (paymentIntent.getLastPaymentError() != null) {
                        reason = "Payment failed: " + paymentIntent.getLastPaymentError().getMessage();
                    }
                    booking.setCancellationReason(reason);
                    System.out.println("Booking " + booking.getId() + " has been canceled due to payment failure.");
                }

                bookingRepository.save(booking);
            }
        }
    }
}
