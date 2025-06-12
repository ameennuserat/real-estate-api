package com.graduation.realestateconsulting.controller;

import com.graduation.realestateconsulting.model.dto.request.FeedbackRequest;
import com.graduation.realestateconsulting.model.dto.response.GlobalResponse;
import com.graduation.realestateconsulting.services.BookingFeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("feedback")
public class FeedbackController {
    private final BookingFeedbackService service;

    @PostMapping
    public ResponseEntity<?> save(@RequestBody FeedbackRequest request) {
        GlobalResponse globalResponse = GlobalResponse.builder()
                .status("success")
                .data(service.save(request))
                .build();
        return new ResponseEntity<>(globalResponse, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<?> findAll() {
        GlobalResponse globalResponse = GlobalResponse.builder()
                .status("success")
                .data(service.getAll())
                .build();
        return ResponseEntity.ok(globalResponse);
    }

    @Operation(
            summary = "Get Feedback By Id."
    )
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        GlobalResponse globalResponse = GlobalResponse.builder()
                .status("success")
                .data(service.getById(id))
                .build();
        return ResponseEntity.ok(globalResponse);
    }

    @Operation(
            summary = "Get Feedback By Booking Id."
    )
    @GetMapping("booking/{bookingId}")
    public ResponseEntity<?> findByBookingId(@PathVariable Long bookingId) {
        GlobalResponse globalResponse = GlobalResponse.builder()
                .status("success")
                .data(service.getByBookingId(bookingId))
                .build();
        return ResponseEntity.ok(globalResponse);
    }
}
