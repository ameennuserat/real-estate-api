package com.graduation.realestateconsulting.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "rates")
public class Rate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double rate;

    @ManyToOne
    private Client client;

    @ManyToOne
    private Expert expert;

    @ManyToOne
    private Office office;

}
