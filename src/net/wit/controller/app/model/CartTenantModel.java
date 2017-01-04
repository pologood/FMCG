package net.wit.controller.app.model;

import net.wit.entity.*;

import java.util.*;

/**
 *
 * Created by WangChao on 2016-7-18.
 */
public class CartTenantModel {
    /** 店铺ID */
    private Long id;
    /** 店铺名称 */
    private String name;
    /** 店铺活动信息 */
    private Set<PromotionModel> promotions;
    /* 是否可以领券 */
    private Boolean availableCoupon;
    /* 订单商品 */
    private List<CartItemModel> cartItems;
	/*礼物项*/
	private Set<GiftItemModel> giftItems;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<PromotionModel> getPromotions() {
        return promotions;
    }

    public void setPromotions(Set<PromotionModel> promotions) {
        this.promotions = promotions;
    }

    public Boolean getAvailableCoupon() {
        return availableCoupon;
    }

    public void setAvailableCoupon(Boolean availableCoupon) {
        this.availableCoupon = availableCoupon;
    }

    public List<CartItemModel> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItemModel> cartItems) {
        this.cartItems = cartItems;
    }

    public Set<GiftItemModel> getGiftItems() {
		return giftItems;
	}

	public void setGiftItems(Set<GiftItemModel> giftItems) {
		this.giftItems = giftItems;
	}

    public void copyFrom(Tenant tenant,Cart cart){
        this.id=tenant.getId();
        this.name=tenant.getName();
        this.availableCoupon=false;
        for (Coupon coupon:tenant.getCoupons()){
            if (!coupon.getHasExpired() && coupon.getIsEnabled() && coupon.getHasStart() && cart.isValid(coupon) &&coupon.getType()== Coupon.Type.tenantCoupon){
                availableCoupon=true;
                break;
            }
        }
        Set<PromotionModel> promotionModels = new HashSet<>();
        for (Promotion promotion:tenant.getPromotions()) {
        	if (promotion.getType().equals(Promotion.Type.mail)) {
                PromotionModel model = new PromotionModel();
                model.copyFrom(promotion);
                promotionModels.add(model);
        	}
        }
        this.promotions=promotionModels;
    }

    public static List<CartTenantModel> bindData(Cart cart){
        List<CartTenantModel> models=new ArrayList<>();
        for(Tenant tenant:cart.getTenants()){
            CartTenantModel model=new CartTenantModel();
            model.copyFrom(tenant,cart);
            model.setCartItems(CartItemModel.bindData(cart.getCartItems(tenant)));
            model.setGiftItems(GiftItemModel.bindData(cart.getGiftItems(tenant)));
            models.add(model);
        }
        return models;
    }

}
