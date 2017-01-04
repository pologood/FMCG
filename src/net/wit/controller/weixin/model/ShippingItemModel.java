package net.wit.controller.weixin.model;

import net.wit.entity.ShippingItem;

import java.util.ArrayList;
import java.util.List;

public class ShippingItemModel extends BaseModel {

    /**
     * 编号
     */
    private Long id;

    /**
     * 编号
     */
    private String sn;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 数量
     */
    private Integer quantity;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void copyFrom(ShippingItem shippingItem) {
        this.id = shippingItem.getId();
        this.sn = shippingItem.getSn();
        this.name = shippingItem.getName();
        this.quantity = shippingItem.getQuantity();
    }

    public static List<ShippingItemModel> bindData(List<ShippingItem> shippingItems) {
        List<ShippingItemModel> models = new ArrayList<ShippingItemModel>();
        for (ShippingItem shippingItem : shippingItems) {
            ShippingItemModel model = new ShippingItemModel();
            model.copyFrom(shippingItem);
            models.add(model);
        }
        return models;
    }

}
