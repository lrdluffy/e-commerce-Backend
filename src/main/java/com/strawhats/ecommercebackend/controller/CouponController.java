package com.strawhats.ecommercebackend.controller;

import com.strawhats.ecommercebackend.payload.CouponDTO;
import com.strawhats.ecommercebackend.service.CouponService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class CouponController {

    private final CouponService couponService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Coupon already exists")
    })
    @Operation(summary = "Create Coupon", description = "Add discount code")
    @PostMapping("/admin/coupon")
    public ResponseEntity<CouponDTO> createCoupon(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Coupon object to be created", required = true)
            @RequestBody CouponDTO couponDTO) {
        CouponDTO createdCouponDTO = couponService.createCoupon(couponDTO);
        return new ResponseEntity<>(createdCouponDTO, HttpStatus.CREATED);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "404", description = "Coupon Not Found"),
            @ApiResponse(responseCode = "400", description = "Coupon is expired")
    })
    @Operation(summary = "Validate Coupon", description = "Validate coupon")
    @PostMapping("/user/coupon/validate")
    public ResponseEntity<CouponDTO> validateCoupon(@RequestBody String couponCode) {
        CouponDTO validatedCouponDTO = couponService.validateCoupon(couponCode);
        return new ResponseEntity<>(validatedCouponDTO, HttpStatus.OK);
    }
}
