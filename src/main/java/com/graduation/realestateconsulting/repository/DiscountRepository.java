package com.graduation.realestateconsulting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.graduation.realestateconsulting.model.entity.Discount;

public interface DiscountRepository extends JpaRepository<Discount, Long> {

}