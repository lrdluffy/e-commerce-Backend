package com.strawhats.ecommercebackend.service;

import com.strawhats.ecommercebackend.exception.ApiException;
import com.strawhats.ecommercebackend.exception.ResourceNotFoundException;
import com.strawhats.ecommercebackend.model.Coupon;
import com.strawhats.ecommercebackend.payload.CouponDTO;
import com.strawhats.ecommercebackend.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;
    private final ModelMapper modelMapper;

    @Override
    public CouponDTO createCoupon(CouponDTO couponDTO) {
        couponRepository.findCouponByCode(couponDTO.getCode())
                .ifPresent(coupon -> new ApiException("Coupon with code " + coupon.getCode() + " already exists!"));

        Coupon coupon = modelMapper.map(couponDTO, Coupon.class);
        Coupon savedCoupon = couponRepository.save(coupon);

        CouponDTO savedCouponDTO = modelMapper.map(savedCoupon, CouponDTO.class);
        return savedCouponDTO;
    }

    @Override
    public CouponDTO validateCoupon(String couponCode) {
        Coupon coupon = couponRepository.findCouponByCode(couponCode)
                .orElseThrow(() -> new ResourceNotFoundException("Coupon", "code", couponCode));

        if (coupon.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new ApiException("Coupon has expired!");
        }

        CouponDTO validCouponDTO = modelMapper.map(couponCode, CouponDTO.class);
        return validCouponDTO;
    }
}
