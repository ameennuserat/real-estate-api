package com.graduation.realestateconsulting.controller;

import com.graduation.realestateconsulting.model.dto.request.PostsRequest;
import com.graduation.realestateconsulting.model.dto.response.GlobalResponse;
import com.graduation.realestateconsulting.services.PostsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostsController {

    private final PostsService service;

    @GetMapping
    public ResponseEntity<?> findAll() {
        GlobalResponse response = GlobalResponse.builder()
                .status("success")
                .data(service.findAll())
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/home")
    public ResponseEntity<?> findAllForHome(@RequestParam(value = "page",defaultValue = "0") int page,
                                            @RequestParam(value = "size",defaultValue = "10") int size
    ) {
        PageRequest pageRequest = PageRequest.of(page, size);
        GlobalResponse response = GlobalResponse.builder()
                .status("success")
                .data(service.findForHome(pageRequest))
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/expert/{expertId}")
    public ResponseEntity<?> findAllByExpertId (@PathVariable Long expertId) {
        GlobalResponse response = GlobalResponse.builder()
                .status("success")
                .data(service.findAllByExpertId(expertId))
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

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> save(@ModelAttribute PostsRequest request) {
        GlobalResponse response = GlobalResponse.builder()
                .status("success")
                .data(service.save(request)).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestParam("content") String content) {
        GlobalResponse response = GlobalResponse.builder()
                .status("success")
                .data(service.update(id,content)).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        service.delete(id);
        GlobalResponse response = GlobalResponse.builder()
                .status("success")
                .data("Message => Post deleted successfully").build();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }
}