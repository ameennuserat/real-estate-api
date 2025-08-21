package com.graduation.realestateconsulting.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.security.Timestamp;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "booking_feedbacks")
public class BookingFeedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private  int rating;

    private String review;

    @OneToOne
    @JoinColumn(name = "booking_id",referencedColumnName = "id")
    private Booking booking;

    @ManyToOne
    @JoinColumn(name = "rated_by",referencedColumnName = "id")
    private User client;

    @CreationTimestamp
    private LocalDateTime created_at;

}
