package com.avneet.coupon.dto;

import java.math.BigDecimal;

import com.avneet.coupon.entity.CouponType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
public class CouponApplicableDto {
    private long couponId;
    private CouponType type;
    private BigDecimal discount;
}
