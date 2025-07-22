package com.graduation.realestateconsulting.services.implement;

import com.graduation.realestateconsulting.config.JwtService;
import com.graduation.realestateconsulting.model.dto.request.UserImageRequest;
import com.graduation.realestateconsulting.model.dto.response.UserResponse;
import com.graduation.realestateconsulting.model.entity.User;
import com.graduation.realestateconsulting.model.enums.UserStatus;
import com.graduation.realestateconsulting.model.mapper.UserMapper;
import com.graduation.realestateconsulting.repository.UserRepository;
import com.graduation.realestateconsulting.services.ImageService;
import com.graduation.realestateconsulting.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final ImageService imageService;

    private final UserMapper mapper;

    @Override
    public void uploadImage(UserImageRequest request) throws IOException {
        User user = userRepository.findByEmail(jwtService.getCurrentUserName()).orElseThrow(() ->
                new IllegalArgumentException("user not found"));

        String imageUrl = imageService.uploadImage(request.getImage());

        user.setImageUrl(imageUrl);

        userRepository.save(user);
    }

    @Override
    public UserResponse getMe() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User currentUser = (User) authentication.getPrincipal();

        return mapper.toDto(currentUser);
    }

    @Override
    public UserResponse updateStatus(Long id, UserStatus status) {
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()) {
            user.get().setStatus(status);
            userRepository.save(user.get());
            return mapper.toDto(user.get());
        }
        throw new IllegalArgumentException("user not found");
    }

    @Override
    public void deleteById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("user not found"));
        userRepository.delete(user);
    }

    @Override
    public Page<UserResponse> filterUser(Specification<User> userSpecification, Pageable pageable) {
        return userRepository.findAll(userSpecification, pageable).map(mapper::toDto);
    }
}