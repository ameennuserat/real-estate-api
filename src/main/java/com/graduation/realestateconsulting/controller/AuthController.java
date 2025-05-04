package com.graduation.realestateconsulting.controller;

import com.graduation.realestateconsulting.model.dto.request.*;
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
        return new ResponseEntity<>(authService.register(registerRequest), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(authService.refreshToken(request));
    }

    @PostMapping("/verification")
    public ResponseEntity<?> verificationAccount(@RequestBody VerificationRequest request) {
        authService.verificationAccount(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/send-code")
    public ResponseEntity<?> sendCode(CodeRequest dto){
        authService.sendCode(dto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(ResetPasswordRequest dto) {
        authService.changePassword(dto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity<?> logout(){
        authService.logout();
        return ResponseEntity.noContent().build();
    }
}
