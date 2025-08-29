package com.graduation.realestateconsulting.model.dto.response;

import com.graduation.realestateconsulting.model.enums.PropertyImageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PropertyImageResponse {

    private Long id;
    private String imageUrl;
    private PropertyImageType type;

}
