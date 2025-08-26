package com.graduation.realestateconsulting.services.implement;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.graduation.realestateconsulting.model.entity.Booking;
import com.graduation.realestateconsulting.model.entity.SessionActivityLog;
import com.graduation.realestateconsulting.model.entity.User;
import com.graduation.realestateconsulting.model.enums.EventType;
import com.graduation.realestateconsulting.model.enums.Role;
import com.graduation.realestateconsulting.repository.BookingRepository;
import com.graduation.realestateconsulting.repository.SessionActivityLogRepository;
import com.graduation.realestateconsulting.repository.UserRepository;
import com.graduation.realestateconsulting.services.ZegoCallbackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ZegoCallbackServiceImpl implements ZegoCallbackService {

    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final SessionActivityLogRepository activityLogRepository;

    @Override
    public void processZegoCallback(String rawPayload) throws Exception {
        Map<String, Object> payloadMap = objectMapper.readValue(rawPayload, new TypeReference<>() {
        });

        String event = (String) payloadMap.get("event");
        log.info("Received ZegoCloud event: {}", event);

        switch (event) {
            case "room_user_login":
                handleUserLogin(payloadMap);
                break;

            case "room_user_logout":
                handleUserLogout(payloadMap);
                break;

            default:
                log.warn("Unhandled ZegoCloud event type: {}", event);
        }
    }


    private void handleUserLogin(Map<String, Object> payloadMap) {
        processUserEvent(payloadMap, EventType.JOIN);
    }


    private void handleUserLogout(Map<String, Object> payloadMap) {
        processUserEvent(payloadMap, EventType.LEAVE);
    }


    private void processUserEvent(Map<String, Object> payloadMap, EventType eventType) {
        Long bookingId = Long.parseLong((String) payloadMap.get("room_id"));

        List<Map<String, Object>> userList = (List<Map<String, Object>>) payloadMap.get("user_list");

        if (userList != null && !userList.isEmpty()) {
            for (Map<String, Object> user : userList) {
                Long userId = Long.parseLong((String) user.get("user_id"));
                Role role = null;

                Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new RuntimeException("Booking not found"));
                if(booking.getClient().getId().equals(userId)) {
                    role = Role.USER;
                } else if (booking.getExpert().getUser().getId().equals(userId)) {
                    role = Role.EXPERT;
                }

                SessionActivityLog logEntry = SessionActivityLog.builder()
                        .bookingId(bookingId)
                        .userId(userId)
                        .roleInSession(role)
                        .eventType(eventType)
                        .eventTimestamp(Instant.ofEpochSecond(((Number) payloadMap.get("timestamp")).longValue()))
                        .build();

                activityLogRepository.save(logEntry);
                log.info("Saved {} event for user {} with role {} in booking {}", eventType, userId, role, bookingId);
            }
        }
    }
}