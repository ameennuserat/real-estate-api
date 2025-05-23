package com.graduation.realestateconsulting.model.dto.response;

import com.graduation.realestateconsulting.exceptions.ExceptionDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ExceptionResponse {
    private String status;
    private String message;
    private List<ExceptionDto> errors;
}