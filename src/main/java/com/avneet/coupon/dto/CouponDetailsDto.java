package com.avneet.coupon.dto;

import java.util.List;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;

import com.avneet.coupon.entity.BxGyProduct;

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
public class CouponDetailsDto {

    private Long id;

    @DecimalMin(value = "0.0", inclusive = true, message = "Threshold value must be at least 0")
    private int threshold;

    @DecimalMin(value = "0.0", inclusive = true, message = "Discount value must be at least 0")
    @DecimalMax(value = "100.0", inclusive = true, message = "Discount value must not exceed 100")
    private int maxDiscountPer;
    
    private long product_id;

    @DecimalMin(value = "1.0", inclusive = true, message = "Buy Quantity value must be at least 1")
    private int buyQuantity;

    @DecimalMin(value = "1.0", inclusive = true, message = "Get Quantity value must be at least 1")
    private int getQuantity;

    @DecimalMin(value = "1.0", inclusive = true, message = "Max Usage Count value must be at least 1")
    private int maxUsageCount;
    private List<BxGyProductDto>  buyProducts;
    private List<BxGyProductDto>  getProducts;
 

}
