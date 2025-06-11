package com.graduation.realestateconsulting.model.mapper;


import com.graduation.realestateconsulting.model.dto.request.AvailabilityExceptionRequest;
import com.graduation.realestateconsulting.model.dto.request.UpdateAvailabilityExceptionRequest;
import com.graduation.realestateconsulting.model.dto.response.AvailabilityExceptionResponse;
import com.graduation.realestateconsulting.model.entity.AvailabilityExceptions;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper
public interface AvailabilityExceptionMapper {
    AvailabilityExceptions toEntity(AvailabilityExceptionRequest availabilityExceptionRequest);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(UpdateAvailabilityExceptionRequest dto, @MappingTarget AvailabilityExceptions entity);

    AvailabilityExceptionResponse toDto(AvailabilityExceptions availabilityExceptions);

    List<AvailabilityExceptionResponse> toDtos(List<AvailabilityExceptions> availabilityExceptions);
}
