package com.graduation.realestateconsulting.model.mapper;

import com.graduation.realestateconsulting.model.dto.request.ExpertRequest;
import com.graduation.realestateconsulting.model.dto.request.OfficeRequest;
import com.graduation.realestateconsulting.model.dto.response.ExpertResponse;
import com.graduation.realestateconsulting.model.dto.response.OfficeResponse;
import com.graduation.realestateconsulting.model.entity.Expert;
import com.graduation.realestateconsulting.model.entity.Office;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(uses = {UserMapper.class,AppMapper.class})
public interface ExpertMapper {

    @Mapping(target = "idCardImage" , source = "idCardImage", qualifiedByName = "addPrefixToImageUrl")
    @Mapping(target = "degreeCertificateImage" , source = "degreeCertificateImage", qualifiedByName = "addPrefixToImageUrl")
    @Mapping(target = "rating" ,expression = "java(entity.getTotalRate() / entity.getRateCount())")
    ExpertResponse toDto(Expert entity);
    List<ExpertResponse> toDtos(List<Expert> entities);

    void toEntity(@MappingTarget Expert entity, ExpertRequest request);
}
