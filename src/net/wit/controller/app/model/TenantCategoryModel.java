package net.wit.controller.app.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.wit.entity.TenantCategory;

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
	
	private Set<TenantCategoryModel> childrens;
	
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
	
	public Set<TenantCategoryModel> getChildrens() {
		return childrens;
	}
	public void setChildrens(Set<TenantCategoryModel> childrens) {
		this.childrens = childrens;
	}
	
	public void copyFrom(TenantCategory tenantCategory) {
		this.id = tenantCategory.getId();
		this.name = tenantCategory.getName();
		this.image = tenantCategory.getImage();
		if (tenantCategory.getChildren()!=null) {
			if (tenantCategory.getChildren().size()>0) {
		    	this.hasChildren = true;
			} else {
		    	this.hasChildren = false;
			}
		} else {
			this.hasChildren = false;
		}
	}
	
	public static List<TenantCategoryModel> bindData(List<TenantCategory> tenantCategories) {
		List<TenantCategoryModel> models = new ArrayList<TenantCategoryModel>();
		for (TenantCategory tenantCategory:tenantCategories) {
			TenantCategoryModel model = new TenantCategoryModel();
			model.copyFrom(tenantCategory);
			models.add(model);
		}
		return models;
	}
	
	
	public static Set<TenantCategoryModel> bindData(Set<TenantCategory> tenantCategories) {
		Set<TenantCategoryModel> models = new HashSet<TenantCategoryModel>();
		for (TenantCategory tenantCategory:tenantCategories) {
			TenantCategoryModel model = new TenantCategoryModel();
			model.copyFrom(tenantCategory);
			models.add(model);
		}
		return models;
	}
	
	public static List<TenantCategoryModel> bindAllData(List<TenantCategory> tenantCategories) {
		List<TenantCategoryModel> models = new ArrayList<TenantCategoryModel>();
		for (TenantCategory tenantCategory:tenantCategories) {
			TenantCategoryModel model = new TenantCategoryModel();
			model.copyFrom(tenantCategory);
			model.childrens = TenantCategoryModel.bindData(tenantCategory.getChildren());
			models.add(model);
		}
		return models;
	}
}
