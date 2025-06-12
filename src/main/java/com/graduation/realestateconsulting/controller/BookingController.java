package com.graduation.realestateconsulting.controller;

import com.graduation.realestateconsulting.model.dto.request.BookingRequest;
import com.graduation.realestateconsulting.model.dto.request.CancleRequest;
import com.graduation.realestateconsulting.model.dto.response.ExceptionResponse;
import com.graduation.realestateconsulting.model.dto.response.GlobalResponse;
import com.graduation.realestateconsulting.model.enums.BookingStatus;
import com.graduation.realestateconsulting.services.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("booking")
public class BookingController {
    private final BookingService bookingService;

    @Operation(
            summary = "Book an appointment with the expert ",
            description = "Accepts a list of expertId , clientId , callType , duration and booking time.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Successfully created",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = GlobalResponse.class))),
                    @ApiResponse(responseCode = "400 or 500", description = "Any Exception",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionResponse.class)))
            }
    )
    @PostMapping
    public ResponseEntity<?> save(@RequestBody BookingRequest request) throws IllegalAccessException {
        GlobalResponse response = GlobalResponse.builder()
                .data(bookingService.book(request))
                .status("SUCCESS")
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get all booking by Status "
    )
    @GetMapping("/all/{status}")
    public ResponseEntity<?> findAll(@PathVariable BookingStatus status) {
        GlobalResponse response = GlobalResponse.builder()
                .data(bookingService.getAllBookings(status))
                .status("SUCCESS")
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        GlobalResponse globalResponse = GlobalResponse.builder()
                .data(bookingService.getBooking(id))
                .status("SUCCESS")
                .build();
        return ResponseEntity.ok(globalResponse);
    }


    @Operation(
            summary = "Cancellation of reservation by expert or client with refund mony",
            description = "Cancellation of the reservation by the expert or the client, provided that the cancellation time is 24 hours before the reservation time, if the client is the one who wants to cancel the reservation.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = GlobalResponse.class))),
                    @ApiResponse(responseCode = "400 or 500", description = "Any Exception",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionResponse.class)))
            }
    )
    @PostMapping("cancelled-with")
    public ResponseEntity<?> calledWithRefund(@RequestBody CancleRequest request) throws IllegalAccessException {
        GlobalResponse globalResponse = GlobalResponse.builder()
                .status("SUCCESS")
                .data(bookingService.cancleBookingWithRefundMony(request))
                .build();
        return ResponseEntity.ok(globalResponse);
    }


    @Operation(
            summary = "Cancellation of reservation by client withOut refund mony",
            description = "Cancellation of the reservation by the customer without refunding the money, less than 24 hours after the reservation time",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = GlobalResponse.class))),
                    @ApiResponse(responseCode = "400 or 500", description = "Any Exception",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionResponse.class)))
            }
    )
    @PostMapping("cancelled-without")
    public ResponseEntity<?> calledWithoutRefund(@RequestBody CancleRequest request) throws IllegalAccessException {
        GlobalResponse globalResponse = GlobalResponse.builder()
                .status("SUCCESS")
                .data(bookingService.cancleBookingWithoutRefundMony(request))
                .build();

        return ResponseEntity.ok(globalResponse);
    }
}
