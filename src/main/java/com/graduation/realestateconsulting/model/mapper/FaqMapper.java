package com.graduation.realestateconsulting.model.mapper;

import com.graduation.realestateconsulting.model.dto.request.FaqRequest;
import com.graduation.realestateconsulting.model.dto.response.FaqResponse;
import com.graduation.realestateconsulting.model.entity.Faq;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(uses = {AppMapper.class,FaqCategoryMapper.class})
public interface FaqMapper {

    FaqResponse toDto(Faq entity);

    List<FaqResponse> toDtos(List<Faq> entities);

    @Mapping(target = "faqCategory",source ="categoryId",qualifiedByName = "getFaqCategoryById")
    Faq toEntity(FaqRequest request);

    @Mapping(target = "faqCategory",source ="categoryId",qualifiedByName = "getFaqCategoryById")
    void toEntity(@MappingTarget Faq entity, FaqRequest request);
}
