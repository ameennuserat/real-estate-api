package com.graduation.realestateconsulting.controller;

import com.graduation.realestateconsulting.model.dto.request.PropertyRequest;
import com.graduation.realestateconsulting.model.dto.response.GlobalResponse;
import com.graduation.realestateconsulting.model.enums.HouseType;
import com.graduation.realestateconsulting.model.enums.ServiceType;
import com.graduation.realestateconsulting.services.PropertyService;
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
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/properties")
@RequiredArgsConstructor
@Tag(name = "Property Management", description = "APIs for managing real estate properties")
public class PropertyController {

    private final PropertyService service;

    @GetMapping
    @Operation(summary = "Get all properties", description = "Retrieves a paginated list of all properties", parameters = {@Parameter(name = "page", description = "Page number (0-based)", in = ParameterIn.QUERY, schema = @Schema(type = "integer", defaultValue = "0")), @Parameter(name = "size", description = "Number of items per page", in = ParameterIn.QUERY, schema = @Schema(type = "integer", defaultValue = "20")), @Parameter(name = "sort", description = "Sorting criteria in the format: property(,asc|desc)", in = ParameterIn.QUERY, schema = @Schema(type = "string"), example = "price,asc")})
    @ApiResponse(responseCode = "200", description = "Successfully retrieved property list")
    public ResponseEntity<?> findAll(@PageableDefault Pageable pageable) {
        GlobalResponse response = GlobalResponse.builder().status("success").data(service.findAll(pageable)).build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/filters")
    @Operation(summary = "Get all properties by filters", description = "Retrieves a list of properties")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved property list")
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


    @GetMapping("/office/{officeId}")
    @Operation(summary = "Get properties by office ID", description = "Retrieves a paginated list of properties managed by a specific office")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Successfully retrieved properties for the office", content = @Content(schema = @Schema(implementation = GlobalResponse.class))), @ApiResponse(responseCode = "404", description = "Office not found")})
    public ResponseEntity<?> findAllByOfficeId(@PageableDefault Pageable pageable, @Parameter(description = "ID of the office", required = true, example = "456") @PathVariable Long officeId) {
        GlobalResponse response = GlobalResponse.builder().status("success").data(service.findAllByOfficeId(pageable, officeId)).build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get property by ID", description = "Retrieves detailed information about a specific property")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Successfully retrieved the property"), @ApiResponse(responseCode = "404", description = "Property not found")})
    public ResponseEntity<?> findById(@Parameter(description = "ID of the property to retrieve", required = true, example = "1") @PathVariable Long id) {
        GlobalResponse response = GlobalResponse.builder().status("success").data(service.findById(id)).build();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Create a new property", description = "Adds a new property to the real estate portfolio")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "Property created successfully"), @ApiResponse(responseCode = "400", description = "Invalid input")})
    public ResponseEntity<?> save(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Property details to create", required = true) @RequestBody PropertyRequest request) {
        GlobalResponse response = GlobalResponse.builder().status("success").data(service.save(request)).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a property", description = "Updates the details of an existing property")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Property updated successfully"), @ApiResponse(responseCode = "400", description = "Invalid input"), @ApiResponse(responseCode = "404", description = "Property not found")})
    public ResponseEntity<?> update(@Parameter(description = "ID of the property to update", required = true, example = "1") @PathVariable Long id, @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Updated property details", required = true, content = @Content(schema = @Schema(implementation = PropertyRequest.class))) @RequestBody PropertyRequest request) {
        GlobalResponse response = GlobalResponse.builder().status("success").data(service.update(id, request)).build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a property", description = "Removes a property from the system")
    @ApiResponses({@ApiResponse(responseCode = "204", description = "Property deleted successfully", content = @Content(schema = @Schema(implementation = GlobalResponse.class), examples = @ExampleObject(value = """
            {
              "status": "success",
              "data": "Message => Property deleted successfully"
            }
            """))), @ApiResponse(responseCode = "404", description = "Property not found"), @ApiResponse(responseCode = "500", description = "Internal server error during deletion")})
    public ResponseEntity<?> deleteById(@Parameter(description = "ID of the property to delete", required = true, example = "1") @PathVariable Long id) throws IOException {
        service.delete(id);
        GlobalResponse response = GlobalResponse.builder().status("success").data("Message => Property deleted successfully").build();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }
}