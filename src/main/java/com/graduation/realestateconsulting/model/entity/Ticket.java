package com.graduation.realestateconsulting.model.entity;

import com.graduation.realestateconsulting.model.enums.HouseType;
import com.graduation.realestateconsulting.model.enums.ServiceType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ticket")
public class Ticket{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    private String description;

    @Enumerated(EnumType.STRING)
    private HouseType houseType;

    @Enumerated(EnumType.STRING)
    private ServiceType serviceType;

    private String location;
    private String direction;
    private double lowPrice;
    private double highPrice;
    private double area;
    private int numberOfBed;
    private int numberOfRooms;
    private int numberOfBathrooms;

    private LocalDateTime dateTime;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}