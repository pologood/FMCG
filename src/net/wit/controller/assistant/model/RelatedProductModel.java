package net.wit.controller.assistant.model;

import net.wit.controller.app.model.*;
import net.wit.entity.Product;
import net.wit.entity.Promotion;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public class RelatedProductModel extends BaseModel {
	/*商品ID*/
	private Long id;
	/*商品SN*/
	private String sn;
	/*商品名称*/
	private String name;
	/*全名描述*/
	private String fullName;
	/*缩略图*/
	private String thumbnail;
	/*展示图片 */
	private String image;
	/*销售价*/
	private BigDecimal price;
	/*促销*/
	private Set<PromotionModel> promotions;

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


	public String getFullName() {
		return fullName;
	}


	public void setFullName(String fullName) {
		this.fullName = fullName;
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

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Set<PromotionModel> getPromotions() {
		return promotions;
	}

	public void setPromotions(Set<PromotionModel> promotions) {
		this.promotions = promotions;
	}

	public void copyFrom(Product product) {
		this.id = product.getId();
		this.sn = product.getSn();
		this.name = product.getName();
		this.fullName = product.getFullName();
		this.price = product.getPrice();
		this.image = product.getImage();
		Set<PromotionModel> promotions = new HashSet<PromotionModel>();
		for (Promotion promotion:product.getPromotions()) {
			PromotionModel model = new PromotionModel();
			model.copyFrom(promotion);
			promotions.add(model);
		}

		this.promotions = promotions;
		this.thumbnail = product.getThumbnail();
	}

}
