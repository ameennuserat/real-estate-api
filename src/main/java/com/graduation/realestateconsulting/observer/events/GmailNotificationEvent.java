package com.graduation.realestateconsulting.observer.events;

import com.graduation.realestateconsulting.model.dto.request.SentEmailMessageRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Service;


@Getter
public class GmailNotificationEvent extends ApplicationEvent {
    private final SentEmailMessageRequest sentEmailMessageRequest;
    public GmailNotificationEvent(Object source, SentEmailMessageRequest sentEmailMessageRequest1) {
        super(source);
        this.sentEmailMessageRequest = sentEmailMessageRequest1;
    }
}