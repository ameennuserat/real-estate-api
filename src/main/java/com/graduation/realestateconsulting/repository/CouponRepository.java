package com.graduation.realestateconsulting.repository;


import com.graduation.realestateconsulting.model.entity.CouponEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CouponRepository extends JpaRepository<CouponEntity, Long> {
    boolean existsByCode(String upperCase);
    Optional<CouponEntity> findByCode(String code);
    List<CouponEntity> findAllByExpertId(Long expert);
}
