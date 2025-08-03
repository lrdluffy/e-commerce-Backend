package com.strawhats.ecommercebackend.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouponDTO {

    private Long couponId;
    private String code;
    private BigDecimal discount;
    private LocalDateTime expiryDate;
}
