package com.graduation.realestateconsulting.controller;

import com.graduation.realestateconsulting.model.dto.request.PropertyImageRequest;
import com.graduation.realestateconsulting.model.dto.response.GlobalResponse;
import com.graduation.realestateconsulting.services.PropertyImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/property-images")
@RequiredArgsConstructor
public class PropertyImageController {

    private final PropertyImageService service;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> save(@ModelAttribute PropertyImageRequest request) {
        GlobalResponse response = GlobalResponse.builder()
                .status("success")
                .data(service.save(request))
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) throws IOException {
        service.delete(id);
        GlobalResponse response = GlobalResponse.builder()
                .status("success")
                .data("Message => Property Image deleted successfully")
                .build();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }
}