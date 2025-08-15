package com.graduation.realestateconsulting.services.implement;

import com.graduation.realestateconsulting.model.dto.request.NotificationRequest;
import com.graduation.realestateconsulting.model.dto.request.SentEmailMessageRequest;
import com.graduation.realestateconsulting.model.entity.User;
import com.graduation.realestateconsulting.model.enums.Role;
import com.graduation.realestateconsulting.observer.events.GmailNotificationEvent;
import com.graduation.realestateconsulting.repository.UserRepository;
import com.graduation.realestateconsulting.services.NotificationService;
import com.graduation.realestateconsulting.services.UserManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserManagementServiceImpl implements UserManagementService {
    private final UserRepository userRepository;
    private final ApplicationEventPublisher publisher;
    private final NotificationService notificationService;

    @Override
    @Transactional
    public void blockUser(Long userIdToBlock, int days) {

        User userToBlock = userRepository.findById(userIdToBlock)
                .orElseThrow(() -> new IllegalArgumentException("User to block not found with ID: " + userIdToBlock));

        LocalDateTime blockExpiration = LocalDateTime.now().plusDays(days);

        userToBlock.setBlocked(true);
        userToBlock.setBlockExpiresAt(blockExpiration);
        userToBlock.setBlocksCount(userToBlock.getBlocksCount() + 1);

        userRepository.save(userToBlock);

        // to all Admins
        String adminTitle = "User Blocked";
        String adminMessage = String.format(
                "An administrator has blocked the user %s (%s) for 15 days.",
                userToBlock.getFirstName(),
                userToBlock.getEmail()
        );
        List<User> admins = userRepository.findAllByRole(Role.ADMIN);
        for (User user : admins) {
            NotificationRequest notificationRequest = NotificationRequest.builder()
                    .title(adminTitle)
                    .message(adminMessage)
                    .user(user)
                    .build();
            notificationService.createAndSendNotification(notificationRequest);
        }

        // to user by email
        String userBlockedSubject = "Account Blocked";
        String userBlockedBody = String.format(
                "Dear %s,\n\nWe regret to inform you that your account has been temporarily blocked for 15 days due to a violation of our platform guidelines.\nThe block will expire on: %s",
                userToBlock.getFirstName(),
                LocalDateTime.now().plusDays(15).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        );
        SentEmailMessageRequest request = SentEmailMessageRequest.builder().to(userToBlock.getEmail()).subject(userBlockedSubject).body(userBlockedBody).build();
        publisher.publishEvent(new GmailNotificationEvent(this, request));

    }

    @Override
    @Transactional
    public void warnUser(Long userIdToWarn) {
        User userToWarn = userRepository.findById(userIdToWarn)
                .orElseThrow(() -> new IllegalArgumentException("User to warn not found with ID: " + userIdToWarn));

        userToWarn.setWarnsCount(userToWarn.getWarnsCount() + 1);
        userRepository.save(userToWarn);

        String subject = "Account Warning";
        String body = String.format(
                "Dear %s,\n\nThis is a warning regarding your recent activity which violates our platform's policies. Please review our terms of use to avoid future actions that could lead to your account being blocked.\n\nSincerely,\nThe Support Team",
                userToWarn.getFirstName() + " " + userToWarn.getLastName()
        );
        SentEmailMessageRequest emailRequest = new SentEmailMessageRequest(userToWarn.getEmail(), subject, body);
        publisher.publishEvent(new GmailNotificationEvent(this, emailRequest));

        // realtime notification
        String title = "Account Warning";
        String message = "This is a warning regarding your recent activity which violates our platform's policies.";

        notificationService.createAndSendNotification(NotificationRequest.builder()
                .user(userToWarn)
                .title(title)
                .message(message)
                .build());
    }
}