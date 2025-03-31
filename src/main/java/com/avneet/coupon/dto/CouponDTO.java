package com.avneet.coupon.dto;

import com.avneet.coupon.entity.CouponType;
import lombok.*;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CouponDTO {

    private long id;

    @NotNull(message = "Coupon type cannot be null")
    private CouponType type;


    private CouponDetailsDto couponDetails;
    
    // @DecimalMin(value = "0.0", inclusive = true, message = "Discount value must be at least 0")
    // @DecimalMax(value = "100.0", inclusive = true, message = "Discount value must not exceed 100")
    // private BigDecimal discountValue;

    // @DecimalMin(value = "0.0", inclusive = false, message = "Minimum cart value must be greater than 0")
    // private BigDecimal minCartValue;

    // private String applicableProductIds;

    // private String bxgyDetails;
}
