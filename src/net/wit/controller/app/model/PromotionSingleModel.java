package net.wit.controller.app.model;

import net.wit.entity.Promotion;

public class PromotionSingleModel extends BaseModel {
	/*id*/
	private Long id;
	/*名称*/
	private String name;
	/*类型*/
	private Promotion.Type type;
	
	
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
	public Promotion.Type getType() {
		return type;
	}
	public void setType(Promotion.Type type) {
		this.type = type;
	}
}
