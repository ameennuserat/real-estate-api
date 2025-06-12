package com.graduation.realestateconsulting.controller;

import com.graduation.realestateconsulting.model.dto.request.ExpertImageRequest;
import com.graduation.realestateconsulting.model.dto.request.ExpertRequest;
import com.graduation.realestateconsulting.model.dto.response.GlobalResponse;
import com.graduation.realestateconsulting.model.enums.UserStatus;
import com.graduation.realestateconsulting.services.ExpertService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/experts")
@RequiredArgsConstructor
public class ExpertController {

    private final ExpertService service;

    @GetMapping
    public ResponseEntity<?> findAll(@PageableDefault Pageable pageable) {
        GlobalResponse response = GlobalResponse.builder()
                .status("Success")
                .data(service.findAll(pageable))
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status")
    public ResponseEntity<?> findAllByUserStatus(@RequestParam(name = "status") UserStatus status) {
        GlobalResponse response = GlobalResponse.builder()
                .status("Success")
                .data(service.findAllByUserStatus(status))
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
    public ResponseEntity<?> updateMe(@RequestBody ExpertRequest request) {
        GlobalResponse response = GlobalResponse.builder()
                .status("Success")
                .data(service.updateMe(request))
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "upload-image",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImage(@ModelAttribute ExpertImageRequest request) throws IOException {
        service.uploadImage(request);
        GlobalResponse response = GlobalResponse.builder()
                .status("Success")
                .data(ResponseEntity.noContent().build().getStatusCode())
                .build();
        return ResponseEntity.ok(response);
    }

}