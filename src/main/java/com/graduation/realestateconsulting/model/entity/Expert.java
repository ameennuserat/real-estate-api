package com.graduation.realestateconsulting.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

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
    private User user;

    private String profession;

    private String experience;

    private double totalRate;

    private double rateCount;

    private String bio;

    @NotBlank(message =  "IdCardImage must not be blank")
    @Column(name = "id_card_image")
    private String idCardImage;

    @NotBlank(message =  "DegreeCertificateImage must not be blank")
    @Column(name = "degree_certificate_image")
    private String degreeCertificateImage;

    @OneToMany(mappedBy = "expert", cascade = CascadeType.ALL)
    private List<WorkingTimes> workingHours;
}