package com.graduation.realestateconsulting.controller;

import com.graduation.realestateconsulting.exceptions.ExceptionDto;
import com.graduation.realestateconsulting.model.dto.request.AvailabilityExceptionRequest;
import com.graduation.realestateconsulting.model.dto.request.UpdateAvailabilityExceptionRequest;
import com.graduation.realestateconsulting.model.dto.response.ExceptionResponse;
import com.graduation.realestateconsulting.model.dto.response.GlobalResponse;
import com.graduation.realestateconsulting.services.AvailabilityExceptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/availability_exception")
@Tag(name = "Availability Exception Management", description = "APIs for managing availability exceptions for consultants")
public class AvailabilityExceptionController {
    private final AvailabilityExceptionService availabilityExceptionService;

    @Operation(summary = "Create a new availability exception", description = "Adds a new period where a resource is unavailable or available.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Availability exception created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GlobalResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionResponse.class))),
    })
    @PostMapping
    public ResponseEntity<?> save(@RequestBody @Valid AvailabilityExceptionRequest request) {
        GlobalResponse globalResponse = GlobalResponse.builder()
                .status("success")
                .data(availabilityExceptionService.create(request))
                .build();
        return new ResponseEntity<>(globalResponse, HttpStatus.CREATED);
    }

    @Operation(summary = "Get all availability exceptions", description = "Retrieves a list of all existing availability exceptions.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GlobalResponse.class)))
    })
    @GetMapping
    public ResponseEntity<?> getAll() {
        GlobalResponse globalResponse = GlobalResponse.builder()
                .status("success")
                .data(availabilityExceptionService.getAll())
                .build();
        return ResponseEntity.ok(globalResponse);
    }

    @Operation(summary = "Update an existing availability exception", description = "Updates the details of an existing availability exception by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Availability exception updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GlobalResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDto.class))),
            @ApiResponse(responseCode = "404", description = "Availability exception not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDto.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @Parameter(description = "ID of the availability exception to be updated", required = true) @PathVariable Long id,
            @RequestBody UpdateAvailabilityExceptionRequest request) {
        GlobalResponse globalResponse = GlobalResponse.builder()
                .status("success")
                .data(availabilityExceptionService.update(request, id))
                .build();
        return ResponseEntity.ok(globalResponse);
    }

    @Operation(summary = "Delete an availability exception", description = "Deletes an availability exception by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Availability exception deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Availability exception not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDto.class))),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @Parameter(description = "ID of the availability exception to be deleted", required = true) @PathVariable Long id) {
        availabilityExceptionService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}