package net.wit.controller.app.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.wit.entity.Parameter;
import net.wit.entity.ParameterGroup;

public class ParameterGroupModel extends BaseModel {
	/*分组ID*/
	private Long id;
	/*分组名*/
	private String name;
	/*数据集*/
	private List<ParameterModel> parameters;
	
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
	public List<ParameterModel> getParameters() {
		return parameters;
	}
	public void setParameters(List<ParameterModel> parameters) {
		this.parameters = parameters;
	}

	public void copyFrom(ParameterGroup parameterGroup) {
		this.id = parameterGroup.getId();
		this.name = parameterGroup.getName();
		this.parameters = ParameterModel.bindData(parameterGroup.getParameters());
	}
	
	public static Set<ParameterGroupModel> bindData(Set<ParameterGroup> ParameterGroups) {
		Set<ParameterGroupModel> models = new HashSet<ParameterGroupModel>(ParameterGroups.size());
		for (ParameterGroup parameterGroup:ParameterGroups) {
			ParameterGroupModel model = new ParameterGroupModel();
			model.copyFrom(parameterGroup);
			models.add(model);
		}
		return models;
	}
	
}
