package com.graduation.realestateconsulting.model.dto.response;

import com.graduation.realestateconsulting.model.enums.DiscountType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
public record CouponResponse(
        Long id,

        String code,

        String description,

        DiscountType discountType,

        BigDecimal discountValue,

        LocalDateTime expirationDate,

        Long maxUses,

        Long timesUsed,

        boolean isActive,

//        Integer requiredSessions,

        Long expertId,

        String expertName,

        LocalDateTime createdAt

){}
