package com.graduation.realestateconsulting.model.mapper;

import com.graduation.realestateconsulting.model.dto.request.PropertyRequest;
import com.graduation.realestateconsulting.model.dto.response.PropertyResponse;
import com.graduation.realestateconsulting.model.entity.Property;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(uses = {AppMapper.class,OfficeMapper.class,PropertyImageMapper.class})
public interface PropertyMapper {

    PropertyResponse toDto(Property entity);
    List<PropertyResponse> toDtos(List<Property> entities);


    @Mapping(target ="office",source = "officeId" ,qualifiedByName = "getOfficeById")
    Property toEntity(PropertyRequest request);
    @Mapping(target ="office",source = "officeId" ,qualifiedByName = "getOfficeById")
    void toEntity(@MappingTarget Property entity,PropertyRequest request);

}
