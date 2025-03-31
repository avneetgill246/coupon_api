
package com.avneet.coupon.service;

import com.avneet.coupon.dto.BxGyProductDto;
import com.avneet.coupon.dto.CouponApplicableDto;
import com.avneet.coupon.dto.CouponAppliedDto;
import com.avneet.coupon.dto.CouponDTO;
import com.avneet.coupon.dto.CouponDetailsDto;
import com.avneet.coupon.entity.Coupon;
import com.avneet.coupon.entity.CouponType;
import com.avneet.coupon.exception.ResourceNotFoundException;
import com.avneet.coupon.model.Cart;
import com.avneet.coupon.model.CartItem;
import com.avneet.coupon.repository.CouponRepository;
import com.avneet.coupon.util.BXGYCoupon;
import com.avneet.coupon.util.BaseCouponType;
import com.avneet.coupon.util.CartWiseCoupon;
import com.avneet.coupon.util.CouponTypeFactory;
import com.avneet.coupon.util.ProductWiseCoupon;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import com.avneet.coupon.entity.BxGyProduct;
import com.avneet.coupon.entity.CouponDetails;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;

    public CouponDTO createCoupon(CouponDTO dto) {
        Coupon coupon = mapToEntity(dto);
        return mapToDTO(couponRepository.save(coupon));
    }

    public List<CouponDTO> getAllCoupons() {
        return couponRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public CouponDTO getCoupon(Long id) {
        Coupon coupon = couponRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Coupon not found"));
        return mapToDTO(coupon);
    }

    public CouponDTO updateCoupon(Long id, CouponDTO dto) {
        Coupon coupon = couponRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Coupon not found"));

        coupon.setType(dto.getType());
        CouponDetails couponDetails = coupon.getCouponDetails();
        couponDetails.setMaxDiscountPer(dto.getCouponDetails().getMaxDiscountPer());
        couponDetails.setMaxUsageCount(dto.getCouponDetails().getMaxUsageCount());
        couponDetails.setThreshold(dto.getCouponDetails().getThreshold());        
        couponDetails.setProduct_id(dto.getCouponDetails().getProduct_id());


        // Update buyProducts and getProducts
        List<BxGyProductDto> buyProduct = dto.getCouponDetails().getBuyProducts();
       
        if (buyProduct != null) {
            List<BxGyProduct> buyPro = new ArrayList<>();
            for (BxGyProductDto product : buyProduct) {
                BxGyProduct bxGyProduct = new BxGyProduct();
                bxGyProduct.setId(product.getId());
                bxGyProduct.setProductId(product.getProductId());
                bxGyProduct.setQuantity(product.getQuantity());
                buyPro.add(bxGyProduct);
            }
            couponDetails.setBuyProducts(buyPro);   
        }
       
        // Update getProducts
        List<BxGyProductDto> getProduct = dto.getCouponDetails().getGetProducts();
        
        if (getProduct != null) {
            List<BxGyProduct> getPro =new ArrayList<>();
            for (BxGyProductDto product : getProduct) {      
                BxGyProduct bxGyProduct = new BxGyProduct();
                bxGyProduct.setId(product.getId());
                bxGyProduct.setProductId(product.getProductId());
                bxGyProduct.setQuantity(product.getQuantity());
                getPro.add(bxGyProduct);
            }
            couponDetails.setGetProducts(getPro);
        } 
        coupon.setCouponDetails(couponDetails);

        return mapToDTO(couponRepository.save(coupon));
    }

    public void deleteCoupon(Long id) {
        couponRepository.deleteById(id);
    }

    public List<CouponApplicableDto> getApplicableCoupons(Cart cart) {
        // here isntead of new obj on is applicable static function?
        List<Coupon> allCoupons = couponRepository.findAll();
        return allCoupons.stream()
                .map(coupon -> isApplicableAll(coupon, cart))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public CouponAppliedDto applyCoupon(Long id, Cart cart) {
        Coupon coupon = couponRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Coupon not found"));

        // Get the coupon type Object
        BaseCouponType couponType = Optional.ofNullable(CouponTypeFactory.getCouponType(coupon.getType()))
                .orElseThrow(() -> new ResourceNotFoundException("Coupon type not supported"));

         // Apply the coupon to the cart
         return Optional.ofNullable(couponType.applyCoupon(coupon, cart))
                .orElseThrow(() -> new ResourceNotFoundException("Coupon is not applicable to this cart"));

    }

    private CouponApplicableDto isApplicableAll(Coupon coupon, Cart cart) {
        // Check if the coupon is applicable to the cart
        return switch (coupon.getType()) {
            case CART_WISE -> CartWiseCoupon.isApplicable(coupon, cart);
            case PRODUCT_WISE -> ProductWiseCoupon.isApplicable(coupon, cart);
            case BXGY -> BXGYCoupon.isApplicable(coupon, cart);
            default -> null;
        };
    }

    private CouponDTO mapToDTO(Coupon coupon) {
        return CouponDTO.builder()
                .id(coupon.getId())
                .type(coupon.getType())
                .couponDetails(coupon.getCouponDetails()!=null?CouponDetailsDto.builder()
                        // .id(coupon.getCouponDetails().getId())
                        .maxDiscountPer(coupon.getCouponDetails().getMaxDiscountPer())
                        .maxUsageCount(coupon.getCouponDetails().getMaxUsageCount())
                        .threshold(coupon.getCouponDetails().getThreshold())
                        .product_id(coupon.getCouponDetails().getProduct_id())
                        .buyQuantity(coupon.getCouponDetails().getBuyQuantity())
                        .getQuantity(coupon.getCouponDetails().getGetQuantity())
                        .buyProducts(coupon.getCouponDetails().getBuyProducts()!=null?coupon.getCouponDetails().getBuyProducts().stream()
                                .map(bxGyProduct -> BxGyProductDto.builder()
                                        .id(bxGyProduct.getId())
                                        .productId(bxGyProduct.getProductId())
                                        .quantity(bxGyProduct.getQuantity())
                                        .build())
                                .collect(Collectors.toList()):null)
                        .getProducts(coupon.getCouponDetails().getGetProducts()!=null?coupon.getCouponDetails().getGetProducts().stream()
                                .map(bxGyProduct -> BxGyProductDto.builder()
                                        .id(bxGyProduct.getId())
                                        .productId(bxGyProduct.getProductId())
                                        .quantity(bxGyProduct.getQuantity())
                                        .build())
                                .collect(Collectors.toList()):null)
                        .build():new CouponDetailsDto())
                .build();
    }

    private Coupon mapToEntity(CouponDTO dto) {
        return Coupon.builder()
                . id(dto.getId())
                .type(dto.getType())
                .couponDetails(dto.getCouponDetails()!=null?CouponDetails.builder()
                        // .id(dto.getCouponDetails().getId())
                        .maxDiscountPer(dto.getCouponDetails().getMaxDiscountPer())
                        .maxUsageCount(dto.getCouponDetails().getMaxUsageCount())
                        .threshold(dto.getCouponDetails().getThreshold())
                        .product_id(dto.getCouponDetails().getProduct_id())
                        .buyQuantity(dto.getCouponDetails().getBuyQuantity())
                        .getQuantity(dto.getCouponDetails().getGetQuantity())
                        .buyProducts(dto.getCouponDetails().getBuyProducts()!=null?dto.getCouponDetails().getBuyProducts().stream()
                                .map(bxGyProductDto -> BxGyProduct.builder()
                                        .id(bxGyProductDto.getId())
                                        .productId(bxGyProductDto.getProductId())
                                        .quantity(bxGyProductDto.getQuantity())
                                        .build())
                                .collect(Collectors.toList()):null)
                        .getProducts(dto.getCouponDetails().getGetProducts()!=null?dto.getCouponDetails().getGetProducts().stream()
                                .map(bxGyProductDto -> BxGyProduct.builder()
                                        .id(bxGyProductDto.getId())
                                        .productId(bxGyProductDto.getProductId())
                                        .quantity(bxGyProductDto.getQuantity())
                                        .build())
                                .collect(Collectors.toList()):null)
                        .build():new CouponDetails())
                .build();
    }
}
