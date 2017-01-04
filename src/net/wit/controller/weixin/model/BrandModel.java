package net.wit.controller.weixin.model;

import net.wit.entity.Brand;

/**
 * 品牌
 * Created by wangchao on 2016/10/22.
 */
public class BrandModel {
    //ID
    private Long id;
    //名称
    private String name;
    //类型
    private Brand.Type type;
    //logo
    private String logo;
    //介绍
    private String introduction;

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

    public Brand.Type getType() {
        return type;
    }

    public void setType(Brand.Type type) {
        this.type = type;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public void copyFrom(Brand brand) {
        this.id=brand.getId();
        this.name=brand.getName();
        this.type=brand.getType();
        this.logo=brand.getLogo();
        this.introduction=brand.getIntroduction();
    }
}
