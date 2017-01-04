package net.wit.controller.weixin.model;

import net.wit.entity.Promotion;
import net.wit.entity.Promotion.Type;

import java.util.ArrayList;
import java.util.List;

public class PromotionModel extends BaseModel {
    /*ID*/
    private Long id;
    /*活动类型*/
    private Type type;
    /*活动名称*/
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void copyFrom(Promotion promotion) {
        this.id = promotion.getId();
        this.type = promotion.getType();
        this.name = promotion.getName();
    }

    public static List<PromotionModel> bindData(List<Promotion> promotions) {
        List<PromotionModel> models = new ArrayList<>();
        for (Promotion promotion : promotions) {
            PromotionModel model = new PromotionModel();
            model.copyFrom(promotion);
            models.add(model);
        }
        return models;
    }

}
