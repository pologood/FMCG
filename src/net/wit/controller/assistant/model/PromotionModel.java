package net.wit.controller.assistant.model;

import net.wit.controller.assistant.model.BaseModel;
import net.wit.entity.Product;
import net.wit.entity.Promotion;
import net.wit.entity.Promotion.Type;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PromotionModel extends BaseModel {
	/*ID*/
	private Long id;
	private Type type;
	/*活动名称*/
	private String name;
	/*活动名称*/
	private String title;
	/*活动商品*/
	private String fullName;
	/*活动商家*/
	private String tenantName;
	/** 促销价格 */
	private BigDecimal price;
	/*市场价*/
	private BigDecimal marketPrice;
	private Long productId;
	/** 商品图片 */
	private String thumbnail;
	/** 起始日期 */
	private Date beginDate;
	/** 结束日期 */
	private Date endDate;

	/** 平均评分 */
	private Float score;
	/** 点击数 */
	private Long hits;

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


	public String getFullName() {
		return fullName;
	}


	public void setFullName(String fullName) {
		this.fullName = fullName;
	}


	public String getTenantName() {
		return tenantName;
	}


	public void setTenantName(String tenantName) {
		this.tenantName = tenantName;
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


	public String getThumbnail() {
		return thumbnail;
	}


	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
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


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}

	public Long getProductId() {
		return productId;
	}


	public void setProductId(Long productId) {
		this.productId = productId;
	}


	public Float getScore() {
		return score;
	}


	public void setScore(Float score) {
		this.score = score;
	}


	public Long getHits() {
		return hits;
	}


	public void setHits(Long hits) {
		this.hits = hits;
	}


	public void copyFrom(Promotion promotion) {
		this.id = promotion.getId();
		this.name = promotion.getName();
		this.title = promotion.getTitle();
		if (this.title==null) {
			this.title = promotion.getName();
		}
		this.beginDate = promotion.getBeginDate();
		this.endDate = promotion.getEndDate();
		Product product = promotion.getDefaultProduct();
		if (product!=null) {
			if (product.getId()==null) {
				product=null;
			} else {
			    this.fullName = product.getFullName();
			    this.thumbnail = product.getThumbnail();
				this.score = product.getScore();
				this.hits = product.getHits();
				this.productId = product.getId();
			}
		}
		if (promotion.getTenant()!=null) {
		    this.tenantName = promotion.getTenant().getShortName();
		}
		this.type = promotion.getType();
		if (product!=null) {
			if (promotion.getType().equals(Type.buyfree)) {
				this.price = product.getPrice();
				this.marketPrice = product.getMarketPrice();
			} else if (promotion.getType().equals(Type.seckill)) {
				this.price = new BigDecimal(promotion.getPriceExpression());
				this.marketPrice = product.getMarketPrice();
			}
		}
	}
	
	public static List<PromotionModel> bindData(List<Promotion> promotions) {
		List<PromotionModel> models = new ArrayList<PromotionModel>(promotions.size());
		for (Promotion promotion:promotions) {
			PromotionModel model = new PromotionModel();
			model.copyFrom(promotion);
			models.add(model);
		}
		return models;
	}
			
}
