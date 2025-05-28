package com.graduation.realestateconsulting.model.dto.request;

import com.graduation.realestateconsulting.model.enums.HouseType;
import com.graduation.realestateconsulting.model.enums.ServiceType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@Builder
public class TicketRequest {

    private Long clientId;

    private String description;
    private HouseType houseType;
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

}