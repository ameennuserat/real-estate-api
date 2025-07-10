package com.graduation.realestateconsulting.services.implement;

import com.graduation.realestateconsulting.model.dto.request.SentEmailMessageRequest;
import com.graduation.realestateconsulting.model.entity.User;
import com.graduation.realestateconsulting.observer.events.GmailNotificationEvent;
import com.graduation.realestateconsulting.repository.UserRepository;
import com.graduation.realestateconsulting.services.UserManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class UserManagementServiceImpl implements UserManagementService {
    private final UserRepository userRepository;
    private final ApplicationEventPublisher publisher;

    @Override
    @Transactional
    public void blockUser(Long userIdToBlock, int days) {
        User userToBlock = userRepository.findById(userIdToBlock)
                .orElseThrow(() -> new IllegalArgumentException("User to block not found with ID: " + userIdToBlock));

        LocalDateTime blockExpiration = LocalDateTime.now().plusDays(days);

        userToBlock.setBlocked(true);
        userToBlock.setBlockExpiresAt(blockExpiration);

        userRepository.save(userToBlock);

        // --- English Email Content ---
        String subject = "Account Blocked";
        String body = String.format(
                "Dear %s,\n\nWe regret to inform you that your account has been temporarily blocked for %d days due to a violation of our platform guidelines.\nThe block will expire on: %s\n\nSincerely,\nThe Support Team",
                userToBlock.getFirstName() + " " + userToBlock.getLastName(),
                days,
                blockExpiration.format(DateTimeFormatter.ofPattern("yyyy-MM-dd 'at' HH:mm"))
        );

        SentEmailMessageRequest emailRequest = new SentEmailMessageRequest(userToBlock.getEmail(), subject, body);
        publisher.publishEvent(new GmailNotificationEvent(this, emailRequest));
    }

    @Override
    public void warnUser(Long userIdToWarn) {
        User userToWarn = userRepository.findById(userIdToWarn)
                .orElseThrow(() -> new IllegalArgumentException("User to warn not found with ID: " + userIdToWarn));

        // --- English Email Content ---
        String subject = "Account Warning";
        String body = String.format(
                "Dear %s,\n\nThis is a warning regarding your recent activity which violates our platform's policies. Please review our terms of use to avoid future actions that could lead to your account being blocked.\n\nSincerely,\nThe Support Team",
                userToWarn.getFirstName() + " " + userToWarn.getLastName()
        );

        SentEmailMessageRequest emailRequest = new SentEmailMessageRequest(userToWarn.getEmail(), subject, body);
        publisher.publishEvent(new GmailNotificationEvent(this, emailRequest));
    }
}