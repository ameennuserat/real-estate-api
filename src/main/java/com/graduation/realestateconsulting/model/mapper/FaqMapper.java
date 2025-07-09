package com.graduation.realestateconsulting.model.mapper;

import com.graduation.realestateconsulting.model.dto.request.FaqRequest;
import com.graduation.realestateconsulting.model.dto.response.FaqResponse;
import com.graduation.realestateconsulting.model.entity.Faq;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper()
public interface FaqMapper {

    FaqResponse toDto(Faq entity);

    List<FaqResponse> toDtos(List<Faq> entities);

    Faq toEntity(FaqRequest request);
}
