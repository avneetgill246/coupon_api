package com.avneet.coupon.util;

import com.avneet.coupon.dto.CouponAppliedDto;
import com.avneet.coupon.entity.Coupon;
import com.avneet.coupon.model.Cart;

public class BaseCouponType {
    
    // This is a base class for all coupon types
    // It can contain common properties and methods for all coupon types


    public CouponAppliedDto applyCoupon(Coupon coupon,Cart cart) {
        // Logic to calculate discount based on the coupon type
        return null; 
    }

}
