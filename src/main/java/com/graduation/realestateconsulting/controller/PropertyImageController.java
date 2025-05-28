package com.graduation.realestateconsulting.controller;

import com.graduation.realestateconsulting.model.dto.request.PropertyImageRequest;
import com.graduation.realestateconsulting.model.dto.response.GlobalResponse;
import com.graduation.realestateconsulting.services.PropertyImageService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/property-images")
@RequiredArgsConstructor
@Tag(name = "Property Image Management", description = "APIs for managing property images")
public class PropertyImageController {

    private final PropertyImageService service;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Upload a property image",
            description = "Uploads an image for a property with optional additional data"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Image uploaded successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input or file format"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Property not found"
            ),
            @ApiResponse(
                    responseCode = "413",
                    description = "File size too large"
            ),
            @ApiResponse(
                    responseCode = "415",
                    description = "Unsupported media type"
            )
    })
    public ResponseEntity<?> save(@ModelAttribute PropertyImageRequest request) {
        GlobalResponse response = GlobalResponse.builder()
                .status("success")
                .data(service.save(request))
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a property image",
            description = "Deletes a property image by its ID"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Image deleted successfully",
                    content = @Content(
                            schema = @Schema(implementation = GlobalResponse.class),
                            examples = @ExampleObject(
                                    value = """
                        {
                          "status": "success",
                          "data": "Message => Property Image deleted successfully"
                        }
                        """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Image not found"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error deleting image file"
            )
    })
    public ResponseEntity<?> deleteById(
            @Parameter(
                    description = "ID of the property image to delete",
                    required = true,
                    example = "1"
            )
            @PathVariable Long id) throws IOException {
        service.delete(id);
        GlobalResponse response = GlobalResponse.builder()
                .status("success")
                .data("Message => Property Image deleted successfully")
                .build();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }
}