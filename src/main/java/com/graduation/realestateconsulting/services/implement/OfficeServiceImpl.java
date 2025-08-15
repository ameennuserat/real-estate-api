package com.graduation.realestateconsulting.services.implement;

import com.graduation.realestateconsulting.model.dto.request.OfficeImageRequest;
import com.graduation.realestateconsulting.model.dto.request.OfficeRequest;
import com.graduation.realestateconsulting.model.dto.response.OfficeResponse;
import com.graduation.realestateconsulting.model.entity.Office;
import com.graduation.realestateconsulting.model.entity.User;
import com.graduation.realestateconsulting.model.enums.UserStatus;
import com.graduation.realestateconsulting.model.mapper.OfficeMapper;
import com.graduation.realestateconsulting.repository.OfficeRepository;
import com.graduation.realestateconsulting.services.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.graduation.realestateconsulting.services.OfficeService;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OfficeServiceImpl implements OfficeService {

    private final OfficeRepository repository;
    private final OfficeMapper mapper;
    private final ImageService imageService;


    @Override
    public Page<OfficeResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toDto);
    }

    @Override
    public List<OfficeResponse> findAllByUserStatus(UserStatus status) {
        return mapper.toDtos(repository.findAllByUserStatus((status)));
    }

    @Override
    public List<OfficeResponse> findTop20Rated() {
        return mapper.toDtos(repository.findTop20ByAverageRating(PageRequest.of(0, 20)));
    }

    @Override
    public OfficeResponse findById(Long id) {
        return repository.findById(id).map(mapper::toDto).orElseThrow(() -> new IllegalArgumentException("Office not found"));
    }

    @Override
    public OfficeResponse getMe() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();

        return repository.findByUserId(user.getId()).map(mapper::toDto).orElseThrow(() -> new IllegalArgumentException("Office not found"));
    }

    @Override
    public OfficeResponse updateMe(OfficeRequest request) {
        // get user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();

        // get office
        Office office = repository.findByUserId(user.getId()).orElseThrow(() -> new IllegalArgumentException("Office not found"));

        // update office
        mapper.toEntity(office, request);

        // save new office
        Office savedOffice = repository.save(office);

        return  mapper.toDto(savedOffice);
    }

    @Override
    public void uploadImage(OfficeImageRequest request) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();

        Office office = repository.findByUserId(user.getId()).orElseThrow(() -> new IllegalArgumentException("Office not found"));

        // remove current image
        imageService.deleteImage(office.getCommercialRegisterImage());

        String imageUrl = imageService.uploadImage(request.getImage());

        office.setCommercialRegisterImage(imageUrl);

        repository.save(office);
    }

    @Override
    public Page<OfficeResponse> filterOffice(Specification<Office> officeSpecification, Pageable pageable) {
        return repository.findAll(officeSpecification, pageable).map(mapper::toDto);
    }


}