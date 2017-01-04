package net.wit.controller.weixin.model;

import net.wit.entity.CartItem;
import net.wit.entity.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CartItemModel extends BaseModel {
    /**
     * 等级ID
     */
    private Long id;
    /**
     * 商品
     */
    private Long productId;
    /**
     * 全名
     */
    private String fullName;
    /**
     * 规格
     */
    private List<SpecificationValueModel> specification;
    /**
     * 缩略图
     */
    private String thumbnail;
    /**
     * 数量
     */
    private Integer quantity;
    /**
     * 价格
     */
    private BigDecimal price;
    /**
     * 市场价
     */
    private BigDecimal marketPrice;
    /**
     * 选择
     */
    private Boolean selected;
    /**
     * 活动
     */
    private List<PromotionModel> promotions;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(BigDecimal marketPrice) {
        this.marketPrice = marketPrice;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public List<SpecificationValueModel> getSpecification() {
        return specification;
    }

    public void setSpecification(List<SpecificationValueModel> specification) {
        this.specification = specification;
    }

    public List<PromotionModel> getPromotions() {
        return promotions;
    }

    public void setPromotions(List<PromotionModel> promotions) {
        this.promotions = promotions;
    }

    public void copyFrom(CartItem cartItem) {
        this.id = cartItem.getId();
        this.quantity = cartItem.getQuantity();
        this.price = cartItem.getEffectivePrice();
        this.selected = cartItem.getSelected();
        Product product = cartItem.getProduct();
        if (product != null) {
            this.marketPrice = product.getMarketPrice();
            this.productId = product.getId();
            this.fullName = product.getFullName();
            this.thumbnail = product.getThumbnail();
            this.specification = SpecificationValueModel.bindData(product.getSpecificationValues());
            this.promotions = PromotionModel.bindData(new ArrayList<>(product.getValidPromotions()));
        }
    }

    public static List<CartItemModel> bindData(Set<CartItem> cartItems) {
        List<CartItemModel> models = new ArrayList<>(cartItems.size());
        for (CartItem cartItem : cartItems) {
            CartItemModel model = new CartItemModel();
            model.copyFrom(cartItem);
            models.add(model);
        }
        return models;
    }

}
