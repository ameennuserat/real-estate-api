package com.graduation.realestateconsulting.observer.events;

import com.graduation.realestateconsulting.model.entity.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;


@Getter
public class CreateClientEvent extends ApplicationEvent {

    private final User user;
    public CreateClientEvent(Object source, User user) {
        super(source);
        this.user = user;
    }
}
