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
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class OfficeServiceImpl implements OfficeService {

    private final OfficeRepository repository;
    private final OfficeMapper mapper;
    private final ImageService imageService;
    private final CacheManager cacheManager;


    @Override
    public Page<OfficeResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toDto);
    }

    @Cacheable(value = "officesByStatus", key = "#status")
    @Override
    public List<OfficeResponse> findAllByUserStatus(UserStatus status) {
        return mapper.toDtos(repository.findAllByUserStatus((status)));
    }

    @Cacheable("offices_top_rated")
    @Override
    public List<OfficeResponse> findTop20Rated() {
        return mapper.toDtos(repository.findTop20ByAverageRating(PageRequest.of(0, 20)));
    }


    @Cacheable(value = "offices", key = "#id")
    @Override
    public OfficeResponse findById(Long id) {
        return repository.findById(id).map(mapper::toDto).orElseThrow(() -> new IllegalArgumentException("Office not found"));
    }

    @Cacheable(value = "myOffice", key = "T(org.springframework.security.core.context.SecurityContextHolder).getContext().getAuthentication().name")
    @Override
    public OfficeResponse getMe() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();

        return repository.findByUserId(user.getId()).map(mapper::toDto).orElseThrow(() -> new IllegalArgumentException("Office not found"));
    }


    @Caching(
            put = { @CachePut(value = "offices", key = "#result.id") },
            evict = {
                    @CacheEvict(value = "myOffice", key = "T(org.springframework.security.core.context.SecurityContextHolder).getContext().getAuthentication().name"),
                    @CacheEvict(value = "officesByStatus", allEntries = true),
                    @CacheEvict(value = "offices_top_rated", allEntries = true)
            }
    )
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

        Cache officesCache = cacheManager.getCache("offices");
        Cache myOfficeCache = cacheManager.getCache("myOffice");
        Cache officesByStatusCache = cacheManager.getCache("officesByStatus");
        Cache officesTopRatedCache = cacheManager.getCache("offices_top_rated");

        if (officesCache != null) {
            officesCache.evict(office.getId());
        }
        if (myOfficeCache != null) {
            myOfficeCache.evict(user.getEmail());
        }
        if (officesByStatusCache != null) {
            officesByStatusCache.clear();
        }
        if (officesTopRatedCache != null) {
            officesTopRatedCache.clear();
        }
    }

    @Override
    public Page<OfficeResponse> filterOffice(Specification<Office> officeSpecification, Pageable pageable) {
        return repository.findAll(officeSpecification, pageable).map(mapper::toDto);
    }


}