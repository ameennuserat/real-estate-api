package com.graduation.realestateconsulting.model.dto.response;

import com.graduation.realestateconsulting.model.enums.DiscountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CouponResponse {
    private Long id;

    private String code;

    private String description;

    private DiscountType discountType;

    private BigDecimal discountValue;

    private LocalDateTime expirationDate;

    private Long maxUses;

    private Long timesUsed;

    private boolean isActive;

    private Long expertId;

    private String expertName;

    private LocalDateTime createdAt;

}
