package com.graduation.realestateconsulting.filter.dto;

import com.graduation.realestateconsulting.filter.enums.GlobalOperator;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FilterRequestDto {

    private List<FilterItemRequestDto> filterItems;

    private GlobalOperator globalOperator;

    private PageRequestDto pageRequest;

}