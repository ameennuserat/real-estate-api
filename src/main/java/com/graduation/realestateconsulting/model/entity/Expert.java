package com.graduation.realestateconsulting.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Expert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id", unique = true)
    private User user2;

    private String profession;

    private String experience;

    private double rating;

    private String bio;

    @Column(name = "profile_image")
    private String profileImage;

    @NotBlank(message =  "IdCardImage must not be blank")
    @Column(name = "id_card_image")
    private String idCardImage;

    @NotBlank(message =  "DegreeCertificateImage must not be blank")
    @Column(name = "degree_certificate_image")
    private String degreeCertificateImage;

}