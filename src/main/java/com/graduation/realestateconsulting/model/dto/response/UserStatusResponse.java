package com.graduation.realestateconsulting.model.dto.response;

import com.graduation.realestateconsulting.model.enums.UserStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserStatusResponse{
    private Long id;
    private UserStatus userStatus;
}
