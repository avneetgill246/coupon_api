package com.avneet.coupon.dto;

import java.math.BigDecimal;
import java.util.List;

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
public class CouponAppliedDto {
    private BigDecimal totalPrice;
    private BigDecimal discount;
    private BigDecimal finalPrice;
    private List<DiscountedItemDto> discountedItems;
}
