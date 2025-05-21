package com.graduation.realestateconsulting.observer.listiners;

import com.graduation.realestateconsulting.model.entity.Expert;
import com.graduation.realestateconsulting.model.enums.UserStatus;
import com.graduation.realestateconsulting.observer.events.CreateExpertEvent;
import com.graduation.realestateconsulting.repository.ExpertRepository;
import com.graduation.realestateconsulting.repository.UserRepository;
import com.graduation.realestateconsulting.services.ImageService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateExpertListener {
    private final ExpertRepository expertRepository;
    private final UserRepository userRepository;
    private final ImageService imageService;

    @Transactional
    @EventListener
    public void createExpertListener(CreateExpertEvent event) throws IOException {
        if(event.getRegisterRequest().getIdCardImage() == null || event.getRegisterRequest().getDegreeCertificateImage() == null) {
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
                .build();
        log.info("Created Expert: {}", expert.getProfession());
        expertRepository.save(expert);
    }
}
