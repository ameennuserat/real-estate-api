package com.graduation.realestateconsulting.model.mapper;

import com.graduation.realestateconsulting.model.dto.request.RegisterRequest;
import com.graduation.realestateconsulting.model.entity.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

@Service

public class RegisterMapper {

    public User toEntity(RegisterRequest registerRequest) {
        System.out.println("RegisterMapper toEntity");
        return User.builder()
                .email(registerRequest.getEmail())
                .password(registerRequest.getPassword())
                .phone(registerRequest.getPhone())
                .verificationCode(RandomStringUtils.randomNumeric(6))
                .role(registerRequest.getRole())
                .enable(false)
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .build();
    }
}
