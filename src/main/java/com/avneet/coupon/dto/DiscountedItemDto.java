package com.avneet.coupon.dto;

import java.math.BigDecimal;

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
public class DiscountedItemDto {
    private long productId;
    private int quantity;
    private BigDecimal price;
    private BigDecimal discount;

}
