package com.graduation.realestateconsulting.model.mapper;

import com.graduation.realestateconsulting.model.dto.request.OfficeRequest;
import com.graduation.realestateconsulting.model.dto.response.OfficeResponse;
import com.graduation.realestateconsulting.model.entity.Office;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(uses = {UserMapper.class,AppMapper.class})
public interface OfficeMapper {

    @Mapping(target = "commercialRegisterImage" , source = "commercialRegisterImage", qualifiedByName = "addPrefixToImageUrl")
    OfficeResponse toDto(Office entity);
    List<OfficeResponse> toDtos(List<Office> entities);

    void toEntity(@MappingTarget Office entity, OfficeRequest request);
}
