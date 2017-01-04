package net.wit.controller.weixin.model;

import net.wit.entity.Attribute;
import net.wit.entity.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductAttributeModel extends BaseModel {
    /*属性ID*/
    private Long id;
    /*属性名*/
    private String name;
    /*属性值*/
    private String value;

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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void copyFrom(Attribute attribute, Product product) {
        this.id = attribute.getId();
        this.name = attribute.getName();
        this.value = product.getAttributeValue(attribute);
    }

    public static List<ProductAttributeModel> bindData(Product product) {
        List<ProductAttributeModel> models = new ArrayList<>();
        for (Attribute attribute : product.getProductCategory().getAttributes()) {
            ProductAttributeModel model = new ProductAttributeModel();
            model.copyFrom(attribute, product);
            models.add(model);
        }
        return models;
    }

}
