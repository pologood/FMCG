package net.wit.controller.app.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.wit.entity.SpecificationValue;

public class SpecificationValueModel extends BaseModel {
	/**ID**/
	private Long id;
	/**名称**/
	private String name;
	/**图标**/
	private String image;
	/**标题**/
	private String title;
	
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
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public void copyFrom(SpecificationValue specificationValue) {
		this.id = specificationValue.getSpecification().getId();
		this.name = specificationValue.getName();
		this.image = specificationValue.getImage();
		this.title = specificationValue.getSpecification().getName();
	}
	
	public static List<SpecificationValueModel> bindData(Set<SpecificationValue> specificationValues) {
		List<SpecificationValueModel> models = new ArrayList<>();
		for (SpecificationValue specificationValue:specificationValues) {
			SpecificationValueModel model = new SpecificationValueModel();
			model.copyFrom(specificationValue);
			models.add(model);
		}
		return models;
	}
	
}
