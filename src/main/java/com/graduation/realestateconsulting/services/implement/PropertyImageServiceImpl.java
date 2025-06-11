package com.graduation.realestateconsulting.services.implement;

import com.graduation.realestateconsulting.model.dto.request.PropertyImageRequest;
import com.graduation.realestateconsulting.model.dto.request.PropertyRequest;
import com.graduation.realestateconsulting.model.dto.response.PropertyImageResponse;
import com.graduation.realestateconsulting.model.dto.response.PropertyResponse;
import com.graduation.realestateconsulting.model.entity.Property;
import com.graduation.realestateconsulting.model.entity.PropertyImage;
import com.graduation.realestateconsulting.model.mapper.PropertyImageMapper;
import com.graduation.realestateconsulting.repository.PropertyImageRepository;
import com.graduation.realestateconsulting.services.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.graduation.realestateconsulting.services.PropertyImageService;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class PropertyImageServiceImpl implements PropertyImageService{

    private final PropertyImageRepository repository;
    private final PropertyImageMapper mapper;

    private final ImageService imageService;

    @Override
    public PropertyImageResponse save(PropertyImageRequest request) {
        PropertyImage propertyImage = mapper.toEntity(request);
        PropertyImage saved = repository.save(propertyImage);
        return mapper.toDto(saved);
    }

    @Override
    public void delete(Long id) throws IOException {
        PropertyImage image = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Property image not found"));
        if(imageService.deleteImage(image.getImageUrl()).equals("Success")){
            repository.delete(image);
        }
    }
}