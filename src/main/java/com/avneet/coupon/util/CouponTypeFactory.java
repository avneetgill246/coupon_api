package com.avneet.coupon.util;

import com.avneet.coupon.entity.CouponType;

public class CouponTypeFactory {
    // Factory method to create a coupon type based on the provided string
    // This method returns an instance of BaseCouponType or its subclasses based on the coupon type string
        public static BaseCouponType getCouponType(CouponType couponType) {
            return switch (couponType) {
                case CART_WISE -> new CartWiseCoupon();
                case PRODUCT_WISE -> new ProductWiseCoupon();
                case BXGY -> new BXGYCoupon();
                default -> throw new IllegalArgumentException("Invalid coupon type: " + couponType);
            };
        }
}
