package com.avneet.coupon.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
public class CouponDetails {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;
    private int threshold;
    private int maxDiscountPer;
    private long product_id;
    private int maxUsageCount;
    private int buyQuantity;
    private int getQuantity;
    @OneToMany(mappedBy = "couponDetails")
    private List<BxGyProduct>  buyProducts;
    @OneToMany(mappedBy = "couponDetails")
    private List<BxGyProduct>  getProducts;

    @OneToOne
    private Coupon coupon;
 

}
