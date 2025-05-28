package com.graduation.realestateconsulting.services;

import com.graduation.realestateconsulting.model.dto.request.PropertyImageRequest;
import com.graduation.realestateconsulting.model.dto.response.PropertyImageResponse;

import java.io.IOException;

public interface PropertyImageService {

    PropertyImageResponse save(PropertyImageRequest request);
    void delete(Long id) throws IOException;

}