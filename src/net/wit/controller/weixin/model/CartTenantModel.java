package net.wit.controller.weixin.model;

import net.wit.entity.Cart;
import net.wit.entity.Tenant;

import java.util.ArrayList;
import java.util.List;

/**
 * 购物车-商家
 * Created by WangChao on 2016-7-18.
 */
public class CartTenantModel {
    /**
     * 店铺ID
     */
    private Long id;
    /**
     * 店铺名称
     */
    private String name;
    /**
     * 订单商品
     */
    private List<CartItemModel> cartItems;
    /**
     * 包邮活动
     */
    private PromotionModel mailPromotion;

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

    public List<CartItemModel> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItemModel> cartItems) {
        this.cartItems = cartItems;
    }

    public PromotionModel getMailPromotion() {
        return mailPromotion;
    }

    public void setMailPromotion(PromotionModel mailPromotion) {
        this.mailPromotion = mailPromotion;
    }

    public void copyFrom(Tenant tenant) {
        this.id = tenant.getId();
        this.name = tenant.getName();
        if (tenant.getMailPromotions().size() != 0) {
            PromotionModel promotionModel = new PromotionModel();
            promotionModel.copyFrom(tenant.getMailPromotions().iterator().next());
            this.mailPromotion = promotionModel;
        }
    }

    public static List<CartTenantModel> bindData(Cart cart) {
        List<CartTenantModel> models = new ArrayList<>();
        for (Tenant tenant : cart.getTenants()) {
            CartTenantModel model = new CartTenantModel();
            model.copyFrom(tenant);
            model.setCartItems(CartItemModel.bindData(cart.getCartItems(tenant)));
            models.add(model);
        }
        return models;
    }

}
