package com.graduation.realestateconsulting.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "faq_categories")
public class FaqCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "faqCategory", cascade = CascadeType.ALL)
    private List<Faq> faqs = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime createdAt;
}