package net.wit.controller.app.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import net.wit.entity.ContactProduct;

public class ContactProductModel extends BaseModel {
	/*商品名称*/
	private String name;
	/*全名*/
	private String fullName;
	/*编码*/
	private String sn;

	private BigDecimal price;
	
	private BigDecimal marketPrice;

	private String large;
	
	private String medium;
	
	private String thumbnail;

	private String introduction;
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getMarketPrice() {
		return marketPrice;
	}

	public void setMarketPrice(BigDecimal marketPrice) {
		this.marketPrice = marketPrice;
	}

	public String getLarge() {
		return large;
	}

	public void setLarge(String large) {
		this.large = large;
	}

	public String getMedium() {
		return medium;
	}

	public void setMedium(String medium) {
		this.medium = medium;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public void copyFrom(ContactProduct product) {
		this.name = product.getName();
		this.fullName = product.getFullName();
		this.sn = product.getSn();
		this.price = product.getPrice();
		this.marketPrice = product.getMarketPrice();
		this.large = product.getLarge();
		this.medium = product.getMedium();
		this.thumbnail = product.getThumbnail();
		this.introduction = product.getIntroduction();
	}
	
	public static List<ContactProductModel> bindData(List<ContactProduct> products) {
		List<ContactProductModel> models = new ArrayList<ContactProductModel>();
		for (ContactProduct product:products) {
			ContactProductModel model = new ContactProductModel();
			model.copyFrom(product);
			models.add(model);
		}
		return models;
	}
	
}
