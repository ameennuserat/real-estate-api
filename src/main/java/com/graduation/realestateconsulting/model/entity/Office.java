package com.graduation.realestateconsulting.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Office {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id", unique = true)
    private User user1;

    @Column(name = "image_url")
    private String imageUrl;

    private String bio;

    private String location;

    private double latitude;

    private double longitude;

//    @NotEmpty(message =  "CommercialRegisterImage must not be Empty")
//    @NotNull(message =  "CommercialRegisterImage must not be Empty")
    @NotBlank(message =  "CommercialRegisterImage must not be Blank")
    @Column(name = "commercial_register_image")
    private String commercialRegisterImage;

}