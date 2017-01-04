package net.wit.controller.pad.model;

import net.wit.controller.assistant.model.SingleModel;
import net.wit.controller.assistant.model.TagModel;
import net.wit.entity.Product;
import net.wit.entity.SpecificationValue;
import net.wit.entity.model.BaseModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2016/11/24.
 */
public class ProductsModel extends BaseModel {
    /*商品ID*/
    private Long id;
    /*型号*/
    private String spec;
    /*颜色*/
    private String color;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void copyFrom(Product product) {
        this.id = product.getId();
        for (SpecificationValue specificationValue:product.getSpecificationValues()) {
            if (specificationValue.getSpecification().getId().equals(1L)) {
                this.spec = specificationValue.getName();
            }
            if (specificationValue.getSpecification().getId().equals(2L)) {
                this.color = specificationValue.getName();
            }
        }

    }
}
