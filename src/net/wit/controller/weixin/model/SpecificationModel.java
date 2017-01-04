package net.wit.controller.weixin.model;

import net.wit.entity.Goods;
import net.wit.entity.Product;
import net.wit.entity.Specification;
import net.wit.entity.SpecificationValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 商品规格
 * Created by wangchao on 2016/10/27.
 */
public class SpecificationModel {
    private Long id;
    private String name;
    private List<SingleModel> specificationValues;

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

    public List<SingleModel> getSpecificationValues() {
        return specificationValues;
    }

    public void setSpecificationValues(List<SingleModel> specificationValues) {
        this.specificationValues = specificationValues;
    }

    public void copyFrom(Goods goods, Specification specification) {
        this.id = specification.getId();
        this.name = specification.getName();
        if (specification.getId().equals(1L) && goods.getSpecification1Title() != null) {
            this.name = goods.getSpecification1Title();
        }
        if (specification.getId().equals(2L) && goods.getSpecification2Title() != null) {
            this.name = goods.getSpecification2Title();
        }
        List<SingleModel> models = new ArrayList<>();
        List<Long> specificationValueIds = new ArrayList<>();
        for (Product product : goods.getSortProducts()) {
            for (SpecificationValue specificationValue : product.getSpecificationValues()) {
                if (Objects.equals(specificationValue.getSpecification().getId(), specification.getId())) {
                    if (!specificationValueIds.contains(specificationValue.getId())) {
                        specificationValueIds.add(specificationValue.getId());
                        SingleModel model = new SingleModel();
                        model.setId(specificationValue.getId());
                        model.setName(specificationValue.getName());
                        models.add(model);
                    }
                }
            }
        }
        this.specificationValues = models;
    }

    public static List<SpecificationModel> bindData(Product product) {
        List<SpecificationModel> models = new ArrayList<>();
        for (SpecificationValue specificationValue : product.getSpecificationValues()) {
            SpecificationModel model = new SpecificationModel();
            model.copyFrom(product.getGoods(), specificationValue.getSpecification());
            models.add(model);
        }
        return models;
    }

}
