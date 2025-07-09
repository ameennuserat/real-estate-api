package com.graduation.realestateconsulting.controller;

import com.graduation.realestateconsulting.model.dto.response.GlobalResponse;
import com.graduation.realestateconsulting.services.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService service;

    @GetMapping
    public ResponseEntity<?> findAll(@RequestParam(value = "page",defaultValue = "0") int page,
                                     @RequestParam(value = "size",defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
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

    @PostMapping("/add-follower/{expertId}")
    public ResponseEntity<?> addFollow(@PathVariable Long expertId) {
        service.addFollower(expertId);
        GlobalResponse response = GlobalResponse.builder()
                .status("Success")
                .data("Follow created successfully")
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/add-favorite/{expertId}")
    public ResponseEntity<?> addFavorite(@PathVariable Long expertId) {
        service.addFavorite(expertId);
        GlobalResponse response = GlobalResponse.builder()
                .status("Success")
                .data("Favorite created successfully")
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/remove-follower/{expertId}")
    public ResponseEntity<?> removeFollow(@PathVariable Long expertId) {
        service.deleteFollower(expertId);
        GlobalResponse response = GlobalResponse.builder()
                .status("Success")
                .data("Follow removed successfully")
                .build();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }

    @DeleteMapping("/remove-favorite/{expertId}")
    public ResponseEntity<?> removeFavorite(@PathVariable Long expertId) {
        service.deleteFavorite(expertId);
        GlobalResponse response = GlobalResponse.builder()
                .status("Success")
                .data("Favorite removed successfully")
                .build();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }

}