package com.graduation.realestateconsulting.controller;

import com.graduation.realestateconsulting.model.dto.response.GlobalResponse;
import com.graduation.realestateconsulting.services.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService service;

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

    @PostMapping("/add-follow/{id}")
    public ResponseEntity<?> addFollow(@PathVariable Long id) {
        service.addFollow(id);
        GlobalResponse response = GlobalResponse.builder()
                .status("Success")
                .data("Follow created successfully")
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/add-favorite/{id}")
    public ResponseEntity<?> addFavorite(@PathVariable Long id) {
        service.addFavorite(id);
        GlobalResponse response = GlobalResponse.builder()
                .status("Success")
                .data("Favorite created successfully")
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/remove-follow/{id}")
    public ResponseEntity<?> removeFollow(@PathVariable Long id) {
        service.deleteFollow(id);
        GlobalResponse response = GlobalResponse.builder()
                .status("Success")
                .data("Follow removed successfully")
                .build();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }

    @DeleteMapping("/remove-favorite/{id}")
    public ResponseEntity<?> removeFavorite(@PathVariable Long id) {
        service.deleteFavorite(id);
        GlobalResponse response = GlobalResponse.builder()
                .status("Success")
                .data("Favorite removed successfully")
                .build();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }

}