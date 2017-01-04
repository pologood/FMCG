package net.wit.controller.app.model;

import java.util.HashSet;
import java.util.Set;

import net.wit.entity.Specification;

public class SpecificationModel extends BaseModel {
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
	
	public void copyFrom(Specification specification) {
		this.id = specification.getId();
		this.name = specification.getName();
	}
	
	public static Set<SpecificationModel> bindData(Set<Specification> specifications) {
		Set<SpecificationModel> models = new HashSet<SpecificationModel>(specifications.size());
		for (Specification specification:specifications) {
			SpecificationModel model = new SpecificationModel();
			model.copyFrom(specification);
			models.add(model);
		}
		return models;
	}
	
}
