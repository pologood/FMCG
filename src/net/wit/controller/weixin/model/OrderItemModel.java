package net.wit.controller.weixin.model;

import net.wit.entity.OrderItem;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class OrderItemModel extends BaseModel {
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
     * 价格
     */
    private BigDecimal price;
    /**
     * 商品是否上架
     */
    private Boolean isMarketable;
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

    public Boolean getMarketable() {
        return isMarketable;
    }

    public void setMarketable(Boolean marketable) {
        isMarketable = marketable;
    }

    public List<PromotionModel> getPromotions() {
        return promotions;
    }

    public void setPromotions(List<PromotionModel> promotions) {
        this.promotions = promotions;
    }

    public void copyFrom(OrderItem orderItem) {
        this.id = orderItem.getId();
        this.isMarketable=false;
        if(orderItem.getProduct()!=null){
            this.productId = orderItem.getProduct().getId();
            this.isMarketable = orderItem.getProduct().getIsMarketable();
            this.promotions = PromotionModel.bindData(new ArrayList<>(orderItem.getProduct().getValidPromotions()));
        }
        this.fullName = orderItem.getFullName();
        this.thumbnail = orderItem.getThumbnail();
        this.quantity = orderItem.getQuantity();
        this.price = orderItem.getPrice();
    }

    public static List<OrderItemModel> bindData(List<OrderItem> orderItems) {
        List<OrderItemModel> models = new ArrayList<>();
        for (OrderItem orderItem : orderItems) {
            OrderItemModel model = new OrderItemModel();
            model.copyFrom(orderItem);
            models.add(model);
        }
        return models;
    }

}
