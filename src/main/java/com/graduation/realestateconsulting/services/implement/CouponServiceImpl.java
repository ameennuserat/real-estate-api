package com.graduation.realestateconsulting.services.implement;

import com.graduation.realestateconsulting.exceptions.newExceptions.InvalidCouponException;
import com.graduation.realestateconsulting.model.dto.request.CreateCouponRequest;
import com.graduation.realestateconsulting.model.dto.request.NotificationRequest;
import com.graduation.realestateconsulting.model.dto.request.UpdateCouponRequest;
import com.graduation.realestateconsulting.model.dto.response.CouponResponse;
import com.graduation.realestateconsulting.model.entity.Client;
import com.graduation.realestateconsulting.model.entity.CouponEntity;
import com.graduation.realestateconsulting.model.entity.Expert;
import com.graduation.realestateconsulting.model.entity.User;
import com.graduation.realestateconsulting.model.enums.BookingStatus;
import com.graduation.realestateconsulting.model.enums.DiscountType;
import com.graduation.realestateconsulting.model.enums.Role;
import com.graduation.realestateconsulting.model.mapper.CouponMapper;
import com.graduation.realestateconsulting.repository.BookingRepository;
import com.graduation.realestateconsulting.repository.CouponRepository;
import com.graduation.realestateconsulting.repository.ExpertRepository;
import com.graduation.realestateconsulting.repository.UserRepository;
import com.graduation.realestateconsulting.services.CouponService;
import com.graduation.realestateconsulting.services.NotificationService;
import com.stripe.exception.StripeException;
import com.stripe.model.Coupon;
import com.stripe.model.PromotionCode;
import com.stripe.param.CouponCreateParams;
import com.stripe.param.PromotionCodeCreateParams;
import com.stripe.param.PromotionCodeUpdateParams;
//import jakarta.transaction.Transactional;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {
    private final CouponRepository couponRepository;
    private final ExpertRepository expertRepository;
    private final BookingRepository bookingRepository;
    private final CouponMapper mapper;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    @Override
    @Transactional
    public CouponResponse createCoupon(CreateCouponRequest request, User creator) {

        validateCouponRequest(request);
        if (couponRepository.existsByCode(request.code().toUpperCase())) {
            throw new IllegalArgumentException("This coupon code already exists.");
        }

        CouponEntity provisionalCoupon = CouponEntity.builder()
                .code(request.code().toUpperCase())
                .description(request.description())
                .discountType(request.discountType())
                .discountValue(request.discountType() == DiscountType.PERCENTAGE ?
                        request.percentageValue() :
                        request.amountValue())
                .maxUses(request.maxUses())
                .expirationDate(request.expirationDate() != null ? request.expirationDate().atStartOfDay() : null)
                .isActive(request.isActive())
                .expert(creator.getRole() == Role.EXPERT ? creator.getExpert() : null)
                .build();


        couponRepository.save(provisionalCoupon);

        try {
            // --- 3. التواصل مع Stripe لإنشاء الكوبونات ---

            //  إنشاء كائن Coupon في Stripe ---
            CouponCreateParams.Builder couponParamsBuilder = CouponCreateParams.builder()
                    .setName(request.code());

            if (request.discountType() == DiscountType.PERCENTAGE) {
                couponParamsBuilder.setPercentOff(request.percentageValue());
            } else {
                long amountInCents = request.amountValue().multiply(BigDecimal.valueOf(100)).longValue();
                couponParamsBuilder.setAmountOff(amountInCents).setCurrency("usd");
            }
            // ****************************************************

            Coupon stripeCoupon = Coupon.create(couponParamsBuilder.build());

            // إنشاء كائن Promotion Code في Stripe ---
            PromotionCodeCreateParams.Builder promoCodeParamsBuilder = PromotionCodeCreateParams.builder()
                    .setCoupon(stripeCoupon.getId())
                    .setCode(request.code().toUpperCase())
                    .setActive(request.isActive());

            if (request.expirationDate() != null) {
                long expiresAtTimestamp = request.expirationDate().atStartOfDay().toEpochSecond(ZoneOffset.UTC);
                promoCodeParamsBuilder.setExpiresAt(expiresAtTimestamp);
            }
            PromotionCode stripePromotionCode = PromotionCode.create(promoCodeParamsBuilder.build());

            provisionalCoupon.setStripeCouponId(stripeCoupon.getId());
            provisionalCoupon.setStripePromotionCodeId(stripePromotionCode.getId());


            CouponEntity finalCoupon = couponRepository.save(provisionalCoupon);

            String title = "New Coupon Created";
            String message = String.format("Coupon '%s' has been created in the system.", finalCoupon.getCode());

            List<User> admins = userRepository.findAllByRole(Role.ADMIN);
            List<User> experts = userRepository.findAllByRole(Role.EXPERT);

            if(creator.getRole() == Role.ADMIN) {
                // to all admins
                for(User admin : admins) {
                    NotificationRequest request1 = NotificationRequest.builder().title(title).message(message).user(admin).build();
                    notificationService.createAndSendNotification(request1);
                }
                // to all experts
                for(User expert : experts) {
                    NotificationRequest request1 = NotificationRequest.builder().title(title).message(message).user(expert).build();
                    notificationService.createAndSendNotification(request1);
                }
            }
            // if added by expert notify all admins only
            else {
                for(User admin : admins) {
                    NotificationRequest request1 = NotificationRequest.builder().title(title).message(message).user(admin).build();
                    notificationService.createAndSendNotification(request1);
                }
            }

            return mapper.toDto(finalCoupon);

        } catch (StripeException e) {
            throw new RuntimeException("Stripe API error: " + e.getMessage(), e);
        }
    }


    @Transactional
    @Override
    public CouponResponse updateCoupon(Long couponId, UpdateCouponRequest request, User currentUser) {

        CouponEntity couponToUpdate = couponRepository.findById(couponId)
                .orElseThrow(() -> new IllegalArgumentException("Coupon with ID " + couponId + " not found."));


        boolean isAdmin = currentUser.getRole() == Role.ADMIN;
        boolean isOwner = (couponToUpdate.getExpert() != null) &&
                (currentUser.getExpert() != null) &&
                (couponToUpdate.getExpert().getId().equals(currentUser.getExpert().getId()));

        if (!isAdmin && !isOwner) {
            throw new AccessDeniedException("You do not have permission to update this coupon.");
        }

        try {
            // 3. استرجاع الـ Promotion Code من Stripe لتحديثه
            PromotionCode stripePromotionCode = PromotionCode.retrieve(couponToUpdate.getStripePromotionCodeId());
            PromotionCodeUpdateParams.Builder paramsBuilder = PromotionCodeUpdateParams.builder();
            boolean needsStripeUpdate = false;

            if (request.getIsActive().isPresent()) {
                paramsBuilder.setActive(request.getIsActive().get());
                needsStripeUpdate = true;
            }


            if (needsStripeUpdate) {
                stripePromotionCode.update(paramsBuilder.build());
            }

        } catch (StripeException e) {
            throw new RuntimeException("Stripe API error while updating promotion code: " + e.getMessage(), e);
        }

        request.getDescription().ifPresent(couponToUpdate::setDescription);
        request.getIsActive().ifPresent(couponToUpdate::setActive);
        request.getMaxUses().ifPresent(couponToUpdate::setMaxUses);

        CouponEntity updatedEntity = couponRepository.save(couponToUpdate);

        return mapper.toDto(updatedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public CouponEntity validateAndGetCoupon(String code, Client client, Expert expert) {

        CouponEntity localCoupon = couponRepository.findByCode(code.toUpperCase())
                .orElseThrow(() -> new InvalidCouponException("Coupon code '" + code + "' not found."));


        if (!localCoupon.isActive()) {
            throw new InvalidCouponException("This coupon is currently inactive.");
        }

        if (localCoupon.getExpert() != null && !localCoupon.getExpert().getId().equals(expert.getId())) {
            throw new InvalidCouponException("This coupon is not valid for the selected expert.");
        }

        try {
            PromotionCode stripePromoCode = PromotionCode.retrieve(localCoupon.getStripePromotionCodeId());
            if (!stripePromoCode.getActive()) {
                throw new InvalidCouponException("This coupon is no longer valid.");
            }
        } catch (StripeException e) {
            throw new RuntimeException("Could not verify coupon with payment provider.", e);
        }

        if (localCoupon.getDiscountType() == DiscountType.PERCENTAGE) {

            if (localCoupon.getExpirationDate() != null && localCoupon.getExpirationDate().isBefore(LocalDateTime.now())) {
                throw new InvalidCouponException("This coupon has expired.");
            }

        } else if (localCoupon.getDiscountType() == DiscountType.FIXED_AMOUNT) {

            if (localCoupon.getMaxUses() != null && localCoupon.getTimesUsed() >= localCoupon.getMaxUses()) {
                throw new InvalidCouponException("This coupon has reached its maximum usage limit.");
            }
        }

        return localCoupon;
    }


    @Override
    public List<CouponResponse> getAllCouponsByExpertId(Long expertId) {
        Expert expert = expertRepository.findById(expertId).orElseThrow(() -> new IllegalArgumentException("Expert with ID " + expertId + " not found."));
        List<CouponEntity> couponEntities = couponRepository.findAllByExpertId(expertId);
        return mapper.toDtos(couponEntities);
    }

    @Override
    public List<CouponResponse> getAllCoupons() {
        List<CouponEntity> couponEntities = couponRepository.findAll();
        return mapper.toDtos(couponEntities);
    }

    @Override
    public CouponResponse getCouponById(Long couponId) {
        CouponEntity couponEntity = couponRepository.findById(couponId).orElseThrow(() -> new IllegalArgumentException("Coupon with ID " + couponId + " not found."));
        return mapper.toDto(couponEntity);
    }


    private void validateCouponRequest(CreateCouponRequest request) {
        if (request.discountType() == null) {
            throw new IllegalArgumentException("Discount type must be specified.");
        }

        switch (request.discountType()) {
            case PERCENTAGE:
                if (request.percentageValue() == null || request.percentageValue().compareTo(BigDecimal.ZERO) <= 0) {
                    throw new IllegalArgumentException("A positive percentage value is required for PERCENTAGE coupons.");
                }

                if (request.expirationDate() == null) {
                    throw new IllegalArgumentException("An expiration date is required for PERCENTAGE coupons.");
                }
                System.out.println("befor check date: " + request.expirationDate());
                if (request.expirationDate().isBefore(LocalDate.now())) {
                    throw new IllegalArgumentException("Expiration date cannot be in the past.");
                }

                if (request.amountValue() != null || request.maxUses() != null) {
                    throw new IllegalArgumentException("Amount value and max uses are not applicable for PERCENTAGE coupons.");
                }
                break;

            case FIXED_AMOUNT:

                if (request.amountValue() == null || request.amountValue().compareTo(BigDecimal.ZERO) <= 0) {
                    throw new IllegalArgumentException("A positive amount value is required for FIXED_AMOUNT coupons.");
                }

                if (request.maxUses() == null || request.maxUses() <= 0) {
                    throw new IllegalArgumentException("A positive max uses limit is required for FIXED_AMOUNT coupons.");
                }

                if (request.percentageValue() != null || request.expirationDate() != null) {
                    throw new IllegalArgumentException("Percentage value, amd expiration date are not applicable for FIXED_AMOUNT coupons.");
                }
                break;
        }
    }
}
