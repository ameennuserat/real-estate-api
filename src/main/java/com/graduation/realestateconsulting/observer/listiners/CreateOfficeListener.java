package com.graduation.realestateconsulting.observer.listiners;

import com.graduation.realestateconsulting.model.entity.Office;
import com.graduation.realestateconsulting.model.enums.UserStatus;
import com.graduation.realestateconsulting.observer.events.CreateOfficeEvent;
import com.graduation.realestateconsulting.repository.OfficeRepository;
import com.graduation.realestateconsulting.repository.UserRepository;
import com.graduation.realestateconsulting.services.ImageService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class CreateOfficeListener {
    private final OfficeRepository officeRepository;
    private final ImageService imageService;
    private final UserRepository userRepository;

    @Transactional
    @EventListener
    public void onCreateOfficeEvent(CreateOfficeEvent event) throws IOException {
        if (event.getRegisterRequest().getCommercialRegisterImage() == null) {
            throw new IllegalArgumentException("must provide a commercial register image for office creation");
        }
        String url = imageService.uploadImage(event.getFile());
        event.getUser().setStatus(UserStatus.PENDING);
        userRepository.save(event.getUser());
        Office office = Office.builder()
                .user1(event.getUser())
                .bio(event.getRegisterRequest().getBio())
                .commercialRegisterImage(url)
                .location(event.getRegisterRequest().getLocation())
                .latitude(event.getRegisterRequest().getLatitude())
                .longitude(event.getRegisterRequest().getLongitude())
                .build();
        officeRepository.save(office);
    }
}
