package com.graduation.realestateconsulting.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "faq")
public class Faq {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String question;

    private String answer;

    @ManyToOne
    @JoinColumn(name = "faq_category_id")
    private FaqCategory faqCategory;

    @CreationTimestamp
    private LocalDateTime createdAt;

}