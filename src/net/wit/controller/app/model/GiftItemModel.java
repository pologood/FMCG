package net.wit.controller.app.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.wit.entity.GiftItem;
import net.wit.entity.OrderItem;
import net.wit.entity.Promotion;

public class GiftItemModel extends BaseModel {
	/*等级ID*/
	private Long id;
	/*商品*/
	private Long productId;
	/*全名*/
	private String fullName;
	/*单位*/
	private String unit;
	/*缩略图*/
	private String thumbnail;
	/*数量*/
	private Integer quantity;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getThumbnail() {
		return thumbnail;
	}
	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public void copyFrom(GiftItem giftItem) {
		this.id = giftItem.getId();
	    this.quantity = giftItem.getQuantity();
	    this.fullName = giftItem.getGift().getFullName();
	    this.unit = giftItem.getGift().getUnit();
	    this.thumbnail = giftItem.getGift().getThumbnail();
	}
	
	public static Set<GiftItemModel> bindData(Set<GiftItem> giftItems) {
		Set<GiftItemModel> models = new HashSet<GiftItemModel>();
		for (GiftItem giftItem:giftItems) {
			GiftItemModel model = new GiftItemModel();
			model.copyFrom(giftItem);
			models.add(model);
		}
		return models;
	}
	
}
