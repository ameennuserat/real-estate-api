package com.graduation.realestateconsulting.services;

import com.graduation.realestateconsulting.model.dto.request.*;
import com.graduation.realestateconsulting.model.dto.response.LoginResponse;
import com.graduation.realestateconsulting.model.dto.response.RefreshTokenResponse;
import com.graduation.realestateconsulting.model.dto.response.RegisterResponse;

import java.time.LocalDateTime;
import java.util.Date;

public interface AuthService {

    RegisterResponse register(RegisterRequest request);

    LoginResponse login(LoginRequest request);

    RefreshTokenResponse refreshToken(RefreshTokenRequest request);

    void verificationAccount(VerificationRequest request);

    void sendCode(CodeRequest dto);

    void changePassword(ResetPasswordRequest dto);

    void logout();

    void checkExpired(LocalDateTime dateTime);
}