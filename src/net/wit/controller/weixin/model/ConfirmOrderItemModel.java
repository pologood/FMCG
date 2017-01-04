package net.wit.controller.weixin.model;

import net.wit.entity.OrderItem;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ConfirmOrderItemModel extends BaseModel {
    /**
     * ID
     */
    private Long id;
    /**
     * 商品Id
     */
    private Long productId;
    /**
     * 全名
     */
    private String fullName;
    /**
     * 缩略图
     */
    private String thumbnail;
    /**
     * 数量
     */
    private Integer quantity;
    /**
     * 原价
     */
    private BigDecimal originalPrice;
    /**
     * 规格
     */
    private String spec;
    /**
     * 折后价格
     */
    private BigDecimal price;
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

    public BigDecimal getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(BigDecimal originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public List<PromotionModel> getPromotions() {
        return promotions;
    }

    public void setPromotions(List<PromotionModel> promotions) {
        this.promotions = promotions;
    }

    public void copyFrom(OrderItem orderItem) {
        this.id = orderItem.getId();
        this.productId = orderItem.getProduct().getId();
        this.fullName = orderItem.getFullName();
        this.thumbnail = orderItem.getThumbnail();
        this.quantity = orderItem.getQuantity();
        this.price=orderItem.getPrice();
        this.originalPrice = orderItem.getOriginalPrice();
        this.spec = "";
        if (orderItem.getName().length() < this.fullName.length()) {
            this.spec = this.fullName.substring(orderItem.getName().length());
        }
        if(orderItem.getProduct()!=null){
            this.promotions = PromotionModel.bindData(new ArrayList<>(orderItem.getProduct().getValidPromotions()));
        }
    }

    public static List<ConfirmOrderItemModel> bindData(List<OrderItem> orderItems) {
        List<ConfirmOrderItemModel> models = new ArrayList<>();
        for (OrderItem orderItem : orderItems) {
            ConfirmOrderItemModel model = new ConfirmOrderItemModel();
            model.copyFrom(orderItem);
            models.add(model);
        }
        return models;
    }

}
