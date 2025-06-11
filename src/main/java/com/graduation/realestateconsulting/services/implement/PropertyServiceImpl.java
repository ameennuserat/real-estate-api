package com.graduation.realestateconsulting.services.implement;

import com.graduation.realestateconsulting.model.dto.request.PropertyRequest;
import com.graduation.realestateconsulting.model.dto.response.PropertyResponse;
import com.graduation.realestateconsulting.model.entity.Property;
import com.graduation.realestateconsulting.model.entity.PropertyImage;
import com.graduation.realestateconsulting.model.mapper.PropertyImageMapper;
import com.graduation.realestateconsulting.model.mapper.PropertyMapper;
import com.graduation.realestateconsulting.repository.PropertyRepository;
import com.graduation.realestateconsulting.services.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.graduation.realestateconsulting.services.PropertyService;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class PropertyServiceImpl implements PropertyService {

    private final PropertyRepository repository;
    private final PropertyMapper mapper;

    private final ImageService imageService;

    @Override
    public Page<PropertyResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toDto);
    }

    @Override
    public Page<PropertyResponse> findAllByOfficeId(Pageable pageable, Long officeId) {
        return repository.findAllByOfficeId(pageable, officeId).map(mapper::toDto);
    }

    @Override
    public PropertyResponse findById(Long id) {
        return repository.findById(id).map(mapper::toDto).orElseThrow(() -> new IllegalArgumentException("Property not found"));
    }

    @Override
    public PropertyResponse save(PropertyRequest request) {

        Property property = mapper.toEntity(request);
        Property saved = repository.save(property);
        return mapper.toDto(saved);
    }

    @Override
    public PropertyResponse update(Long id, PropertyRequest request) {
        Property property = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Property not found"));
        mapper.toEntity(property, request);
        Property saved = repository.save(property);
        return mapper.toDto(saved);
    }

    @Override
    public void delete(Long id) throws IOException {

        Property property = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Property not found"));
        for (PropertyImage image : property.getPropertyImageList()) {
            imageService.deleteImage(image.getImageUrl());
        }
        repository.delete(property);
    }
}