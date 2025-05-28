package com.graduation.realestateconsulting.model.dto.request;

import com.graduation.realestateconsulting.model.enums.HouseType;
import com.graduation.realestateconsulting.model.enums.ServiceType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PropertyRequest {

    private Long OfficeId;

    private String description;
    private HouseType houseType;
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

}