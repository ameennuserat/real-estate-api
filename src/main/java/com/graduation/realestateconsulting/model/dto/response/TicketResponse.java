package com.graduation.realestateconsulting.model.dto.response;

import com.graduation.realestateconsulting.model.enums.HouseType;
import com.graduation.realestateconsulting.model.enums.ServiceType;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
public class TicketResponse {

    private Long id;

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

    private ClientResponse client;

}