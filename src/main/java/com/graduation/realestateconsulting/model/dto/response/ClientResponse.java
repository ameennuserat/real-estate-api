package com.graduation.realestateconsulting.model.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ClientResponse {

    private Long id;
    private UserResponse user;

    private List<Integer> favorites;
    private List<Integer> followers;

}