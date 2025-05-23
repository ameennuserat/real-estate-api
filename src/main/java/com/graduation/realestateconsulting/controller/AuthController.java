package com.graduation.realestateconsulting.controller;

import com.graduation.realestateconsulting.model.dto.request.*;
import com.graduation.realestateconsulting.model.dto.response.GlobalResponse;
import com.graduation.realestateconsulting.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> register(@ModelAttribute @Valid RegisterRequest registerRequest) {
        System.out.println("register");
        GlobalResponse response = GlobalResponse.builder()
                .status("Success")
                .data(authService.register(registerRequest))
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        GlobalResponse response = GlobalResponse.builder()
                .status("Success")
                .data(authService.login(request))
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest request) {
        GlobalResponse response = GlobalResponse.builder()
                .status("Success")
                .data(authService.refreshToken(request))
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verification")
    public ResponseEntity<?> verificationAccount(@RequestBody VerificationRequest request) {
        authService.verificationAccount(request);
        GlobalResponse response = GlobalResponse.builder()
                .status("Success")
                .data("message => Account verified Successfully")
                .build();
        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }

    @PostMapping("/send-code")
    public ResponseEntity<?> sendCode(CodeRequest dto){
        authService.sendCode(dto);
        GlobalResponse response = GlobalResponse.builder()
                .status("Success")
                .data("message => Response sent Successfully")
                .build();
        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(ResetPasswordRequest dto) {
        authService.changePassword(dto);
        GlobalResponse response = GlobalResponse.builder()
                .status("Success")
                .data("message => Password Changed Successfully")
                .build();
        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity<?> logout(){
        authService.logout();
        GlobalResponse response = GlobalResponse.builder()
                .status("Success")
                .data("message => User logout Successfully")
                .build();
        return new ResponseEntity<>(response,HttpStatus.NO_CONTENT);
    }
}
