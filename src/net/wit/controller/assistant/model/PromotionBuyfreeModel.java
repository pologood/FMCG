package net.wit.controller.assistant.model;

import net.wit.controller.assistant.model.BaseModel;
import net.wit.entity.GiftItem;
import net.wit.entity.Promotion;
import net.wit.entity.PromotionProduct;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PromotionBuyfreeModel extends BaseModel {
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
	
	/*市场价*/
	private BigDecimal marketPrice;
	
	/** 购买数量 */
	private Integer minimumQuantity;
	
	/** 赠品数量 */
	private Integer giftQuantity;
	
	/** 商品ID */
	private Long productId;
	
	/** 商品名称 */
	private String fullName;
	
	/** 商品图片 */
	private String thumbnail;
	
	/** 赠品ID */
	private Long giftProductId;
	
	/** 赠品名称 */
	private String giftFullName;
	
	/** 赠品图片 */
	private String giftThumbnail;
	
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
		
	public Integer getMinimumQuantity() {
		return minimumQuantity;
	}

	public void setMinimumQuantity(Integer minimumQuantity) {
		this.minimumQuantity = minimumQuantity;
	}

	public Integer getGiftQuantity() {
		return giftQuantity;
	}

	public void setGiftQuantity(Integer giftQuantity) {
		this.giftQuantity = giftQuantity;
	}

	public Long getGiftProductId() {
		return giftProductId;
	}

	public void setGiftProductId(Long giftProductId) {
		this.giftProductId = giftProductId;
	}

	public String getGiftFullName() {
		return giftFullName;
	}

	public void setGiftFullName(String giftFullName) {
		this.giftFullName = giftFullName;
	}

	public String getGiftThumbnail() {
		return giftThumbnail;
	}

	public void setGiftThumbnail(String giftThumbnail) {
		this.giftThumbnail = giftThumbnail;
	}

	public Integer getMaximumQuantity() {
		return maximumQuantity;
	}

	public void setMaximumQuantity(Integer maximumQuantity) {
		this.maximumQuantity = maximumQuantity;
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

	public void copyFrom(Promotion promotion) {
		this.id = promotion.getId();
		this.name = promotion.getName();
		this.beginDate = promotion.getBeginDate();
		this.endDate = promotion.getEndDate();
		this.maximumQuantity = promotion.getMaximumQuantity();
		this.minimumQuantity = promotion.getMinimumQuantity();
		for (PromotionProduct promotionProduct:promotion.getPromotionProducts()) {
			this.fullName = promotionProduct.getProduct().getFullName();
			this.productId = promotionProduct.getProduct().getId();
			this.thumbnail = promotionProduct.getProduct().getThumbnail();
			this.price = promotionProduct.getProduct().getPrice();
			this.marketPrice = promotionProduct.getProduct().getMarketPrice();
		}
		for (GiftItem giftItem:promotion.getGiftItems()) {
			this.giftFullName = giftItem.getGift().getFullName();
			this.giftProductId = giftItem.getGift().getId();
			this.giftThumbnail = giftItem.getGift().getThumbnail();
			this.giftQuantity = giftItem.getQuantity();
		}
	}
	
	public static List<PromotionBuyfreeModel> bindData(List<Promotion> promotions) {
		List<PromotionBuyfreeModel> models = new ArrayList<PromotionBuyfreeModel>();
		for (Promotion promotion:promotions) {
			PromotionBuyfreeModel model = new PromotionBuyfreeModel();
			model.copyFrom(promotion);
			models.add(model);
		}
		return models;
	}
			
}
