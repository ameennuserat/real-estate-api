package com.graduation.realestateconsulting.model.dto.response;

import com.graduation.realestateconsulting.model.enums.Role;
import lombok.*;

@Data
@Builder
public class RegisterResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private Role role;
    private int warnsCount;
    private int blocksCount;
}