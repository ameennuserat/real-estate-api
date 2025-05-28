package com.graduation.realestateconsulting.services.implement;

import com.graduation.realestateconsulting.config.JwtService;
import com.graduation.realestateconsulting.model.dto.request.*;
import com.graduation.realestateconsulting.model.dto.response.LoginResponse;
import com.graduation.realestateconsulting.model.dto.response.RefreshTokenResponse;
import com.graduation.realestateconsulting.model.entity.User;
import com.graduation.realestateconsulting.model.enums.Role;
import com.graduation.realestateconsulting.model.enums.UserStatus;
import com.graduation.realestateconsulting.model.mapper.UserMapper;
import com.graduation.realestateconsulting.observer.events.CreateClientEvent;
import com.graduation.realestateconsulting.observer.events.CreateExpertEvent;
import com.graduation.realestateconsulting.observer.events.CreateOfficeEvent;
import com.graduation.realestateconsulting.observer.events.GmailNotificationEvent;
import com.graduation.realestateconsulting.repository.UserRepository;
import com.graduation.realestateconsulting.services.AuthService;
import com.graduation.realestateconsulting.trait.SendEmailMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final ApplicationEventPublisher publisher;

    @Transactional
    @Override
    public String register(RegisterRequest request) {
        User user = userMapper.toEntity(request);
        User savedUser = userRepository.save(user);

        if (request.getRole() == Role.USER) {
            publisher.publishEvent(new CreateClientEvent(this, savedUser));
        } else if (request.getRole() == Role.OFFICE) {
            publisher.publishEvent(new CreateOfficeEvent(this, request, savedUser, request.getCommercialRegisterImage()));
        } else if (request.getRole() == Role.EXPERT) {
            publisher.publishEvent(new CreateExpertEvent(this, request, savedUser, request.getIdCardImage(), request.getDegreeCertificateImage()));
        }
        publisher.publishEvent(new GmailNotificationEvent(this, SentEmailMessageRequest.builder().to(savedUser.getEmail()).body(savedUser.getVerificationCode()).subject("Verification your account").build()));
        return "registered successfully";
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(
                () -> new IllegalArgumentException("user with email " + request.getEmail() + " is not found")
        );

        if(user.getStatus() != UserStatus.AVAILABLE){
            throw new IllegalArgumentException("user is "+ user.getStatus().name());
        }

        user.setFcmToken(request.getFcmToken());
        User savedUser = userRepository.save(user);

        String jwt = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return LoginResponse.builder()
                .user(userMapper.toDto(savedUser))
                .token(jwt)
                .refreshToken(refreshToken)
                .build();
    }


    @Override
    public RefreshTokenResponse refreshToken(RefreshTokenRequest request) {
        String username = jwtService.extractUsername(request.getRefreshToken());
        User user = userRepository.findByEmail(username).orElseThrow(
                () -> new IllegalArgumentException("user with email " + username + " is not found")
        );
        if (jwtService.isTokenValid(request.getRefreshToken(), user)) {
            String jwt = jwtService.generateToken(user);

            return RefreshTokenResponse.builder()
                    .token(jwt)
                    .refreshToken(request.getRefreshToken())
                    .build();
        }
        return null;
    }

    @Override
    public void verificationAccount(VerificationRequest request) {
        log.info("verificationAccount");
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new IllegalArgumentException("Invalid email"));
        log.info(user.getCreatedAt().toString()+"before calling");
        this.checkExpired(user.getCreatedAt());
        if (user.getVerificationCode().equals(request.getVerificationCode())) {
            user.setEnable(true);
            userRepository.save(user);
        } else {
            throw new IllegalArgumentException("Invalid verification code");
        }
    }

    @Override
    public void sendCode(CodeRequest dto) {
        String verificationCode = RandomStringUtils.randomNumeric(6);
        publisher.publishEvent(new GmailNotificationEvent(this, SentEmailMessageRequest.builder().to(dto.getEmail()).body(verificationCode).subject("confirm your account").build()));
        User user = userRepository.findByEmail(dto.getEmail()).orElseThrow(() -> new RuntimeException("Email Not Found"));
        user.setVerificationCode(verificationCode);
        userRepository.save(user);
    }

    @Override
    public void changePassword(ResetPasswordRequest dto) {
        User user = userRepository.findByEmail(dto.getEmail()).orElseThrow(() -> new RuntimeException("Email Not Found"));
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        userRepository.save(user);
    }

    @Override
    public void logout() {
        User user = userRepository.findByEmail(jwtService.getCurrentUserName()).orElseThrow(() ->
                new IllegalArgumentException("user not found"));
        user.setFcmToken(null);
        userRepository.save(user);
    }

    @Override
    public void checkExpired(LocalDateTime dateTime) {
        log.info(dateTime.toString()+"after calling");
        dateTime = dateTime.plusSeconds(5);
        log.info("createdAt"+dateTime.toString());
        log.info(LocalDateTime.now().toString());
        if(dateTime.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Expired time resend code");
        }
    }
}