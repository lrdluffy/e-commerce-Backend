package com.strawhats.ecommercebackend.payload;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CouponDTO {

    private Long couponId;
    private String code;
    private BigDecimal discount;
    private LocalDateTime expiryDate;
}
