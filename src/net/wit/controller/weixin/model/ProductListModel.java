package net.wit.controller.weixin.model;

import net.wit.entity.Location;
import net.wit.entity.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 商品列表
 * Created by WangChao on 2016-10-12.
 */
public class ProductListModel {
    /**
     * 商品ID
     */
    private Long id;
    /**
     * 商品名称
     */
    private String name;
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
    /**
     * 人气
     */
    private Integer favoriteCount;
    /**
     * 月销量
     */
    private Long monthSales;
    /**
     * 活动
     */
    private List<PromotionModel> promotions;
    /**
     * 好评度
     */
    private Double positivePercent;
    /**
     * 商家名称
     */
    private String tenantName;
    /**
     * 距离
     */
    private double distance;
    /**
     * 点击数
     */
    private Long hits;


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

    public Integer getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(Integer favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public Long getMonthSales() {
        return monthSales;
    }

    public void setMonthSales(Long monthSales) {
        this.monthSales = monthSales;
    }

    public List<PromotionModel> getPromotions() {
        return promotions;
    }

    public void setPromotions(List<PromotionModel> promotions) {
        this.promotions = promotions;
    }

    public Double getPositivePercent() {
        return positivePercent;
    }

    public void setPositivePercent(Double positivePercent) {
        this.positivePercent = positivePercent;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public Long getHits() {
        return hits;
    }

    public void setHits(Long hits) {
        this.hits = hits;
    }

    public void copyFrom(Product product, Location location) {
        this.id = product.getId();
        this.name = product.getName();
        this.fullName = product.getFullName();
        this.thumbnail = product.getThumbnail();
        this.price = product.calcEffectivePrice(null);
        this.marketPrice = product.getMarketPrice();
        this.favoriteCount = product.getFavoriteMembers().size();
        this.monthSales = product.getMonthSales();
        this.promotions = PromotionModel.bindData(new ArrayList<>(product.getValidPromotions()));
        this.positivePercent = 98D;
        if (product.getTenant() != null) {
            this.tenantName = product.getTenant().getName();
            this.distance = product.getTenant().distatce(location);
            if (this.distance != -1) {
                this.distance = Double.parseDouble(String.format("%.2f", this.distance / 1000));
            }
        }
        this.hits = product.getHits();
    }

    public static List<ProductListModel> bindData(List<Product> products, Location location) {
        List<ProductListModel> models = new ArrayList<>();
        for (Product product : products) {
            ProductListModel model = new ProductListModel();
            model.copyFrom(product, location);
            models.add(model);
        }
        return models;
    }

}
