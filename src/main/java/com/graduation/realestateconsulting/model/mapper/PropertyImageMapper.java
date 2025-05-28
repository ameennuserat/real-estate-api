package com.graduation.realestateconsulting.model.mapper;

import com.graduation.realestateconsulting.model.dto.request.PropertyImageRequest;
import com.graduation.realestateconsulting.model.dto.response.PropertyImageResponse;
import com.graduation.realestateconsulting.model.entity.PropertyImage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(uses = {AppMapper.class})
public interface PropertyImageMapper {

    @Mapping(target = "imageUrl",source = "imageUrl",qualifiedByName = "addPrefixToImageUrl")
    PropertyImageResponse toDto(PropertyImage entity);

    List<PropertyImageResponse> toDtos(List<PropertyImage> entities);


    @Mapping(target ="imageUrl",source = "imageUrl" ,qualifiedByName = "uploadImage")
    @Mapping(target = "property", source = "propertyId" , qualifiedByName = "getPropertyById")
    PropertyImage toEntity(PropertyImageRequest request);

}
