package com.graduation.realestateconsulting.model.mapper;

import com.graduation.realestateconsulting.model.dto.response.LoginResponse;
import com.graduation.realestateconsulting.model.dto.response.UserResponse;
import com.graduation.realestateconsulting.model.entity.User;
import org.springframework.stereotype.Service;


@Service
public class LoginMapper {

    public LoginResponse toDto(User user,String token,String refreshToken) {
       return LoginResponse.builder()
                .token(token)
                .refreshToken(refreshToken)
                .user(UserResponse.builder()
                        .id(user.getId())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .phone(user.getPhone())
                        .email(user.getEmail())
                        .fcmToken(user.getFcmToken())
                        .enabled(user.isEnabled())
                        .build())
                .build();
    }

}
