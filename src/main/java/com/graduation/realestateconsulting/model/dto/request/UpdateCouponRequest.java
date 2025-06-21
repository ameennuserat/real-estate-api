package com.graduation.realestateconsulting.model.dto.request;

import lombok.Data;

import java.time.LocalDate;
import java.util.Optional;

@Data
public class UpdateCouponRequest {
    private Optional<String> description;
    private Optional<Boolean> isActive;
    private Optional<Long> maxUses;
}
