package com.graduation.realestateconsulting.services.implement;

import com.graduation.realestateconsulting.model.dto.request.NotificationRequest;
import com.graduation.realestateconsulting.model.dto.request.PropertyRequest;
import com.graduation.realestateconsulting.model.dto.response.PropertyResponse;
import com.graduation.realestateconsulting.model.entity.Property;
import com.graduation.realestateconsulting.model.entity.PropertyImage;
import com.graduation.realestateconsulting.model.entity.User;
import com.graduation.realestateconsulting.model.enums.HouseType;
import com.graduation.realestateconsulting.model.enums.ServiceType;
import com.graduation.realestateconsulting.model.mapper.PropertyMapper;
import com.graduation.realestateconsulting.repository.PropertyRepository;
import com.graduation.realestateconsulting.services.ImageService;
import com.graduation.realestateconsulting.services.NotificationService;
import com.graduation.realestateconsulting.services.PropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PropertyServiceImpl implements PropertyService {

    private final PropertyRepository repository;
    private final PropertyMapper mapper;
    private final NotificationService notificationService;

    private final ImageService imageService;

    @Override
    public Page<PropertyResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toDto);
    }

    @Override
    public List<PropertyResponse> findByFilters(String name,Double lowPrice, Double highPrice, ServiceType serviceType, HouseType houseType, String lowArea, String highArea, String location) {
        return mapper.toDtos(repository.findByFilters(name,lowPrice, highPrice, serviceType, houseType, lowArea, highArea, location));
    }


    @Override
    public Page<PropertyResponse> findAllByOfficeId(Pageable pageable, Long officeId) {
        return repository.findAllByOfficeId(pageable, officeId).map(mapper::toDto);
    }

    @Cacheable(value = "properties_top_viewed")
    @Override
    public List<PropertyResponse> findTop20Viewed() {
        return mapper.toDtos(repository.findTop20ByOrderByViewsCountDesc());
    }

    @Transactional
    @CacheEvict(value = "properties_top_viewed", allEntries = true)
    @Override
    public PropertyResponse findById(Long id) {
        Property property = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Property not found"));
        property.setViewsCount(property.getViewsCount()+1);
        return mapper.toDto(repository.save(property));
    }

    @Transactional
    @Override
    public PropertyResponse save(PropertyRequest request) {

        Property property = mapper.toEntity(request);
        Property saved = repository.save(property);
        return mapper.toDto(saved);
    }

    @Transactional
    @CacheEvict(value = "properties_top_viewed", allEntries = true)
    @Override
    public PropertyResponse update(Long id, PropertyRequest request) {
        Property property = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Property not found"));
        mapper.toEntity(property, request);
        Property saved = repository.save(property);
        return mapper.toDto(saved);
    }

    @Transactional
    @CacheEvict(value = "properties_top_viewed", allEntries = true)
    @Override
    public void delete(Long id) throws IOException {

        Property property = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Property not found"));

        String title = "Your Property Has Been Removed";
        String message = "We're writing to inform you that your property listing '%s' has been removed by an administrator as it did not comply with our platform's guidelines.";

        User propertyOwnerUser = property.getOffice().getUser();

        try {
            NotificationRequest notificationRequest = NotificationRequest.builder()
                    .user(propertyOwnerUser)
                    .title(title)
                    .message(message)
                    .build();
            notificationService.createAndSendNotification(notificationRequest);
        } catch (Exception e) {
            System.err.println("Failed to send notification for property deletion: " + e.getMessage());
        }
        for (PropertyImage image : property.getPropertyImageList()) {
            imageService.deleteImage(image.getImageUrl());
        }
        repository.delete(property);
    }

    @Override
    public Page<PropertyResponse> filterProperty(Specification<Property> propertySpecification, Pageable pageable) {
        return repository.findAll(propertySpecification,pageable).map(mapper::toDto);
    }
}