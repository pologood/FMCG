package net.wit.controller.weixin.model;

import net.wit.entity.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 商品规格
 * Created by wangchao on 2016/10/27.
 */
public class ProductSpecificationModel {
    /**
     * 商品Id
     */
    private Long id;
    /**
     * 价格
     */
    private BigDecimal price;
    /**
     * 可用库存
     */
    private Integer availableStock;
    /**
     * 缩略图
     */
    private String thumbnail;
    /**
     * 规格
     */
    private List<SpecificationValueModel> specifications;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getAvailableStock() {
        return availableStock;
    }

    public void setAvailableStock(Integer availableStock) {
        this.availableStock = availableStock;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public List<SpecificationValueModel> getSpecifications() {
        return specifications;
    }

    public void setSpecifications(List<SpecificationValueModel> specifications) {
        this.specifications = specifications;
    }

    public void copyFrom(Product product) {
        this.id = product.getId();
        this.price = product.getPrice();
        this.availableStock = product.getAvailableStock();
        this.thumbnail = product.getThumbnail();
        this.specifications = SpecificationValueModel.bindData(product.getSpecificationValues());
        for (SpecificationValueModel specification : this.specifications) {
            if (specification.getId().equals(1L) && product.getGoods().getSpecification1Title() != null) {
                specification.setTitle(product.getGoods().getSpecification1Title());
            }
            if (specification.getId().equals(2L) && product.getGoods().getSpecification2Title() != null) {
                specification.setTitle(product.getGoods().getSpecification2Title());
            }
        }
    }

    public static List<ProductSpecificationModel> bindData(Set<Product> products) {
        List<ProductSpecificationModel> models = new ArrayList<>();
        for (Product product : products) {
            ProductSpecificationModel model = new ProductSpecificationModel();
            model.copyFrom(product);
            models.add(model);
        }
        return models;
    }

}
