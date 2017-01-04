package net.wit.controller.assistant.model;

import net.wit.controller.assistant.model.BaseModel;

public class SingleModel extends BaseModel {
	/*id*/
	private Long id;
	/*名称*/
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
	
}
