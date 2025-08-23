package com.graduation.realestateconsulting.model.entity;

import com.graduation.realestateconsulting.model.enums.EventType;
import com.graduation.realestateconsulting.model.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Table(name = "session-activity-log")
@Entity
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SessionActivityLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "booking_id", nullable = false)
    private Long bookingId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_in_session", nullable = false)
    private Role roleInSession;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false)
    private EventType eventType;

    @Column(name = "event_timestamp", nullable = false)
    private Instant eventTimestamp;
}
