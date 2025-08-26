package com.graduation.realestateconsulting.services.implement;

import com.graduation.realestateconsulting.config.JwtService;
import com.graduation.realestateconsulting.model.dto.request.SentEmailMessageRequest;
import com.graduation.realestateconsulting.model.dto.request.UserImageRequest;
import com.graduation.realestateconsulting.model.dto.response.UserResponse;
import com.graduation.realestateconsulting.model.entity.User;
import com.graduation.realestateconsulting.model.enums.UserStatus;
import com.graduation.realestateconsulting.model.mapper.UserMapper;
import com.graduation.realestateconsulting.observer.events.GmailNotificationEvent;
import com.graduation.realestateconsulting.repository.UserRepository;
import com.graduation.realestateconsulting.services.ImageService;
import com.graduation.realestateconsulting.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
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
    private final ApplicationEventPublisher publisher;
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
        User user = userRepository.findById(id).orElseThrow(()->new IllegalArgumentException("user not found"));

            user.setStatus(status);
            userRepository.save(user);

            String subject = "";
            String body = "";

            switch (status) {
                case AVAILABLE:
                    subject = "Account Approved";
                    body = String.format("Dear %s,\n\nCongratulations! Your account on our platform has been approved. You can now log in and use all the features.\n\nSincerely,\nThe Support Team", user.getFirstName());
                    break;

                case LOCKED:
                    subject = "Account Application Update";
                    body = String.format("Dear %s,\n\nWe regret to inform you that your application to join our platform has been rejected at this time. Thank you for your interest.\n\nSincerely,\nThe Support Team", user.getFirstName());
                    break;

                case PENDING:
                    break;
            }


            if (!subject.isEmpty()) {
                SentEmailMessageRequest emailRequest = new SentEmailMessageRequest(user.getEmail(), subject, body);
                publisher.publishEvent(new GmailNotificationEvent(this, emailRequest));
            }

            return mapper.toDto(user);
    }

    @Override
    public void deleteById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("user not found"));
        String subject = "Account Deletion Confirmation";
        String body = String.format(
                "Dear %s,\n\nThis is to confirm that your account has been permanently deleted from our platform.\n\nThank you for being a part of our community.\n\nSincerely,\nThe Support Team",
                user.getFirstName()
        );
        SentEmailMessageRequest emailRequest = new SentEmailMessageRequest(user.getEmail(), subject, body);
        publisher.publishEvent(new GmailNotificationEvent(this, emailRequest));
        userRepository.delete(user);
    }

    @Override
    public Page<UserResponse> filterUser(Specification<User> userSpecification, Pageable pageable) {
        return userRepository.findAll(userSpecification, pageable).map(mapper::toDto);
    }
}