package net.wit.controller.assistant.model;

import net.wit.controller.assistant.model.BaseModel;
import net.wit.entity.Promotion;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public class PromotionMailModel extends BaseModel {
	/*活动id*/
	private Long id;
	/*活动名称*/
	private String name;
	/*活动名称*/
	private BigDecimal price;
		
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


	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public void copyFrom(Promotion promotion) {
		this.id = promotion.getId();
		this.name = promotion.getName();
		this.price = promotion.getMinimumPrice();
		
	}
	
	public static Set<PromotionMailModel> bindData(Set<Promotion> promotions) {
		Set<PromotionMailModel> models = new HashSet<PromotionMailModel>(promotions.size());
		for (Promotion promotion:promotions) {
			PromotionMailModel model = new PromotionMailModel();
			model.copyFrom(promotion);
			models.add(model);
		}
		return models;
	}
			
}
