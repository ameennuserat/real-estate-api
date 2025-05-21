package com.graduation.realestateconsulting.controller;

import com.graduation.realestateconsulting.model.dto.request.OfficeImageRequest;
import com.graduation.realestateconsulting.model.dto.request.OfficeRequest;
import com.graduation.realestateconsulting.model.dto.response.GlobalResponse;
import com.graduation.realestateconsulting.services.OfficeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/offices")
@RequiredArgsConstructor
public class OfficeController {

    private final OfficeService service;

    @GetMapping
    public ResponseEntity<?> findAll(@PageableDefault Pageable pageable) {
        GlobalResponse response = GlobalResponse.builder()
                .status("Success")
                .data(service.findAll(pageable))
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        GlobalResponse response = GlobalResponse.builder()
                .status("Success")
                .data(service.findById(id))
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMe() {
        GlobalResponse response = GlobalResponse.builder()
                .status("Success")
                .data(service.getMe())
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<?> updateMe(@RequestBody OfficeRequest request) {
        GlobalResponse response = GlobalResponse.builder()
                .status("Success")
                .data(service.updateMe(request))
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "upload-image",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImage(@ModelAttribute OfficeImageRequest request) throws IOException {
        service.uploadImage(request);
        GlobalResponse response = GlobalResponse.builder()
                .status("Success")
                .message("Commercial Register Image uploaded successfully")
                .build();
        return ResponseEntity.ok(response);
    }

}