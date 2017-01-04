package net.wit.controller.app.model;

import net.wit.entity.Promotion;

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
