package com.graduation.realestateconsulting.model.dto.response;

import com.graduation.realestateconsulting.model.enums.HouseType;
import com.graduation.realestateconsulting.model.enums.ServiceType;
import lombok.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PropertyResponse {


    private Long id;
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
    private int viewsCount;

    private List<PropertyImageResponse> propertyImageList;
    private PropertyOfficeResponse office;

}
