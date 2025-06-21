package com.graduation.realestateconsulting.model.dto.request;

import com.graduation.realestateconsulting.model.enums.DiscountType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateCouponRequest(


        @NotBlank(message = "Coupon code cannot be blank")
        @Schema(description = "The code the customer will use. Must be unique.", example = "SUMMER25", required = true)
        String code,

        @Schema(description = "Optional description for the coupon for internal reference.", example = "25% off for the summer season", nullable = true)
        String description,

        @NotNull(message = "Discount type is required")
        @Schema(description = "The type of the discount: PERCENTAGE or FIXED_AMOUNT.", example = "PERCENTAGE", required = true)
        DiscountType discountType,

        @Schema(description = "The discount percentage. Required only if discountType is PERCENTAGE.", example = "25.5", nullable = true)
        BigDecimal percentageValue,

        @Schema(description = "The fixed discount amount. Required only if discountType is FIXED_AMOUNT.", example = "10.00", nullable = true)
        BigDecimal amountValue,

        @Schema(description = "Maximum number of times the coupon can be used. Leave empty or null for unlimited uses.", example = "100", nullable = true)
        Long maxUses,

        @Schema(description = "The date when the coupon expires. Leave empty or null for no expiration date.", example = "2025-12-31", nullable = true)
        LocalDate expirationDate,

//        @Schema(description = "Number of completed sessions required before this coupon can be used. Leave empty or null for no requirement.", example = "3", nullable = true)
//        Integer requiredSessions,

        @Schema(description = "Sets the coupon to active or inactive upon creation. Defaults to true.", example = "true")
        boolean isActive
){}