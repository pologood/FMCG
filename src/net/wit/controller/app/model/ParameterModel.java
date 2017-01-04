package net.wit.controller.app.model;

import java.util.ArrayList;
import java.util.List;

import net.wit.entity.Parameter;

public class ParameterModel extends BaseModel {
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
	
	public void copyFrom(Parameter parameter) {
		this.id = parameter.getId();
		this.name = parameter.getName();
	}
	
	public static List<ParameterModel> bindData(List<Parameter> Parameters) {
		List<ParameterModel> models = new ArrayList<ParameterModel>();
		for (Parameter parameter:Parameters) {
			ParameterModel model = new ParameterModel();
			model.copyFrom(parameter);
			models.add(model);
		}
		return models;
	}
	
}
