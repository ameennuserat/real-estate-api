package com.graduation.realestateconsulting.observer.events;

import com.graduation.realestateconsulting.model.dto.request.RegisterRequest;
import com.graduation.realestateconsulting.model.entity.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class CreateOfficeEvent extends ApplicationEvent {
    private final User user;
    private final MultipartFile file;
    private final RegisterRequest registerRequest;
    public CreateOfficeEvent(Object source, RegisterRequest request, User user, MultipartFile commercialRegisterImage) {
        super(source);
        this.user = user;
        this.file = commercialRegisterImage;
        this.registerRequest = request;
    }
}
