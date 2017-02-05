package net.wit.controller.weixin.model;

import net.wit.entity.ProductCategory;

import java.util.ArrayList;
import java.util.List;

public class ProductCategoryModel extends BaseModel {
    /*分类ID*/
    private Long id;
    /*分类名*/
    private String name;
    /*分类图标*/
    private String image;

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void copyFrom(ProductCategory productCategory) {
        this.id = productCategory.getId();
        this.name = productCategory.getName();
        this.image = productCategory.getThumbnail();
    }

    public static List<ProductCategoryModel> bindAllData(List<ProductCategory> productCategories) {
        List<ProductCategoryModel> models = new ArrayList<>();
        for (ProductCategory productCategory : productCategories) {
            ProductCategoryModel model = new ProductCategoryModel();
            model.copyFrom(productCategory);
            models.add(model);
        }
        return models;
    }

}
