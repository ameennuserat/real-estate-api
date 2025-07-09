package com.graduation.realestateconsulting.services;

import com.graduation.realestateconsulting.model.dto.request.UserImageRequest;
import com.graduation.realestateconsulting.model.dto.response.UserResponse;
import com.graduation.realestateconsulting.model.enums.UserStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface UserService {

    void uploadImage(UserImageRequest request) throws IOException;

    UserResponse getMe();

    UserResponse updateStatus(Long id, UserStatus status);

    void deleteById(Long id);
}