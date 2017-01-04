package net.wit.controller.assistant.model;

import net.wit.controller.assistant.model.BaseModel;
import net.wit.entity.Attribute;
import net.wit.entity.Product;

import java.util.HashSet;
import java.util.Set;

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

    public static Set<ProductAttributeModel> bindData(Set<Attribute> attributes, Product product) {
        Set<ProductAttributeModel> models = new HashSet<>(attributes.size());
        for (Attribute attribute : attributes) {
            ProductAttributeModel model = new ProductAttributeModel();
            model.copyFrom(attribute, product);
            models.add(model);
        }
        return models;
    }

}
