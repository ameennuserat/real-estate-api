package com.graduation.realestateconsulting.controller;

import com.graduation.realestateconsulting.model.dto.request.TicketRequest;
import com.graduation.realestateconsulting.model.dto.response.GlobalResponse;
import com.graduation.realestateconsulting.model.enums.HouseType;
import com.graduation.realestateconsulting.model.enums.ServiceType;
import com.graduation.realestateconsulting.services.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tickets")
@RequiredArgsConstructor
@Tag(name = "Ticket Management", description = "APIs for managing real estate consulting tickets")
public class TicketController {

    private final TicketService service;

    @GetMapping
    @Operation(summary = "Get all tickets", description = "Retrieves a paginated list of all tickets", parameters = {@Parameter(name = "page", description = "Page number (0-based)", in = ParameterIn.QUERY, schema = @Schema(type = "integer", defaultValue = "0")), @Parameter(name = "size", description = "Number of items per page", in = ParameterIn.QUERY, schema = @Schema(type = "integer", defaultValue = "20")), @Parameter(name = "sort", description = "Sorting criteria in the format: property(,asc|desc)", in = ParameterIn.QUERY, schema = @Schema(type = "string"), example = "createdAt,desc")})
    @ApiResponse(responseCode = "200", description = "Successfully retrieved ticket list")
    public ResponseEntity<?> findAll(@PageableDefault Pageable pageable) {
        GlobalResponse response = GlobalResponse.builder().status("success").data(service.findAll(pageable)).build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/filters")
    @Operation(summary = "Get all tickets by filters", description = "Retrieves a list of tickets")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved ticket list")
    public ResponseEntity<?> findByFilters(@RequestParam(required = false) Double lowPrice,
                                           @RequestParam(required = false) Double highPrice,
                                           @RequestParam(required = false) ServiceType serviceType,
                                           @RequestParam(required = false) HouseType houseType,
                                           @RequestParam(required = false) String lowArea,
                                           @RequestParam(required = false) String highArea,
                                           @RequestParam(required = false) String location) {
        GlobalResponse response = GlobalResponse.builder().status("success").data(service.findByFilters(lowPrice, highPrice, serviceType, houseType, lowArea, highArea, location)).build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/client/{clientId}")
    @Operation(summary = "Get tickets by client ID", description = "Retrieves a paginated list of tickets for a specific client")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Successfully retrieved tickets for the client", content = @Content(schema = @Schema(implementation = GlobalResponse.class))), @ApiResponse(responseCode = "404", description = "Client not found")})
    public ResponseEntity<?> findAllByClientId(@PageableDefault Pageable pageable, @Parameter(description = "ID of the client", required = true, example = "123") @PathVariable Long clientId) {
        GlobalResponse response = GlobalResponse.builder().status("success").data(service.findAllByClientId(pageable, clientId)).build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get ticket by ID", description = "Retrieves a single ticket by its unique identifier")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Successfully retrieved the ticket", content = @Content(schema = @Schema(implementation = GlobalResponse.class))), @ApiResponse(responseCode = "404", description = "Ticket not found")})
    public ResponseEntity<?> findById(@Parameter(description = "ID of the ticket to retrieve", required = true, example = "1") @PathVariable Long id) {
        GlobalResponse response = GlobalResponse.builder().status("success").data(service.findById(id)).build();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Create a new ticket", description = "Creates a new ticket for real estate consulting")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "Ticket created successfully", content = @Content(schema = @Schema(implementation = GlobalResponse.class))), @ApiResponse(responseCode = "400", description = "Invalid input")})
    public ResponseEntity<?> save(@RequestBody TicketRequest request) {
        GlobalResponse response = GlobalResponse.builder().status("success").data(service.save(request)).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a ticket", description = "Updates an existing ticket by its ID")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Ticket updated successfully", content = @Content(schema = @Schema(implementation = GlobalResponse.class))), @ApiResponse(responseCode = "404", description = "Ticket not found"), @ApiResponse(responseCode = "400", description = "Invalid input")})
    public ResponseEntity<?> update(@Parameter(description = "ID of the ticket to update", required = true, example = "1") @PathVariable Long id, @RequestBody TicketRequest request) {
        GlobalResponse response = GlobalResponse.builder().status("success").data(service.update(id, request)).build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a ticket", description = "Deletes a ticket by its ID")
    @ApiResponses({@ApiResponse(responseCode = "204", description = "Ticket deleted successfully", content = @Content(schema = @Schema(implementation = GlobalResponse.class))), @ApiResponse(responseCode = "404", description = "Ticket not found")})
    public ResponseEntity<?> deleteById(@Parameter(description = "ID of the ticket to delete", required = true, example = "1") @PathVariable Long id) {
        service.delete(id);
        GlobalResponse response = GlobalResponse.builder().status("success").data("Message => Ticket deleted successfully").build();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }
}