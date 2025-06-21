package com.graduation.realestateconsulting.model.mapper;


import com.graduation.realestateconsulting.model.dto.request.CreateCouponRequest;
import com.graduation.realestateconsulting.model.dto.response.CouponResponse;
import com.graduation.realestateconsulting.model.entity.CouponEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CouponMapper {

    public CouponResponse toDto(CouponEntity couponEntity) {
        return CouponResponse.builder()
                .id(couponEntity.getId())
                .expertName(couponEntity.getExpert() != null ? couponEntity.getExpert().getUser().getFirstName()+" "+couponEntity.getExpert().getUser().getLastName(): null)
                .expertId(couponEntity.getExpert() != null ? couponEntity.getExpert().getId() : null)
                .code(couponEntity.getCode())
                .discountType(couponEntity.getDiscountType())
                .discountValue(couponEntity.getDiscountValue())
                .description(couponEntity.getDescription())
                .expirationDate(couponEntity.getExpirationDate())
                .isActive(couponEntity.isActive())
                .maxUses(couponEntity.getMaxUses())
//                .requiredSessions(couponEntity.getRequiredSessions())
                .createdAt(couponEntity.getCreatedAt())
                .timesUsed(couponEntity.getTimesUsed())
                .build();
    }

    public List<CouponResponse> toDtos(List<CouponEntity> couponEntityList) {
        return couponEntityList.stream().map(this::toDto).collect(Collectors.toList());
    }
}
