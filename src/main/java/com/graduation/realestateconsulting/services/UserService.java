package com.graduation.realestateconsulting.services;

import com.graduation.realestateconsulting.model.dto.request.UserImageRequest;
import com.graduation.realestateconsulting.model.dto.response.UserResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface UserService {

    void uploadImage(UserImageRequest request) throws IOException;


    UserResponse getMe();
}