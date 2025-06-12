package com.graduation.realestateconsulting.services.implement;

import com.graduation.realestateconsulting.config.JwtService;
import com.graduation.realestateconsulting.model.dto.request.BookingRequest;
import com.graduation.realestateconsulting.model.dto.request.CancleRequest;
import com.graduation.realestateconsulting.model.dto.response.BookingResponse;
import com.graduation.realestateconsulting.model.entity.Booking;
import com.graduation.realestateconsulting.model.entity.Client;
import com.graduation.realestateconsulting.model.entity.Expert;
import com.graduation.realestateconsulting.model.entity.User;
import com.graduation.realestateconsulting.model.enums.BookingStatus;
import com.graduation.realestateconsulting.model.enums.CallType;
import com.graduation.realestateconsulting.model.enums.RefundStatus;
import com.graduation.realestateconsulting.model.enums.Role;
import com.graduation.realestateconsulting.model.mapper.BookingMapper;
import com.graduation.realestateconsulting.repository.BookingRepository;
import com.graduation.realestateconsulting.repository.ClientRepository;
import com.graduation.realestateconsulting.repository.ExpertRepository;
import com.graduation.realestateconsulting.repository.UserRepository;
import com.graduation.realestateconsulting.services.ClientService;
import com.graduation.realestateconsulting.services.UserService;
import jakarta.transaction.Transactional;
import jakarta.xml.bind.SchemaOutputResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.graduation.realestateconsulting.services.BookingService;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RequiredArgsConstructor
@Service
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ExpertRepository expertRepository;
    private final ClientRepository clientRepository;
    private final BookingMapper bookingMapper;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public BookingResponse book(BookingRequest request) throws IllegalAccessException {
        // end time
        LocalDateTime startTime = request.getStartDate().atDate(LocalDate.now());
        LocalDateTime end = startTime.plusMinutes(request.getDuration());

        //TODO add payment method to payment befor confirme booking

        // booking cost
        double priceOfMinute;
        Expert expert = expertRepository.findById(request.getExpertId()).orElseThrow();
        if (request.getCallType().equals(CallType.AUDIO)) {
            priceOfMinute = expert.getPerMinuteAudio();
        } else priceOfMinute = expert.getPerMinuteVideo();
        double price = priceOfMinute * request.getDuration();

        Client client = clientRepository.findById(request.getClientId()).orElseThrow();

        if (bookingRepository.countConflictingBookings(expert.getId(), startTime, end, BookingStatus.CONFIRMED) > 0) {
            throw new IllegalAccessException("This time slot has just been booked. Please choose another time slot.");
        }
        return bookingMapper.toDto(bookingRepository.save(bookingMapper.toEntity(request, expert, client, startTime, end, price)));
    }

    @Override
    public BookingResponse getBooking(Long id) {
        Booking booking = bookingRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Booking not found"));
        return bookingMapper.toDto(booking);
    }

    @Override
    public List<BookingResponse> getAllBookings(BookingStatus status) {
        User user = userRepository.findByEmail(jwtService.getCurrentUserName()).orElseThrow();
        List<Booking> bookings = bookingRepository.findAllByExpertIdAndBookingStatus(user.getExpert().getId(),status);
        return bookingMapper.toDtos(bookings);
    }

    @Transactional
    @Override
    public BookingResponse cancleBookingWithRefundMony(CancleRequest request) throws IllegalAccessException {
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
        booking.setBookingStatus(BookingStatus.CANCELED);
        booking.setCancellationReason(request.getCancellationReason());
        booking.setRefundStatus(RefundStatus.PENDING);
        booking.setCancelled_at(LocalDateTime.now());
        booking.setUser(user);

        //TODO add operation refund mony to queue
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