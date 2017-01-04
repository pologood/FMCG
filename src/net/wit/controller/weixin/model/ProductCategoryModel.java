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
    /*分类等级*/
    private Integer grade;
    /*分类标签*/
    private List<TagModel> tags;
    /*是否有下级*/
    private Boolean hasChildren;
    /*下级分类*/
    private List<ProductCategoryModel> childrens;

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

    public List<TagModel> getTags() {
        return tags;
    }

    public void setTags(List<TagModel> tags) {
        this.tags = tags;
    }

    public Boolean getHasChildren() {
        return hasChildren;
    }

    public void setHasChildren(Boolean hasChildren) {
        this.hasChildren = hasChildren;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public List<ProductCategoryModel> getChildrens() {
        return childrens;
    }

    public void setChildrens(List<ProductCategoryModel> childrens) {
        this.childrens = childrens;
    }

    public void copyFrom(ProductCategory productCategory) {
        this.id = productCategory.getId();
        this.name = productCategory.getName();
        this.image = productCategory.getImage();
        this.grade = productCategory.getGrade();
        this.tags = TagModel.bindData(new ArrayList<>(productCategory.getTags()));
        this.hasChildren = productCategory.getChildren().size() > 0;
    }

    public static List<ProductCategoryModel> bindAllData(List<ProductCategory> productCategories) {
        List<ProductCategoryModel> models = new ArrayList<>();
        for (ProductCategory productCategory : productCategories) {
            ProductCategoryModel model = new ProductCategoryModel();
            model.copyFrom(productCategory);
            if(productCategory.getChildren().size()>0){
                model.setChildrens(ProductCategoryModel.bindAllData(new ArrayList<>(productCategory.getChildren())));
            }
            models.add(model);
        }
        return models;
    }

}
