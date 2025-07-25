package com.graduation.realestateconsulting.services;

import com.graduation.realestateconsulting.model.dto.request.*;
import com.graduation.realestateconsulting.model.dto.response.LoginResponse;
import com.graduation.realestateconsulting.model.dto.response.RefreshTokenResponse;
import com.graduation.realestateconsulting.model.dto.response.UserStatusResponse;

import java.time.LocalDateTime;

public interface AuthService {

    UserStatusResponse checkUserStatus(Long id);

    String register(RegisterRequest request);

    LoginResponse login(LoginRequest request);

    RefreshTokenResponse refreshToken(RefreshTokenRequest request);

    void verificationAccount(VerificationRequest request);

    void sendCode(CodeRequest dto);

    void changePassword(ResetPasswordRequest dto);

    void logout();

    void checkExpired(LocalDateTime dateTime);
}