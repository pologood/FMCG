package net.wit.controller.assistant.model;

import net.wit.controller.app.model.BaseModel;
import net.wit.controller.app.model.PromotionSingleModel;
import net.wit.controller.app.model.TagModel;
import net.wit.entity.Product;
import net.wit.entity.Promotion;

import java.math.BigDecimal;
import java.util.*;

public class ProductListModel extends BaseModel {
	/*商品id*/
	private Long id;
	/*编码*/
	private String sn;
	/*条型码*/
	private String barcode;
	/*全名*/
	private String fullName;
	/*缩略图*/
	private String thumbnail;
	/*中等图*/
	private String medium;
	/*销售价*/
	private BigDecimal price;
	/*市场价*/
	private BigDecimal marketPrice;
	/*单位*/
	private String unit;
	/*是否多规格*/
	private Boolean hasSpecication;
	/*库存*/
	private Integer stock;
	/*销量*/
	private Long sales;
	/*点击数*/
	private Long hits;
	/*最后修改时间*/
	private Date modify_date;
	/*促销*/
	private Set<PromotionSingleModel> promotions;
	/*好评度*/
	private Double positivePercent;
	/*标签*/
	private Set<TagModel> tags;
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public BigDecimal getMarketPrice() {
		return marketPrice;
	}

	public void setMarketPrice(BigDecimal marketPrice) {
		this.marketPrice = marketPrice;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public Long getSales() {
		return sales;
	}

	public void setSales(Long sales) {
		this.sales = sales;
	}

	public Long getHits() {
		return hits;
	}

	public void setHits(Long hits) {
		this.hits = hits;
	}

	public Date getModify_date() {
		return modify_date;
	}

	public void setModify_date(Date modify_date) {
		this.modify_date = modify_date;
	}		

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public Set<PromotionSingleModel> getPromotions() {
		return promotions;
	}

	public void setPromotions(Set<PromotionSingleModel> promotions) {
		this.promotions = promotions;
	}	

	public String getMedium() {
		return medium;
	}

	public void setMedium(String medium) {
		this.medium = medium;
	}

	public Boolean getHasSpecication() {
		return hasSpecication;
	}

	public void setHasSpecication(Boolean hasSpecication) {
		this.hasSpecication = hasSpecication;
	}

	public Double getPositivePercent() {
		return positivePercent;
	}

	public void setPositivePercent(Double positivePercent) {
		this.positivePercent = positivePercent;
	}

	public Set<TagModel> getTags() {
		return tags;
	}

	public void setTags(Set<TagModel> tags) {
		this.tags = tags;
	}

	public void copyFrom(Product product) {
		this.id = product.getId();
		this.fullName = product.getName();
		this.thumbnail = product.getThumbnail();
		this.medium = product.getMedium();
		this.price = product.calcEffectivePrice(null);
		this.marketPrice = product.getMarketPrice();
		this.unit = product.getUnit();
		Integer stock = product.getGoods().getStock()-product.getGoods().getAllocatedStock();
		this.stock =stock < 0 ? 0 : stock;
		this.sales = product.getGoods().getMonthSales();
		this.hits = product.getGoods().getHits();
		this.modify_date = product.getModifyDate();
		this.sn = product.getSn();
		this.barcode = product.getBarcode();
		this.hasSpecication = product.getSpecifications().size()>0;
		Set<PromotionSingleModel> promotions = new HashSet<PromotionSingleModel>();
		for (Promotion promotion:product.getValidPromotions()) {
			PromotionSingleModel model = new PromotionSingleModel();
			model.setId(promotion.getId());
			model.setName(promotion.getName());
			model.setType(promotion.getType());
			promotions.add(model);
		}
		this.promotions = promotions;
		this.positivePercent = 0.98D;
		this.tags=TagModel.bindData(product.getTags());
	}
	
	public static List<ProductListModel> bindData(List<Product> products) {
		List<ProductListModel> models = new ArrayList<ProductListModel>();
		for (Product product:products) {
			ProductListModel model = new ProductListModel();
			model.copyFrom(product);
			models.add(model);
		}
		return models;
	}
	
}
