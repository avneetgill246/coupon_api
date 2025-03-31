
package com.avneet.coupon.model;

import lombok.*;

import java.math.BigDecimal;

@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
public class CartItem {
    private long productId;
    private String productName;
    private BigDecimal price;
    private int quantity;
}
