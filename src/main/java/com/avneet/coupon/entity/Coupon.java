
package com.avneet.coupon.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    private CouponType type;

    @OneToOne(cascade = CascadeType.ALL)
    private CouponDetails couponDetails;
}

/*{
type: "CART_TYPE",
maxDiscountPer: 50,
threshold: 100
}*/