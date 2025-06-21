package com.graduation.realestateconsulting.model.dto.request;

import com.graduation.realestateconsulting.model.entity.CouponEntity;

import java.math.BigDecimal;

public record DiscountResult(BigDecimal finalPrice, BigDecimal discountAmount, CouponEntity appliedCoupon) {
}
