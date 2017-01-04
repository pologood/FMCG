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

import net.wit.entity.SpReturnsItem;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import net.wit.entity.Shipping;
import net.wit.entity.ShippingItem;
import net.wit.entity.ShippingMethod;
import net.wit.entity.ShippingMethod.Method;

public class SpReturnItemModel extends BaseModel {
	
	/** ID */
	private Long id;
	
	/** 商品编号 */
	private String sn;

	/** 商品名称 */
	private String name;

	/** 退货数量 */
	private Integer returnQuantity;

	/*缩略图*/
	private String thumbnail;

	/*价格*/
	private BigDecimal price;

	/*原价*/
	private BigDecimal originalPrice;

	/*规格*/
	private String spec;

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

	public Integer getReturnQuantity() {
		return returnQuantity;
	}

	public void setReturnQuantity(Integer returnQuantity) {
		this.returnQuantity = returnQuantity;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getOriginalPrice() {
		return originalPrice;
	}

	public void setOriginalPrice(BigDecimal originalPrice) {
		this.originalPrice = originalPrice;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public void copyFrom(SpReturnsItem spReturnsItem) {
		this.id = spReturnsItem.getId();
		this.sn = spReturnsItem.getSn();
		this.name = spReturnsItem.getName();
		this.returnQuantity = spReturnsItem.getReturnQuantity();
		this.thumbnail=spReturnsItem.getOrderItem().getThumbnail();
		this.price=spReturnsItem.getOrderItem().getPrice();
		this.originalPrice=spReturnsItem.getOrderItem().getOriginalPrice();
		this.spec="";
		if(spReturnsItem.getOrderItem().getName().length()<spReturnsItem.getOrderItem().getFullName().length()){
			this.spec=spReturnsItem.getOrderItem().getFullName().substring(spReturnsItem.getOrderItem().getName().length());
		}
	}
	
	public static List<SpReturnItemModel> bindData(List<SpReturnsItem> spReturnsItems) {
		List<SpReturnItemModel> models = new ArrayList<>();
		for (SpReturnsItem spReturnsItem:spReturnsItems) {
			SpReturnItemModel model = new SpReturnItemModel();
			model.copyFrom(spReturnsItem);
			models.add(model);
		}
		return models;
	}
	
}
