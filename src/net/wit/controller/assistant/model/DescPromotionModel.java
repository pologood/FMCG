package net.wit.controller.assistant.model;

import net.wit.entity.Product;
import net.wit.entity.Promotion;
import net.wit.entity.Promotion.Type;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DescPromotionModel extends BaseModel {
	/*ID*/
	private Long id;
	private Type type;
	/*活动名称*/
	private String name;

	public Long getId() {
		return id;
	}


	public Type getType() {
		return type;
	}


	public void setType(Type type) {
		this.type = type;
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

	public void copyFrom(Promotion promotion) {
		this.id = promotion.getId();
		if(promotion.getType().equals(Type.activity)){
			this.name = "活动立减（平台主办";
		}else if(promotion.getType().equals(Type.coupon)){
			this.name = "送优惠券";
		}else if(promotion.getType().equals(Type.points)){
			this.name = "积分兑换";
		}else if(promotion.getType().equals(Type.mail)){
			this.name = "满额包邮  商家活动";
		}else if(promotion.getType().equals(Type.discount)){
			this.name = "满额折扣  商家活动";
		}else if(promotion.getType().equals(Type.seckill)){
			this.name = "秒杀、限时抢购 单品活动";
		}else if(promotion.getType().equals(Type.buyfree)){
			this.name = "买赠 单品活动";
		}
		this.type = promotion.getType();

	}
	
	public static List<DescPromotionModel> bindData(List<Promotion> promotions) {
		List<DescPromotionModel> models = new ArrayList<DescPromotionModel>(promotions.size());
		for (Promotion promotion:promotions) {
			DescPromotionModel model = new DescPromotionModel();
			model.copyFrom(promotion);
			models.add(model);
		}
		return models;
	}
			
}
