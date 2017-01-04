package net.wit.controller.app.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.wit.entity.ProductCategoryTenant;

public class ProductCategoryTenantModel extends BaseModel {
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
	/*有几个商品*/
	private Integer products;
	private Set<ProductCategoryTenantModel> childrens;
	
	
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
	
	public Integer getProducts() {
		return products;
	}
	public void setProducts(Integer products) {
		this.products = products;
	}
		
	public Set<ProductCategoryTenantModel> getChildrens() {
		return childrens;
	}
	public void setChildrens(Set<ProductCategoryTenantModel> childrens) {
		this.childrens = childrens;
	}
	
	public void copyFrom(ProductCategoryTenant productCategory) {
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
		this.products = productCategory.getProducts().size();
	}
	
	public static List<ProductCategoryTenantModel> bindData(List<ProductCategoryTenant> productCategories) {
		List<ProductCategoryTenantModel> models = new ArrayList<ProductCategoryTenantModel>();
		for (ProductCategoryTenant productCategory:productCategories) {
			ProductCategoryTenantModel model = new ProductCategoryTenantModel();
			model.copyFrom(productCategory);
			models.add(model);
		}
		return models;
	}
	
	public static List<ProductCategoryTenantModel> bindAllData(List<ProductCategoryTenant> productCategories) {
		List<ProductCategoryTenantModel> models = new ArrayList<ProductCategoryTenantModel>();
		for (ProductCategoryTenant productCategory:productCategories) {
			ProductCategoryTenantModel model = new ProductCategoryTenantModel();
			model.copyFrom(productCategory);
			models.add(model);
			model.setChildrens(new HashSet<ProductCategoryTenantModel>());
			for (ProductCategoryTenant category:productCategory.getChildren()) {
				ProductCategoryTenantModel child = new ProductCategoryTenantModel();
				child.copyFrom(category);
				model.getChildrens().add(child);
			}
		}
		return models;
	}
}
