package com.graduation.realestateconsulting.services.implement;

import com.graduation.realestateconsulting.model.dto.request.ExpertImageRequest;
import com.graduation.realestateconsulting.model.dto.request.ExpertRequest;
import com.graduation.realestateconsulting.model.dto.response.ExpertResponse;
import com.graduation.realestateconsulting.model.entity.Expert;
import com.graduation.realestateconsulting.model.entity.User;
import com.graduation.realestateconsulting.model.enums.UserStatus;
import com.graduation.realestateconsulting.model.mapper.ExpertMapper;
import com.graduation.realestateconsulting.repository.ExpertRepository;
import com.graduation.realestateconsulting.services.ExpertService;
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
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExpertServiceImpl implements ExpertService{

    private final ExpertRepository repository;
    private final ExpertMapper mapper;
    private final ImageService imageService;
    private final CacheManager cacheManager;

    @Override
    public Page<ExpertResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toDto);
    }

    @Override
    public List<ExpertResponse> findAllByUserStatus(UserStatus status) {
        return mapper.toDtos(repository.findAllByUserStatus(status));
    }

    @Cacheable(value = "top20")
    @Override
    public List<ExpertResponse> findTop20Rated() {
        return mapper.toDtos(repository.findTop20ByAverageRating(PageRequest.of(0,20)));
    }

    @Cacheable(value = "experts" , key = "#id")
    @Override
    public ExpertResponse findById(Long id) {
        return repository.findById(id).map(mapper::toDto).orElseThrow(() -> new IllegalArgumentException("Expert not found"));
    }


    @Cacheable(value = "myExpertProfile", key = "T(org.springframework.security.core.context.SecurityContextHolder).getContext().getAuthentication().name")
    @Override
    public ExpertResponse getMe() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();

        return repository.findByUserId(user.getId()).map(mapper::toDto).orElseThrow(() -> new IllegalArgumentException("Expert not found"));
    }

    @Caching(
            put = { @CachePut(value = "experts", key = "#result.id") },
            evict = {
                    @CacheEvict(value = "myExpertProfile", key = "T(org.springframework.security.core.context.SecurityContextHolder).getContext().getAuthentication().name"),
                    @CacheEvict(value = "top20", allEntries = true)
            }
    )
    @Override
    public ExpertResponse updateMe(ExpertRequest request) {
        // get user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();

        // get office
        Expert expert = repository.findByUserId(user.getId()).orElseThrow(() -> new IllegalArgumentException("Expert not found"));

        // update expert
        mapper.toEntity(expert, request);

        // save new expert
        Expert savedExpert = repository.save(expert);

        return  mapper.toDto(savedExpert);
    }


    @Transactional
    @Override
    public void uploadImage(ExpertImageRequest request) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();

        Expert expert = repository.findByUserId(user.getId()).orElseThrow(() -> new IllegalArgumentException("Expert not found"));

        if(request.getIdCardImage() != null) {
           // remove current image
            imageService.deleteImage(expert.getIdCardImage());
            String imageUrl = imageService.uploadImage(request.getIdCardImage());
            expert.setIdCardImage(imageUrl);
        }

        if(request.getDegreeCertificateImage() != null) {
           // remove current image
            imageService.deleteImage(expert.getDegreeCertificateImage());
            String imageUrl = imageService.uploadImage(request.getDegreeCertificateImage());
            expert.setDegreeCertificateImage(imageUrl);
        }

        repository.save(expert);

        log.info("Evicting caches for user {} after image upload.", user.getEmail());
        Cache expertsCache = cacheManager.getCache("experts");
        if (expertsCache != null) expertsCache.evict(expert.getId());

        Cache myExpertProfileCache = cacheManager.getCache("myExpertProfile");
        if (myExpertProfileCache != null) myExpertProfileCache.evict(user.getEmail());

        Cache top20Cache = cacheManager.getCache("top20");
        if (top20Cache != null) top20Cache.clear();

    }

    @Override
    public Page<ExpertResponse> filterExpert(Specification<Expert> expertSpecification, Pageable pageable) {
        return repository.findAll(expertSpecification, pageable).map(mapper::toDto);
    }

//    @Override
//    public void updateExpertFollowerCount(Long id, Integer value) {
//        Expert expert = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Expert not found"));
//        Integer count = expert.getFollowersCount() == null ? 0 : expert.getFollowersCount();
//        expert.setFollowersCount(count + value);
//        repository.save(expert);
//    }
//
//    @Override
//    public void updateExpertFavoriteCount(Long id, Integer value) {
//        Expert expert = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Expert not found"));
//        Integer count = expert.getFavoritesCount() == null ? 0 : expert.getFavoritesCount();
//        expert.setFavoritesCount(count + value);
//        repository.save(expert);
//    }


}