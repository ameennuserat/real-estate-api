package com.graduation.realestateconsulting.aspect;

import com.graduation.realestateconsulting.config.JwtService;
import com.graduation.realestateconsulting.model.dto.request.CancleRequest;
import com.graduation.realestateconsulting.model.entity.Booking;
import com.graduation.realestateconsulting.model.entity.Client;
import com.graduation.realestateconsulting.model.entity.Expert;
import com.graduation.realestateconsulting.model.entity.User;
import com.graduation.realestateconsulting.model.enums.Role;
import com.graduation.realestateconsulting.repository.BookingRepository;
import com.graduation.realestateconsulting.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class CancelBooking {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Before(value = "execution(* com.graduation.realestateconsulting.controller.BookingController.calledWithRefund(..)) || " +
    "execution(* com.graduation.realestateconsulting.controller.BookingController.calledWithoutRefund(..))")
    public void generateReportToFile(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        CancleRequest cancleRequest = (CancleRequest) args[0];
        Long bookingId = cancleRequest.getId();
        boolean isAuthorized = false;

        User user = userRepository.findByEmail(jwtService.getCurrentUserName()).orElseThrow(() -> new RuntimeException("User not found"));
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new RuntimeException("Booking not found"));



        if (user.getRole().equals(Role.EXPERT)) {
            Expert expert = user.getExpert();
            if (booking.getExpert().getId().equals(expert.getId())) {
                isAuthorized = true;
            }
        } else if (user.getRole().equals(Role.USER)) {
            Client client = user.getClient();
            if (booking.getClient().getId().equals(client.getId())) {
                isAuthorized = true;
            }
        }

        if (!isAuthorized) {
            throw new AccessDeniedException("You do not have permission to perform this action");
        }
            System.out.println("after check");
    }
}
