package net.wit.controller.pad.model;

import net.wit.controller.app.model.BaseModel;
import net.wit.entity.ProductImage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/23.
 */
public class ProductImageModel extends BaseModel {

    /** 标题 */
    private String title;

    /** 中图片 */
    private String medium;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public void copyFrom(ProductImage productImage) {
        this.title = productImage.getTitle();
        this.medium = productImage.getMedium();
    }

    public static List<ProductImageModel> bindData(List<ProductImage> productImages) {
        List<ProductImageModel> models = new ArrayList<ProductImageModel>();
        for (ProductImage productImage:productImages) {
            ProductImageModel model = new ProductImageModel();
            model.copyFrom(productImage);
            models.add(model);
        }
        return models;
    }
}
