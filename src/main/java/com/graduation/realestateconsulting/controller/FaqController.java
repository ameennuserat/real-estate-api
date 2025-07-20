package com.graduation.realestateconsulting.controller;

import com.graduation.realestateconsulting.model.dto.request.FaqRequest;
import com.graduation.realestateconsulting.model.dto.response.GlobalResponse;
import com.graduation.realestateconsulting.services.FaqService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/FAQs")
@RequiredArgsConstructor
public class FaqController {

    private final FaqService service;

    @GetMapping
    public ResponseEntity<?> findAll() {
        GlobalResponse response = GlobalResponse.builder()
                .status("success")
                .data(service.findAll())
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<?> findAllByCategoryId(@PathVariable Long categoryId) {
        GlobalResponse response = GlobalResponse.builder()
                .status("success")
                .data(service.findAllByCategoryId(categoryId))
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        GlobalResponse response = GlobalResponse.builder().status("success")
                .data(service.findById(id))
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody FaqRequest request) {
        GlobalResponse response = GlobalResponse.builder()
                .status("success")
                .data(service.save(request)).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody FaqRequest request, @PathVariable Long id) {
        GlobalResponse response = GlobalResponse.builder()
                .status("success")
                .data(service.update(id,request)).build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        service.delete(id);
        GlobalResponse response = GlobalResponse.builder()
                .status("success")
                .data("Message => Faq deleted successfully").build();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }
}