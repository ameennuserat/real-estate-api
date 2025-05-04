package com.graduation.realestateconsulting.trait;

import com.graduation.realestateconsulting.model.dto.request.SentEmailMessageRequest;
import lombok.RequiredArgsConstructor;
import org.eclipse.angus.mail.util.MailConnectException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SendEmailMessage {
    private final JavaMailSender mailSender;

    public void handleEvent(SentEmailMessageRequest sentEmailMessageRequest) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("real_estate");
            message.setTo(sentEmailMessageRequest.getTo());
            message.setSubject(sentEmailMessageRequest.getSubject());
            message.setText(sentEmailMessageRequest.getBody());
            mailSender.send(message);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}