package net.wit.controller.app.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.wit.entity.Attribute;

public class AttributeModel extends BaseModel {
	/*分类ID*/
	private Long id;
	/*分类名*/
	private String name;
	
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
	
	public void copyFrom(Attribute attribute) {
		this.id = attribute.getId();
		this.name = attribute.getName();
	}
	
	public static Set<AttributeModel> bindData(Set<Attribute> attributes) {
		Set<AttributeModel> models =  new HashSet<AttributeModel>(attributes.size());
		for (Attribute attribute:attributes) {
			AttributeModel model = new AttributeModel();
			model.copyFrom(attribute);
			models.add(model);
		}
		return models;
	}
	
}
