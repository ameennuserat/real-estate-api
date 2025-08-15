package com.graduation.realestateconsulting.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginResponse {
    private String token;
    private String refreshToken;
    private ExpertResponse expert;
    private ClientResponse client;
    private OfficeResponse office;
}
