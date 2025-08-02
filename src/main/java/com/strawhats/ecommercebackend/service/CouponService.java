package com.strawhats.ecommercebackend.service;

import com.strawhats.ecommercebackend.payload.CouponDTO;

public interface CouponService {

    CouponDTO createCoupon(CouponDTO couponDTO);

    CouponDTO validateCoupon(CouponDTO couponDTO);

}
