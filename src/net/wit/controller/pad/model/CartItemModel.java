package net.wit.controller.pad.model;

import net.wit.entity.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by ruanx on 2016/12/6.
 */
public class CartItemModel {

    /*商品id*/
    private Long id;

    /*商品条目id*/
    private Long cartItemId;

    /*商品名称*/
    private String name;

    /*商品价格*/
    private BigDecimal effectivePrice;

    /*是否被勾选*/
    private boolean selected;

    /*商品缩略图*/
    private String thumbnail;

    /*数量*/
    private int quantity;

    /*促销活动*/
    private Set<PromotionModel> promotions;

    /*尺寸*/
    private String spec;

    /*尺寸*/
    private String color;

    /*净重*/
    private String weight;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getEffectivePrice() {
        return effectivePrice;
    }

    public void setEffectivePrice(BigDecimal effectivePrice) {
        this.effectivePrice = effectivePrice;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Long getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(Long cartItemId) {
        this.cartItemId = cartItemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Set<PromotionModel> getPromotions() {
        return promotions;
    }

    public void setPromotions(Set<PromotionModel> promotions) {
        this.promotions = promotions;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void copyFrom(CartItem cartItem, Tenant tenant){
        this.id = cartItem.getProduct().getId();
        this.cartItemId = cartItem.getId();
        this.quantity = cartItem.getQuantity();
        this.name = cartItem.getProduct().getName();
        this.effectivePrice = cartItem.getEffectivePrice();
        this.selected = cartItem.getSelected();
        this.thumbnail = cartItem.getProduct().getThumbnail();
        Product product = cartItem.getProduct();
        Set<PromotionModel> promotions = new HashSet<PromotionModel>();
        for (Promotion promotion:product.getPromotions()) {
            PromotionModel model = new PromotionModel();
            model.copyFrom(promotion);
            promotions.add(model);
        }
        for (Promotion promotion:tenant.getMailPromotions()) {
            PromotionModel model = new PromotionModel();
            model.copyFrom(promotion);
            promotions.add(model);
        }
        this.promotions = promotions;
        for(SpecificationValue specificationValue:cartItem.getProduct().getSpecificationValues()){
            String type = "";
            if("颜色".equals(specificationValue.getSpecification().getName())){
                this.color = specificationValue.getName();
            }
            if("型号".equals(specificationValue.getSpecification().getName())){
                this.spec = specificationValue.getName();
            }
            if("净重".equals(specificationValue.getSpecification().getName())){
                this.spec = specificationValue.getName();
            }
        }
    }
}
