package com.graduation.realestateconsulting.observer.listiners;

import com.graduation.realestateconsulting.model.dto.request.NotificationRequest;
import com.graduation.realestateconsulting.model.entity.Expert;
import com.graduation.realestateconsulting.model.entity.User;
import com.graduation.realestateconsulting.model.enums.Role;
import com.graduation.realestateconsulting.model.enums.UserStatus;
import com.graduation.realestateconsulting.observer.events.CreateExpertEvent;
import com.graduation.realestateconsulting.repository.ExpertRepository;
import com.graduation.realestateconsulting.repository.UserRepository;
import com.graduation.realestateconsulting.services.ImageService;
import com.graduation.realestateconsulting.services.NotificationService;
import com.graduation.realestateconsulting.services.StripeConnectService;
import com.stripe.exception.StripeException;
import com.stripe.model.Account;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateExpertListener {
    private final ExpertRepository expertRepository;
    private final UserRepository userRepository;
    private final ImageService imageService;
    private final NotificationService notificationService;
    private final StripeConnectService stripeConnectService;

    @Async
    @Transactional
    @EventListener
    public void createExpertListener(CreateExpertEvent event) throws IOException {
        if (event.getRegisterRequest().getIdCardImage() == null || event.getRegisterRequest().getDegreeCertificateImage() == null) {
            throw new IllegalArgumentException("must id card and degree certificate image");
        }
        String idCardImage = imageService.uploadImage(event.getIdCardImage());
        String degreeCertificateImage = imageService.uploadImage(event.getDegreeCertificateImage());
        event.getUser().setStatus(UserStatus.PENDING);
        userRepository.save(event.getUser());
        Expert expert = Expert.builder()
                .user(event.getUser())
                .bio(event.getRegisterRequest().getBio())
                .experience(event.getRegisterRequest().getExperience())
                .profession(event.getRegisterRequest().getProfession())
                .totalRate(5)
                .rateCount(1)
                .idCardImage(idCardImage)
                .degreeCertificateImage(degreeCertificateImage)
                .followersCount(0)
                .favoritesCount(0)
                .build();
        log.info("Created Expert: {}", expert.getProfession());
        expertRepository.save(expert);
        try {
            Account stripeAccount = stripeConnectService.createAndVerifyCustomAccount(expert.getUser());

            expert.setStripeAccountId(stripeAccount.getId());
            expertRepository.save(expert);

            System.out.println("Successfully created Stripe Connected Account: " + stripeAccount.getId());

        } catch (StripeException e) {
            throw new RuntimeException("Could not create Stripe account for the expert: " + e.getMessage(), e);
        }

        List<User> users = userRepository.findAllByRole(Role.ADMIN);
        for (User user : users) {
            NotificationRequest notificationRequest = NotificationRequest.builder()
                    .title("Create Expert")
                    .message("A new expert has been registered and is awaiting your approval")
                    .user(user)
                    .build();
            notificationService.createAndSendNotification(notificationRequest);
        }
    }
}
