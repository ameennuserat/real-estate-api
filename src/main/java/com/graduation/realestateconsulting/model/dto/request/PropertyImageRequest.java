package com.graduation.realestateconsulting.model.dto.request;

import com.graduation.realestateconsulting.model.enums.PropertyImageType;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
public class PropertyImageRequest {

    private Long propertyId;
    private MultipartFile imageUrl;
    private PropertyImageType type;

}
