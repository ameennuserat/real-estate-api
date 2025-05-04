package com.graduation.realestateconsulting.observer.events;

import com.graduation.realestateconsulting.model.dto.request.RegisterRequest;
import com.graduation.realestateconsulting.model.entity.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class CreateExpertEvent extends ApplicationEvent {
    private final User user;
    private final MultipartFile idCardImage;
    private final MultipartFile degreeCertificateImage;
    private final RegisterRequest registerRequest;
    public CreateExpertEvent(Object source, RegisterRequest request, User user, MultipartFile idCardImage, MultipartFile degreeCertificateImage) {
        super(source);
        this.user = user;
        this.idCardImage = idCardImage;
        this.degreeCertificateImage = degreeCertificateImage;
        this.registerRequest = request;
    }
}
