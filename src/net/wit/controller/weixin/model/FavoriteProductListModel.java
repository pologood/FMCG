package net.wit.controller.weixin.model;

import net.wit.entity.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 收藏的商品列表
 * Created by WangChao on 2016-10-12.
 */
public class FavoriteProductListModel {
    /**
     * 商品ID
     */
    private Long id;
    /**
     * 商品全名
     */
    private String fullName;
    /**
     * 缩略图
     */
    private String thumbnail;
    /**
     * 销售价
     */
    private BigDecimal price;
    /**
     * 市场价
     */
    private BigDecimal marketPrice;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public void copyFrom(Product product) {
        this.id = product.getId();
        this.fullName = product.getFullName();
        this.thumbnail = product.getThumbnail();
        this.price = product.calcEffectivePrice(null);
        this.marketPrice = product.getMarketPrice();
    }

    public static List<FavoriteProductListModel> bindData(List<Product> products) {
        List<FavoriteProductListModel> models = new ArrayList<>();
        for (Product product : products) {
            FavoriteProductListModel model = new FavoriteProductListModel();
            model.copyFrom(product);
            models.add(model);
        }
        return models;
    }

}
