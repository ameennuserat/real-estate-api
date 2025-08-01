package com.graduation.realestateconsulting.observer.listiners;

import com.graduation.realestateconsulting.observer.events.GmailNotificationEvent;
import com.graduation.realestateconsulting.trait.SendEmailMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class GmailNotificationListener {
    private final SendEmailMessage sendEmailMessage;

    @Async
    @EventListener
    public void handleGmailEvent(GmailNotificationEvent event) {
        sendEmailMessage.handleEvent(event.getSentEmailMessageRequest());
    }
}