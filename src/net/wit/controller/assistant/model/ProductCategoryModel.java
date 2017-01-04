package net.wit.controller.assistant.model;

import net.wit.controller.assistant.model.BaseModel;
import net.wit.controller.assistant.model.TagModel;
import net.wit.entity.ProductCategory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProductCategoryModel extends BaseModel {
	/*分类ID*/
	private Long id;
	/*分类名*/
	private String name;
	/*分类图标*/
	private String image;
	/*分类图标*/
	private Integer grade;
	/*分类标签*/
	private Set<TagModel> tags;
	/*是否有下级*/
	private Boolean hasChildren;
	
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
	
	public Integer getGrade() {
		return grade;
	}
	public void setGrade(Integer grade) {
		this.grade = grade;
	}
	
	public void copyFrom(ProductCategory productCategory) {
		this.id = productCategory.getId();
		this.name = productCategory.getName();
		this.image = productCategory.getImage();
		this.grade = productCategory.getGrade();
		if (productCategory.getChildren()!=null) {
			if (productCategory.getChildren().size()>0) {
		    	this.hasChildren = true;
			} else {
		    	this.hasChildren = false;
			}
		} else {
			this.hasChildren = false;
		}
	}
	
	public static List<ProductCategoryModel> bindData(List<ProductCategory> productCategories) {
		List<ProductCategoryModel> models = new ArrayList<ProductCategoryModel>();
		for (ProductCategory productCategory:productCategories) {
			ProductCategoryModel model = new ProductCategoryModel();
			model.copyFrom(productCategory);
			models.add(model);
		}
		return models;
	}
	
	public static Set<ProductCategoryModel> bindData(Set<ProductCategory> productCategories) {
		Set<ProductCategoryModel> models = new HashSet<ProductCategoryModel>();
		for (ProductCategory productCategory:productCategories) {
			ProductCategoryModel model = new ProductCategoryModel();
			model.copyFrom(productCategory);
			models.add(model);
		}
		return models;
	}
}
