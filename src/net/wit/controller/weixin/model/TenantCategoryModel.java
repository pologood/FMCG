package net.wit.controller.weixin.model;

import net.wit.entity.TenantCategory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TenantCategoryModel extends BaseModel {
    /*分类ID*/
    private Long id;
    /*分类名*/
    private String name;
    /*分类图标*/
    private String image;
    /*分类标签*/
    private Set<TagModel> tags;
    /*是否有下级*/
    private Boolean hasChildren;
    /*下级分类*/
    private List<TenantCategoryModel> childrens;

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

    public Set<TagModel> getTags() {
        return tags;
    }

    public void setTags(Set<TagModel> tags) {
        this.tags = tags;
    }

    public Boolean getHasChildren() {
        return hasChildren;
    }

    public void setHasChildren(Boolean hasChildren) {
        this.hasChildren = hasChildren;
    }

    public List<TenantCategoryModel> getChildrens() {
        return childrens;
    }

    public void setChildrens(List<TenantCategoryModel> childrens) {
        this.childrens = childrens;
    }

    public void copyFrom(TenantCategory tenantCategory) {
        this.id = tenantCategory.getId();
        this.name = tenantCategory.getName();
        this.image = tenantCategory.getImage();
        this.hasChildren = tenantCategory.getChildren().size() > 0;
    }
    //绑定商家分类
    public static List<TenantCategoryModel> bindData(List<TenantCategory> tenantCategories) {
        List<TenantCategoryModel> models = new ArrayList<>();
        for (TenantCategory tenantCategory : tenantCategories) {
            TenantCategoryModel model = new TenantCategoryModel();
            model.copyFrom(tenantCategory);
            models.add(model);
        }
        return models;
    }
    //绑定数据（递归出所有子级分类）
    public static List<TenantCategoryModel> bindAllData(List<TenantCategory> tenantCategories) {
        List<TenantCategoryModel> models = new ArrayList<>();
        for (TenantCategory tenantCategory : tenantCategories) {
            TenantCategoryModel model = new TenantCategoryModel();
            model.copyFrom(tenantCategory);
            if(tenantCategory.getChildren().size()>0){
                model.childrens = TenantCategoryModel.bindAllData(new ArrayList<>(tenantCategory.getChildren()));
            }
            models.add(model);
        }
        return models;
    }
}
