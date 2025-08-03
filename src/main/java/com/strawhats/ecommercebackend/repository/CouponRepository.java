package com.strawhats.ecommercebackend.repository;

import com.strawhats.ecommercebackend.model.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Integer> {

    Optional<Coupon> findCouponByCode(String code);
}
