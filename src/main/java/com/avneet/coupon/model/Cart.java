
package com.avneet.coupon.model;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
public class Cart {
    private List<CartItem> items;
}
