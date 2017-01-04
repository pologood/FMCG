package net.wit.controller.assistant.model;

import net.wit.controller.assistant.model.BaseModel;
import net.wit.entity.Product;
import net.wit.entity.Promotion;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PromotionSecKillModel extends BaseModel {
	/*活动id*/
	private Long id;
	/*活动名称*/
	private String name;

	/** 起始日期 */
	private Date beginDate;

	/** 结束日期 */
	private Date endDate;

	/** 促销价格 */
	private BigDecimal price;
	
	/** 商品ID */
	private Long productId;
	
	/** 商品名称 */
	private String fullName;
	
	/** 商品图片 */
	private String thumbnail;
	/*市场价*/
	private BigDecimal marketPrice;
	
	/*限购数量*/
	private Integer maximumQuantity;
	
		
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


	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
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

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public BigDecimal getMarketPrice() {
		return marketPrice;
	}

	public void setMarketPrice(BigDecimal marketPrice) {
		this.marketPrice = marketPrice;
	}
	
	public Integer getMaximumQuantity() {
		return maximumQuantity;
	}

	public void setMaximumQuantity(Integer maximumQuantity) {
		this.maximumQuantity = maximumQuantity;
	}

	public void copyFrom(Promotion promotion) {
		this.id = promotion.getId();
		this.name = promotion.getName();
		this.beginDate = promotion.getBeginDate();
		this.endDate = promotion.getEndDate();
		this.maximumQuantity = promotion.getMaximumQuantity();
		this.price = new BigDecimal(promotion.getPriceExpression());
		for (Product product:promotion.getProducts()) {
			this.fullName = product.getFullName();
			this.productId = product.getId();
			this.thumbnail = product.getThumbnail();
			this.marketPrice = product.getMarketPrice();
		}
		
	}
	
	public static List<PromotionSecKillModel> bindData(List<Promotion> promotions) {
		List<PromotionSecKillModel> models = new ArrayList<PromotionSecKillModel>();
		for (Promotion promotion:promotions) {
			PromotionSecKillModel model = new PromotionSecKillModel();
			model.copyFrom(promotion);
			models.add(model);
		}
		return models;
	}
			
}
