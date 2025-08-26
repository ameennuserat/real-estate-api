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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpertServiceImpl implements ExpertService{

    private final ExpertRepository repository;
    private final ExpertMapper mapper;
    private final ImageService imageService;


    @Override
    public Page<ExpertResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toDto);
    }

    @Override
    public List<ExpertResponse> findAllByUserStatus(UserStatus status) {
        return mapper.toDtos(repository.findAllByUserStatus(status));
    }

    @Override
    public List<ExpertResponse> findTop20Rated() {
        return mapper.toDtos(repository.findTop20ByAverageRating(PageRequest.of(0,20)));
    }

    @Override
    public ExpertResponse findById(Long id) {
        return repository.findById(id).map(mapper::toDto).orElseThrow(() -> new IllegalArgumentException("Expert not found"));
    }

    @Override
    public ExpertResponse getMe() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();

        return repository.findByUserId(user.getId()).map(mapper::toDto).orElseThrow(() -> new IllegalArgumentException("Expert not found"));
    }

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