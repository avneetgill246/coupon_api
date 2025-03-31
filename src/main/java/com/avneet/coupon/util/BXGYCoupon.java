package com.avneet.coupon.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import com.avneet.coupon.dto.CouponApplicableDto;
import com.avneet.coupon.dto.CouponAppliedDto;
import com.avneet.coupon.dto.DiscountedItemDto;
import com.avneet.coupon.entity.BxGyProduct;
import com.avneet.coupon.entity.Coupon;
import com.avneet.coupon.exception.ResourceNotFoundException;
import com.avneet.coupon.model.Cart;
import com.avneet.coupon.model.CartItem;

public class BXGYCoupon extends BaseCouponType{
    

    public static HashMap<CartItem,Integer> findGetItems(Coupon coupon, Cart cart) {
        // Logic to check if the coupon is applicable

        // For BxGYCoupon, we need to check if the cart contains the products in the buyProducts and getProducts lists
        List<Long> buyProducts= coupon.getCouponDetails().getBuyProducts().stream().map(BxGyProduct::getProductId).toList();
        List<Long> getProducts= coupon.getCouponDetails().getGetProducts().stream().map(BxGyProduct::getProductId).toList();

        HashSet<Long> buyProductsSet = new HashSet<>(buyProducts);
        HashSet<Long> getProductsSet = new HashSet<>(getProducts);

        //To simplify skipped logic for taking y lowest price products from getList
        int buyProductCount=0;
        int getProductCount=0;
        // List<CartItem> getItems = new ArrayList<>();
        for (CartItem item : cart.getItems()) {
            if (buyProductsSet.contains(item.getProductId())) {
                buyProductCount ++;
            } else if (getProductsSet.contains(item.getProductId())) {
                getProductCount ++;
            }
        }

        // Check if the buy product Quantity match the BxGy Condition
        if(buyProductCount>=coupon.getCouponDetails().getBuyQuantity() ){
            int buyRepititions= buyProductCount/coupon.getCouponDetails().getBuyQuantity();
            int getRepititions= getProductCount/coupon.getCouponDetails().getGetQuantity();
            getRepititions=getRepititions==0?1:getRepititions;

            int repitions= Math.min(coupon.getCouponDetails().getMaxUsageCount(),Math.min(buyRepititions, getRepititions));
            int maxGetProducts= coupon.getCouponDetails().getGetQuantity()*repitions;
            
            int cartCounter=0;
            int maxGetProCounter=0;
            HashMap<CartItem,Integer> getItems = new HashMap<>();
            while(cartCounter<cart.getItems().size() && maxGetProCounter<maxGetProducts){
                CartItem item= cart.getItems().get(cartCounter);
                if(getProductsSet.contains(item.getProductId())){
                    if(maxGetProducts-maxGetProCounter<item.getQuantity()){
                        getItems.put( item,maxGetProducts-maxGetProCounter);
                        maxGetProCounter+= maxGetProducts-maxGetProCounter;
                    }else{  
                        getItems.put( item,item.getQuantity());
                        maxGetProCounter+= item.getQuantity();
                    }
                }
                cartCounter++;
            }
            return getItems;
        }

        return null; 
    }

  
    public static CouponApplicableDto isApplicable(Coupon coupon, Cart cart) {
     
        HashMap<CartItem,Integer> getItems = Optional.ofNullable(findGetItems(coupon, cart)).orElse(null);
        if(getItems==null){
            return null;
        }   
        BigDecimal discount= BigDecimal.ZERO;
        // Calculate the discount based on the getItems
        for (HashMap.Entry<CartItem, Integer> entry : getItems.entrySet()) {
            CartItem item = entry.getKey();
            discount = discount.add(item.getPrice().multiply(BigDecimal.valueOf(entry.getValue())));
        }
        return CouponApplicableDto.builder()
                .couponId(coupon.getId())
                .type(coupon.getType())
                .discount(discount)
                .build();

    }

    @Override
    public CouponAppliedDto applyCoupon(Coupon coupon,Cart cart) {

        HashMap<CartItem,Integer>  getItems = Optional.ofNullable(findGetItems(coupon, cart)).orElseThrow(()->new ResourceNotFoundException("Coupon type not supported"));

        BigDecimal discount= BigDecimal.ZERO;
        BigDecimal actualTotal= BigDecimal.ZERO;
        List<DiscountedItemDto> discountedItems= new ArrayList<>();
        // Calculate the discount based on the getItems

        for(CartItem item : cart.getItems()){

            BigDecimal actualPrice=item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));

            if(getItems.containsKey(item)){
                BigDecimal discountedPrice= item.getPrice().multiply(BigDecimal.valueOf(getItems.get(item)));
                discountedItems.add(DiscountedItemDto.builder()
                        .productId(item.getProductId())
                        .quantity(item.getQuantity())
                        .price(actualPrice.subtract(discountedPrice))
                        .discount(discount)
                        .build());
                        discount=discount.add(discountedPrice);

            }else{
                discountedItems.add(DiscountedItemDto.builder()
                        .productId(item.getProductId())
                        .quantity(item.getQuantity())
                        .price(actualPrice)
                        .discount(BigDecimal.ZERO)
                        .build());
            }
            actualTotal=actualTotal.add(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
            
        }

        return CouponAppliedDto.builder()
        .discount(discount)
        .totalPrice(actualTotal)
        .finalPrice(actualTotal.subtract(discount))
        .discountedItems(discountedItems).build();

    }
}