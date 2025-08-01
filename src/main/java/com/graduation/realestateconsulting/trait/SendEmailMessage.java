package com.graduation.realestateconsulting.trait;

import com.graduation.realestateconsulting.model.dto.request.SentEmailMessageRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@RequiredArgsConstructor
@Service
public class SendEmailMessage {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;

    @Value("${spring.mail.properties.mail.from.name}")
    private String senderName;

    public void handleEvent(SentEmailMessageRequest sentEmailMessageRequest) {
        try {

            MimeMessage mimeMessage = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");

            helper.setFrom(senderEmail, senderName);

            helper.setTo(sentEmailMessageRequest.getTo());
            helper.setSubject(sentEmailMessageRequest.getSubject());
            helper.setText(sentEmailMessageRequest.getBody());

            mailSender.send(mimeMessage);

        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }
}