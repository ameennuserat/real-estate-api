package com.graduation.realestateconsulting.controller;

import com.graduation.realestateconsulting.model.dto.request.WorkingTimesRequest;
import com.graduation.realestateconsulting.model.dto.response.ExceptionResponse;
import com.graduation.realestateconsulting.model.dto.response.GlobalResponse;
import com.graduation.realestateconsulting.services.WorkingTimesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/working_times")
public class WorkingTimesController {
    private final WorkingTimesService workingTimesService;
    private static final Logger logger = LoggerFactory.getLogger(WorkingTimesController.class);

    @Operation(
            summary = "Create working times for an expert",
            description = "Accepts a list of working times and associates them with the specified expert by ID.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Successfully created",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = GlobalResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Expert not found",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionResponse.class)))
            }
    )
    @PostMapping("/{id}")
    public ResponseEntity<GlobalResponse> createWorkingTimes(
            @Parameter(description = "Expert ID", example = "5")
            @PathVariable Long id,

            @Parameter(description = "List of working times", required = true)
            @RequestBody List<WorkingTimesRequest> workingHours
    ) {
        logger.info("Creating working times for expert {}", id);
        GlobalResponse response = GlobalResponse.builder()
                .status("success")
                .data(workingTimesService.createWorkingTimes(id, workingHours))
                .build();

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get all working times",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = GlobalResponse.class)))
            }
    )
    @GetMapping
    public ResponseEntity<GlobalResponse> getAllWorkingTimes() {
        GlobalResponse response = GlobalResponse.builder()
                .status("success")
                .data(workingTimesService.getWorkingTimes())
                .build();
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Get working times by id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
                    @ApiResponse(responseCode = "404", description = "Expert not found")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<GlobalResponse> getWorkingTimes(
            @Parameter(description = "WorkingTime ID", example = "5")
            @PathVariable Long id
    ) {
        GlobalResponse response = GlobalResponse.builder()
                .status("success")
                .data(workingTimesService.getWorkingTimes(id))
                .build();
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Update a working time",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully updated"),
                    @ApiResponse(responseCode = "404", description = "Working time not found")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<GlobalResponse> updateWorkingTimes(
            @Parameter(description = "Working time ID", example = "10")
            @PathVariable Long id,

            @Parameter(description = "Updated working time data", required = true)
            @RequestBody WorkingTimesRequest workingHours
    ) {
        GlobalResponse response = GlobalResponse.builder()
                .status("success")
                .data(workingTimesService.updateWorkingTime(id, workingHours))
                .build();
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Delete a working time",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully deleted"),
                    @ApiResponse(responseCode = "404", description = "Working time not found")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteWorkingTimes(
            @Parameter(description = "Working time ID", example = "10")
            @PathVariable Long id
    ) {
        workingTimesService.deleteWorkingTime(id);
        GlobalResponse response = GlobalResponse.builder()
                .status("success")
                .data(ResponseEntity.noContent().build().getStatusCode())
                .build();
        return ResponseEntity.ok(response);
    }
}
