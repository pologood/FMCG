package net.wit.controller.app.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import net.wit.entity.Shipping;
import net.wit.entity.ShippingItem;
import net.wit.entity.ShippingMethod;
import net.wit.entity.ShippingMethod.Method;

public class ShippingItemModel extends BaseModel {
	
	/** 编号 */
	private Long id;
	
	/** 编号 */
	private String sn;

	/** 商品名称 */
	private String name;

	/** 数量 */
	private Integer quantity;

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public void copyFrom(ShippingItem shippingItem) {
		this.id = shippingItem.getId();
		this.sn = shippingItem.getSn();
		this.name = shippingItem.getName();
		this.quantity = shippingItem.getQuantity();
	}
	
	public static List<ShippingItemModel> bindData(List<ShippingItem> shippingItems) {
		List<ShippingItemModel> models = new ArrayList<ShippingItemModel>();
		for (ShippingItem shippingItem:shippingItems) {
			ShippingItemModel model = new ShippingItemModel();
			model.copyFrom(shippingItem);
			models.add(model);
		}
		return models;
	}
	
}
