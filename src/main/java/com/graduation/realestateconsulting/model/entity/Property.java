package com.graduation.realestateconsulting.model.entity;

import com.graduation.realestateconsulting.model.enums.HouseType;
import com.graduation.realestateconsulting.model.enums.ServiceType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "property")
public class Property{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "office_id")
    private Office office;

    private String description;

    @Enumerated(EnumType.STRING)
    private HouseType houseType;

    @Enumerated(EnumType.STRING)
    private ServiceType serviceType;

    private String location;
    private String direction;

    private double price;
    private double priceInMonth;
    private double area;
    private int numberOfBed;
    private int numberOfRooms;
    private int numberOfBathrooms;
    private double latitude;
    private double longitude;

    @OneToMany(mappedBy = "property",cascade = CascadeType.REMOVE)
    private List<PropertyImage> propertyImageList;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

}