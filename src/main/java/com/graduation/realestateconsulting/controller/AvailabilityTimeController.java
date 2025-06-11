package com.graduation.realestateconsulting.controller;

import com.graduation.realestateconsulting.model.dto.request.AvailableTimeToBookingRequest;
import com.graduation.realestateconsulting.model.dto.response.ExceptionResponse;
import com.graduation.realestateconsulting.model.dto.response.GlobalResponse;
import com.graduation.realestateconsulting.services.AvailabilityTimeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("availability_time")
public class AvailabilityTimeController {
    private final AvailabilityTimeService availabilityTimeService;

    @Operation(
            summary = "Get available booking periods",
            description = "It takes the requested time and date, expert ID, and consultation duration and returns available booking slots based on expert availability.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = GlobalResponse.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "status": "Success",
                                                 "data": [
                                                                       "10:30:00",
                                                                       "10:45:00",
                                                                       "11:00:00",
                                                                       "11:15:00",
                                                                       "11:30:00",
                                                                       "11:45:00",
                                                                       "12:00:00",
                                                                       "12:15:00",
                                                                       "12:30:00",
                                                                       "12:45:00",
                                                                       "13:00:00",
                                                                       "13:15:00",
                                                                       "13:30:00",
                                                                       "13:45:00",
                                                                       "14:00:00",
                                                                       "14:15:00",
                                                                       "14:30:00",
                                                                       "14:45:00",
                                                                       "15:00:00",
                                                                       "15:15:00",
                                                                       "15:30:00",
                                                                       "15:45:00",
                                                                       "16:00:00",
                                                                       "16:15:00",
                                                                       "16:30:00",
                                                                       "16:45:00",
                                                                       "17:00:00",
                                                                       "17:15:00"
                                                                     ]
                                            }
                                            """))),
                    @ApiResponse(responseCode = "400", description = "Bad request",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionResponse.class)))
            }
    )
    @PostMapping
    public ResponseEntity<?> availabilityTime(@RequestBody AvailableTimeToBookingRequest request) {
        GlobalResponse globalResponse = GlobalResponse.builder()
                .data(availabilityTimeService.findAvailablePeriods(request))
                .status("Success")
                .build();
        return ResponseEntity.ok(globalResponse);
    }
}
