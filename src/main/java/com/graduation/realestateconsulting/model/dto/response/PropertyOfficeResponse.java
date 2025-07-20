package com.graduation.realestateconsulting.model.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PropertyOfficeResponse {
    private Long id;
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String imageUrl;
}
