package com.graduation.realestateconsulting.services;

import com.graduation.realestateconsulting.model.dto.request.CreateCouponRequest;
import com.graduation.realestateconsulting.model.dto.request.UpdateCouponRequest;
import com.graduation.realestateconsulting.model.dto.response.CouponResponse;
import com.graduation.realestateconsulting.model.entity.Client;
import com.graduation.realestateconsulting.model.entity.CouponEntity;
import com.graduation.realestateconsulting.model.entity.Expert;
import com.graduation.realestateconsulting.model.entity.User;

import java.util.List;

public interface CouponService {
    CouponResponse createCoupon(CreateCouponRequest request, User creator);
    CouponResponse updateCoupon(Long couponId, UpdateCouponRequest request, User currentUser);
    List<CouponResponse> getAllCouponsByExpertId(Long expertId);
    List<CouponResponse> getAllCoupons();
    CouponResponse getCouponById(Long couponId);
    CouponEntity validateAndGetCoupon(String code, User client, Expert expert);
    List<CouponResponse> getGeneralCoupons();
    // يمكنك إضافة توابع أخرى هنا مستقبلاً مثل:
    // void deactivateCoupon(Long couponId, User creator);
    // List<CouponResponseDTO> findCouponsByExpert(Long expertId);
}