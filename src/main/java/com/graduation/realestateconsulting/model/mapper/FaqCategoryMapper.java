package com.graduation.realestateconsulting.model.mapper;

import com.graduation.realestateconsulting.model.dto.request.FaqCategoryRequest;
import com.graduation.realestateconsulting.model.dto.response.FaqCategoryResponse;
import com.graduation.realestateconsulting.model.entity.FaqCategory;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper
public interface FaqCategoryMapper {

    FaqCategoryResponse toDto(FaqCategory entity);

    List<FaqCategoryResponse> toDtos(List<FaqCategory> entities);

    FaqCategory toEntity(FaqCategoryRequest request);

    void toEntity(@MappingTarget FaqCategory entity, FaqCategoryRequest request);
}
