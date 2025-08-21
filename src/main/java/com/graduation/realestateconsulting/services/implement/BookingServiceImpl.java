package com.graduation.realestateconsulting.services.implement;

import com.graduation.realestateconsulting.config.JwtService;
import com.graduation.realestateconsulting.model.dto.request.BookingRequest;
import com.graduation.realestateconsulting.model.dto.request.CancleRequest;
import com.graduation.realestateconsulting.model.dto.request.DiscountResult;
import com.graduation.realestateconsulting.model.dto.response.BookingResponse;
import com.graduation.realestateconsulting.model.entity.*;
import com.graduation.realestateconsulting.model.enums.*;
import com.graduation.realestateconsulting.model.mapper.BookingMapper;
import com.graduation.realestateconsulting.repository.BookingRepository;
import com.graduation.realestateconsulting.repository.ClientRepository;
import com.graduation.realestateconsulting.repository.ExpertRepository;
import com.graduation.realestateconsulting.repository.UserRepository;
import com.graduation.realestateconsulting.services.ClientService;
import com.graduation.realestateconsulting.services.PaymentService;
import com.graduation.realestateconsulting.services.UserService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Refund;
import jakarta.transaction.Transactional;
import jakarta.xml.bind.SchemaOutputResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.graduation.realestateconsulting.services.BookingService;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ExpertRepository expertRepository;
    private final ClientRepository clientRepository;
    private final BookingMapper bookingMapper;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PaymentService paymentService;
    private final CouponServiceImpl couponService;

    @Override
    @Transactional
    public BookingResponse initiateBooking(BookingRequest request) throws StripeException {


        Expert expert = expertRepository.findById(request.getExpertId()).orElseThrow(() -> new IllegalArgumentException("Expert not found"));
        User client = userRepository.findById(request.getClientId()).orElseThrow(() -> new IllegalArgumentException("Client not found"));
        LocalDateTime startTime = request.getStartDate();
        LocalDateTime endTime = startTime.plusMinutes(request.getDuration());


        validateTimeSlot(expert.getId(), startTime, endTime);

        BigDecimal originalPrice = calculateOriginalPrice(expert, request.getDuration(), request.getCallType());

        DiscountResult discountResult = applyCouponIfPresent(originalPrice, request.getCouponCode(), client, expert);


        Booking booking = bookingMapper.toEntity(request, expert, client, startTime, endTime, originalPrice, discountResult);
        bookingRepository.save(booking);


        PaymentIntent paymentIntent = createPaymentIntentAndUpdateBooking(booking, discountResult.finalPrice());


        return bookingMapper.toDto(booking, Optional.of(paymentIntent.getClientSecret()));
    }


    private BigDecimal calculateOriginalPrice(Expert expert, int duration, CallType callType) {
        Double priceOfMinute = (callType == CallType.AUDIO) ? expert.getPerMinuteAudio() : expert.getPerMinuteVideo();
        return BigDecimal.valueOf(priceOfMinute).multiply(BigDecimal.valueOf(duration));
    }


    private void validateTimeSlot(Long expertId, LocalDateTime startTime, LocalDateTime endTime) {
        List<BookingStatus> conflictingStatuses = List.of(BookingStatus.CONFIRMED, BookingStatus.PENDING);
        if (bookingRepository.countConflictingBookings(expertId, startTime, endTime, conflictingStatuses) > 0) {
            throw new IllegalStateException("This time slot has just been booked. Please choose another time slot.");
        }
    }


    private DiscountResult applyCouponIfPresent(BigDecimal originalPrice, String couponCode, User client, Expert expert) {
        if (couponCode == null || couponCode.isEmpty()) {
            return new DiscountResult(originalPrice, BigDecimal.ZERO, null);
        }

        CouponEntity coupon = couponService.validateAndGetCoupon(couponCode, client, expert);

        BigDecimal finalPrice = calculateFinalPrice(originalPrice, coupon);
        BigDecimal discountAmount = originalPrice.subtract(finalPrice);

        return new DiscountResult(finalPrice, discountAmount, coupon);
    }

    private BigDecimal calculateFinalPrice(BigDecimal originalPrice, CouponEntity coupon) {
        if (coupon == null || coupon.getDiscountValue() == null) {
            return originalPrice;
        }

        BigDecimal finalPrice;

        if (coupon.getDiscountType() == DiscountType.PERCENTAGE) {
            BigDecimal percentageValue = coupon.getDiscountValue();

            BigDecimal discountAmountRaw = originalPrice.multiply(percentageValue);

            BigDecimal discountAmountWithHighPrecision = discountAmountRaw.divide(new BigDecimal("100"));

            BigDecimal roundedDiscountAmount = discountAmountWithHighPrecision.setScale(2, RoundingMode.HALF_UP);

            finalPrice = originalPrice.subtract(roundedDiscountAmount);

        } else if (coupon.getDiscountType() == DiscountType.FIXED_AMOUNT) {

            BigDecimal fixedAmount = coupon.getDiscountValue();
            finalPrice = originalPrice.subtract(fixedAmount);
        } else {
            return originalPrice;
        }

        if (finalPrice.compareTo(BigDecimal.ZERO) < 0) {
            return BigDecimal.ZERO;
        }
        return finalPrice;
    }

    private PaymentIntent createPaymentIntentAndUpdateBooking(Booking booking, BigDecimal finalPrice) throws StripeException {
        long finalPriceInCents = finalPrice.multiply(BigDecimal.valueOf(100)).longValue();

        PaymentIntent paymentIntent = paymentService.createPaymentIntent(booking.getId(), finalPriceInCents);

        booking.setPaymentIntentId(paymentIntent.getId());
        bookingRepository.save(booking);

        return paymentIntent;
    }


    @Override
    public BookingResponse getBooking(Long id) {
        Booking booking = bookingRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Booking not found"));
        return bookingMapper.toDto(booking);
    }

    @Override
    public List<BookingResponse> getAllBookings(BookingStatus status) {
        try {
            User user = userRepository.findByEmail(jwtService.getCurrentUserName()).orElseThrow();
            List<Booking> bookings = null;
            if(user.getRole().equals(Role.EXPERT)) {
                bookings = bookingRepository.findAllByExpertIdAndBookingStatus(user.getExpert().getId(), status);
            }
            
            else {   bookings = bookingRepository.findAllByClientIdAndBookingStatus(user.getClient().getId(), status); }

            return bookingMapper.toDtos(bookings);
        } catch (Exception e) {
            throw new RuntimeException("you don't have any bookings for " + jwtService.getCurrentUserName());
        }
    }

    @Transactional
    @Override
    public BookingResponse cancleBookingWithRefundMony(CancleRequest request) throws IllegalAccessException, StripeException {
        Booking booking = bookingRepository.findById(request.getId()).orElseThrow();
        if (!booking.getBookingStatus().equals(BookingStatus.CONFIRMED)) {
            throw new IllegalAccessException("This booking not confirmed.");
        }
        LocalDateTime now = LocalDateTime.now().plusHours(24);
        LocalDateTime timeBooking = booking.getStartTime();
        User user = userRepository.findByEmail(jwtService.getCurrentUserName()).orElseThrow();

        if (!now.isBefore(timeBooking) && !user.getRole().equals(Role.EXPERT)) {
            throw new IllegalAccessException("You will not be able to get your money back if you cancel the reservation.");
        }

        if (booking.getPaymentIntentId() != null) {
            Refund refund = paymentService.createRefund(booking.getPaymentIntentId());
            if ("succeeded".equals(refund.getStatus())) {
                booking.setRefundStatus(RefundStatus.COMPLETED);
                booking.setBookingStatus(BookingStatus.CANCELED);
            } else {
                booking.setRefundStatus(RefundStatus.FAILED);
            }
        }

        // TODO cancel pending booking

        booking.setCancellationReason(request.getCancellationReason());
        booking.setCancelled_at(LocalDateTime.now());
        booking.setUser(user);

        return bookingMapper.toDto(bookingRepository.save(booking));
    }

    @Override
    public BookingResponse cancleBookingWithoutRefundMony(CancleRequest request) throws IllegalAccessException {
        Booking booking = bookingRepository.findById(request.getId()).orElseThrow();
        User user = userRepository.findByEmail(jwtService.getCurrentUserName()).orElseThrow();
        if (!booking.getBookingStatus().equals(BookingStatus.CONFIRMED)) {
            throw new IllegalAccessException("This booking not confirmed.");
        }
        booking.setBookingStatus(BookingStatus.CANCELED);
        booking.setRefundStatus(RefundStatus.NONE);
        booking.setCancellationReason(request.getCancellationReason());
        booking.setCancelled_at(LocalDateTime.now());
        booking.setUser(user);
        return bookingMapper.toDto(bookingRepository.save(booking));
    }
}