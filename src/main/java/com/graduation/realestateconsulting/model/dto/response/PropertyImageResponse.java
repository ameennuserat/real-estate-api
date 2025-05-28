package com.graduation.realestateconsulting.model.dto.response;

import com.graduation.realestateconsulting.model.enums.PropertyImageType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PropertyImageResponse {

    private Long id;
    private String imageUrl;
    private PropertyImageType type;

}
