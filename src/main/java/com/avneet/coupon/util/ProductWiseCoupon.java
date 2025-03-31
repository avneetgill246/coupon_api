package com.avneet.coupon.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.avneet.coupon.dto.CouponApplicableDto;
import com.avneet.coupon.dto.CouponAppliedDto;
import com.avneet.coupon.dto.DiscountedItemDto;
import com.avneet.coupon.entity.Coupon;
import com.avneet.coupon.exception.ResourceNotFoundException;
import com.avneet.coupon.model.Cart;
import com.avneet.coupon.model.CartItem;

public class ProductWiseCoupon extends BaseCouponType {
    public static BigDecimal findDiscount(Coupon coupon, Cart cart) {
        // Logic to check if the coupon is applicable

        for (CartItem item : cart.getItems()) {
            if (item.getProductId()==coupon.getCouponDetails().getProduct_id()) {

                BigDecimal actualPrice = item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
                BigDecimal discountAmount = actualPrice
                        .multiply(BigDecimal.valueOf(coupon.getCouponDetails().getMaxDiscountPer()))
                        .divide(BigDecimal.valueOf(100));
                return discountAmount.setScale(2, java.math.RoundingMode.HALF_UP);
            }

        }
        // If no applicable product found, return null
        return null;
    }

    public static CouponApplicableDto isApplicable(Coupon coupon, Cart cart) {

        BigDecimal discountTotal = Optional.ofNullable(findDiscount(coupon, cart))
                .orElse(null);

        if (discountTotal == null) {
            return null;
        }

        return CouponApplicableDto.builder()
                .couponId(coupon.getId())
                .type(coupon.getType())
                .discount(discountTotal)
                .build();

    }

    @Override
    public CouponAppliedDto applyCoupon(Coupon coupon, Cart cart) {

        BigDecimal discountTotal = Optional.ofNullable(findDiscount(coupon, cart))
                .orElseThrow(() -> new ResourceNotFoundException("Coupon type not supported"));

        BigDecimal actualTotal = BigDecimal.ZERO;
        List<DiscountedItemDto> discountedItems = new ArrayList<>();
        // Calculate the discount based on the getItems

        for (CartItem item : cart.getItems()) {

            BigDecimal actualPrice = item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            actualTotal = actualTotal.add(actualPrice);
            discountedItems.add(DiscountedItemDto.builder()
                    .productId(item.getProductId())
                    .quantity(item.getQuantity())
                    .price(coupon.getCouponDetails().getProduct_id()==item.getProductId()
                            ? actualPrice.subtract(discountTotal)
                            : actualPrice)
                    .discount(coupon.getCouponDetails().getProduct_id()==item.getProductId()? discountTotal
                            : BigDecimal.ZERO)
                    .build());

        }

        return CouponAppliedDto.builder()
                .discount(discountTotal)
                .totalPrice(actualTotal)
                .finalPrice(actualTotal.subtract(discountTotal))
                .discountedItems(discountedItems).build();

    }

}
