package com.avneet.coupon.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.avneet.coupon.dto.CouponApplicableDto;
import com.avneet.coupon.dto.CouponAppliedDto;
import com.avneet.coupon.dto.CouponDTO;
import com.avneet.coupon.model.Cart;
import com.avneet.coupon.service.CouponService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

@PostMapping
public ResponseEntity<CouponDTO> createCoupon(@Valid @RequestBody CouponDTO dto) {
    return ResponseEntity.ok(couponService.createCoupon(dto));
}

    @GetMapping
    public ResponseEntity<List<CouponDTO>> getAllCoupons() {
        return ResponseEntity.ok(couponService.getAllCoupons());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CouponDTO> getCoupon(@PathVariable Long id) {
        return ResponseEntity.ok(couponService.getCoupon(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CouponDTO> updateCoupon(@PathVariable Long id, @RequestBody CouponDTO dto) {
        return ResponseEntity.ok(couponService.updateCoupon(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCoupon(@PathVariable Long id) {
        couponService.deleteCoupon(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/applicable-coupons")
    public ResponseEntity<List<CouponApplicableDto>> getApplicableCoupons(@RequestBody Cart cart) {
        return ResponseEntity.ok().body(couponService.getApplicableCoupons(cart));
    }

    @PostMapping("/apply-coupon/{id}")
    public ResponseEntity<CouponAppliedDto> applyCoupon(@PathVariable Long id, @RequestBody Cart cart) {
        return ResponseEntity.ok().body(couponService.applyCoupon(id, cart));
    }
}
