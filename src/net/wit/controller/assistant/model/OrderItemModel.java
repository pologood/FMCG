package net.wit.controller.assistant.model;

import net.wit.controller.assistant.model.BaseModel;
import net.wit.controller.assistant.model.SingleModel;
import net.wit.entity.OrderItem;
import net.wit.entity.Promotion;
import net.wit.entity.SpecificationValue;

import java.math.BigDecimal;
import java.util.*;

public class OrderItemModel extends BaseModel {
	/*等级ID*/
	private Long id;
	/*商品*/
	private Long productId;
	/*全名*/
	private String fullName;
	/*名称*/
	private String name;
	/*规格*/
	private String spec;

	/*编码*/
	private String sn;
	/*单位*/
	private String unit;
	/*缩略图*/
	private String thumbnail;
	/*数量*/
	private Integer quantity;
	/*发货数量*/
	private Integer shippedQuantity;
	/*价格*/
	private BigDecimal price;
	/*价格*/
	private BigDecimal originalPrice;
	/*金额小计*/
	private BigDecimal subTotal;
	/*折扣金额*/
	private BigDecimal discount;
	/*促销*/
	private Set<SingleModel> promotions;
	/*创建日期*/
	private Date createDate;
	/*颜色*/
	private String color;


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
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public BigDecimal getSubTotal() {
		return subTotal;
	}
	public void setSubTotal(BigDecimal subTotal) {
		this.subTotal = subTotal;
	}
	public BigDecimal getDiscount() {
		return discount;
	}
	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}
	public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
	public Set<SingleModel> getPromotions() {
		return promotions;
	}
	public void setPromotions(Set<SingleModel> promotions) {
		this.promotions = promotions;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSpec() {
		return spec;
	}
	public void setSpec(String spec) {
		this.spec = spec;
	}

	public BigDecimal getOriginalPrice() {
		return originalPrice;
	}
	public void setOriginalPrice(BigDecimal originalPrice) {
		this.originalPrice = originalPrice;
	}

	public Integer getShippedQuantity() {
		return shippedQuantity;
	}
	public void setShippingQuantity(Integer shippedQuantity) {
		this.shippedQuantity = shippedQuantity;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public void copyFrom(OrderItem orderItem) {
		this.id = orderItem.getId();
	    this.quantity = orderItem.getQuantity();
	    this.shippedQuantity = orderItem.getShippedQuantity();
	    this.price = orderItem.getPrice();
	    this.subTotal = orderItem.getSubtotal();
	    this.discount = orderItem.getDiscount();
	    this.fullName = orderItem.getFullName();
	    this.unit = orderItem.getPackagUnitName();
	    this.thumbnail = orderItem.getThumbnail();
	    this.sn = orderItem.getSn();
	    this.name = orderItem.getName();
	    if (this.name.length()<this.fullName.length()) {
	    	this.spec = this.fullName.substring(this.name.length());
	    }
	    this.originalPrice = orderItem.getOriginalPrice();
	    if (orderItem.getProduct()!=null) {
	       this.productId = orderItem.getProduct().getId();
	    }
		Set<SingleModel> promotions = new HashSet<SingleModel>();
		if (orderItem.getProduct()!=null && orderItem.getProduct().getPromotions()!=null) {
			for (Promotion promotion:orderItem.getProduct().getPromotions()) {
				SingleModel model = new SingleModel();
				model.setId(promotion.getId());
				model.setName(promotion.getName());
				promotions.add(model);
			}
			this.promotions = promotions;
		}
		if (orderItem.getProduct()!=null&&orderItem.getProduct().getSpecificationValues()!=null) {
			for (SpecificationValue specificationValue : orderItem.getProduct().getSpecificationValues()) {
				if (specificationValue.getSpecification().getId().equals(1L)) {
					this.spec = specificationValue.getName();
				}
				if (specificationValue.getSpecification().getId().equals(2L)) {
					this.color = specificationValue.getName();
				}
			}
		}
		this.createDate=orderItem.getCreateDate();
	}
	
	public static List<OrderItemModel> bindData(List<OrderItem> orderItems) {
		List<OrderItemModel> models = new ArrayList<OrderItemModel>();
		for (OrderItem orderItem:orderItems) {
			OrderItemModel model = new OrderItemModel();
			model.copyFrom(orderItem);
			models.add(model);
		}
		return models;
	}
	
}
