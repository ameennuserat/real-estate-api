package com.graduation.realestateconsulting.jobs;

import com.graduation.realestateconsulting.model.dto.request.SentEmailMessageRequest;
import com.graduation.realestateconsulting.model.entity.User;
import com.graduation.realestateconsulting.observer.events.GmailNotificationEvent;
import com.graduation.realestateconsulting.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserScheduledTasks {
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Scheduled(cron = "0 0 * * * *")
    @Transactional
    public void unblockExpiredUsers() {
        System.out.println("Running scheduled task to unblock users...");

        List<User> usersToUnblock = userRepository.findByIsBlockedTrueAndBlockExpiresAtBefore(LocalDateTime.now());

        if (!usersToUnblock.isEmpty()) {
            for (User user : usersToUnblock) {
                user.setBlocked(false);
                user.setBlockExpiresAt(null);
                String subject = "Your Account Has Been Unblocked";
                String body = String.format(
                        "Dear %s,\n\nThe temporary block on your account has expired. Your account is now fully active.\n\nYou can now log in and continue using our platform.\n\nSincerely,\nThe Support Team",
                        user.getFirstName()
                );

                SentEmailMessageRequest emailRequest = new SentEmailMessageRequest(user.getEmail(), subject, body);

                eventPublisher.publishEvent(new GmailNotificationEvent(this, emailRequest));
                System.out.println("Published unblock notification event for user: " + user.getEmail());
            }

            userRepository.saveAll(usersToUnblock);
            System.out.println("Successfully unblocked " + usersToUnblock.size() + " user(s).");
        } else {
            System.out.println("No users with expired blocks found.");
        }
    }
}
