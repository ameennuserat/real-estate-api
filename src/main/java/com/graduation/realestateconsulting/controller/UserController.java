package com.graduation.realestateconsulting.controller;

import com.graduation.realestateconsulting.model.dto.request.UserImageRequest;
import com.graduation.realestateconsulting.model.dto.response.GlobalResponse;
import com.graduation.realestateconsulting.model.enums.UserStatus;
import com.graduation.realestateconsulting.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping(value = "/upload-image",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImage(@ModelAttribute UserImageRequest request) throws IOException {
        userService.uploadImage(request);
        GlobalResponse response = GlobalResponse.builder()
                .status("Success")
                .data("message => User image uploaded successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMe(){
        GlobalResponse response = GlobalResponse.builder()
                .status("Success")
                .data(userService.getMe()).build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateStatus(@PathVariable Long id,@RequestParam(name = "status") UserStatus status){
        GlobalResponse response = GlobalResponse.builder()
                .status("Success")
                .data(userService.updateStatus(id,status)).build();
        return ResponseEntity.ok(response);
    }



}