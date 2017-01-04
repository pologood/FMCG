package net.wit.controller.assistant.model;

import net.wit.controller.assistant.model.BaseModel;
import net.wit.entity.Parameter;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ParameterValueModel extends BaseModel {
	/*参数ID*/
	private Long id;
	/*参数名*/
	private String name;
	/*参数值*/
	private String value;
	
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
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	public void copyFrom(Parameter parameter,String parameterValue) {
		this.id = parameter.getId();
		this.name = parameter.getName();
		this.value = parameterValue;
	}
	
	public static Set<ParameterValueModel> bindData(Map<Parameter,String> parameters) {
		Set<ParameterValueModel> models = new HashSet<ParameterValueModel>(parameters.size());
		for (Parameter parameter:parameters.keySet()) {
			ParameterValueModel model = new ParameterValueModel();
			model.copyFrom(parameter,parameters.get(parameter));
			models.add(model);
		}
		return models;
	}
	
}
