package com.graduation.realestateconsulting.filter.dto;

import com.graduation.realestateconsulting.filter.enums.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FilterItemRequestDto {

    @Schema(defaultValue = "firstName")
    private String column;

    @Schema(defaultValue = "John")
    private String value;

    @Schema(defaultValue = "EQUAL")
    private Operation operation;

    @Schema(defaultValue = "address")
    private String joinTable;
}