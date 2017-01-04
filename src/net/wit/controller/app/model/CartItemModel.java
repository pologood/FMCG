package net.wit.controller.app.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.wit.entity.CartItem;
import net.wit.entity.Product;
import net.wit.entity.Promotion;

public class CartItemModel extends BaseModel {
	/*等级ID*/
	private Long id;
	/*商品*/
	private Long productId;
	/*全名*/
	private String fullName;
	/*名称*/
	private String name;
	/*规格*/
	private List<SpecificationValueModel> specification;
	/*单位*/
	private String unit;
	/*缩略图*/
	private String thumbnail;
	/*数量*/
	private Integer quantity;
	/*价格*/
	private BigDecimal price;
	/*市场价*/
	private BigDecimal marketPrice;
	/*金额小计*/
	private BigDecimal subTotal;
	/*折扣金额*/
	private BigDecimal discount;
	/*选择*/
	private Boolean selected;
	
	/*促销*/
	private Set<PromotionSingleModel> promotions;
	
	
	
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

	public BigDecimal getMarketPrice() {
		return marketPrice;
	}

	public void setMarketPrice(BigDecimal marketPrice) {
		this.marketPrice = marketPrice;
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
	
	public Set<PromotionSingleModel> getPromotions() {
		return promotions;
	}
	public void setPromotions(Set<PromotionSingleModel> promotions) {
		this.promotions = promotions;
	}
	public Boolean getSelected() {
		return selected;
	}
	public void setSelected(Boolean selected) {
		this.selected = selected;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public List<SpecificationValueModel> getSpecification() {
		return specification;
	}

	public void setSpecification(List<SpecificationValueModel> specification) {
		this.specification = specification;
	}

	public void copyFrom(CartItem cartItem) {
		this.id = cartItem.getId();
		this.quantity = cartItem.getQuantity();
		this.price = cartItem.getEffectivePrice();
		this.marketPrice = cartItem.getProduct().getMarketPrice();
		this.subTotal = cartItem.getSubtotal();
		this.discount = BigDecimal.ZERO;
		this.productId = cartItem.getProduct().getId();
		this.fullName = cartItem.getProduct().getFullName();
		this.name = cartItem.getProduct().getName();
		this.unit = cartItem.getProduct().getUnit();
		this.thumbnail = cartItem.getProduct().getThumbnail();
		Set<PromotionSingleModel> promotions = new HashSet<PromotionSingleModel>();
		for (Promotion promotion:cartItem.getProduct().getPromotions()) {
			PromotionSingleModel model = new PromotionSingleModel();
			model.setId(promotion.getId());
			model.setName(promotion.getName());
			model.setType(promotion.getType());
			promotions.add(model);
		}
		this.promotions = promotions;
		this.selected = cartItem.getSelected();
		Product product = cartItem.getProduct();
		if (product!=null) {
		   this.specification =	SpecificationValueModel.bindData(product.getSpecificationValues());
		}
	}
	
	
	public static List<CartItemModel> bindData(Set<CartItem> cartItems) {
		List<CartItemModel> models = new ArrayList<>(cartItems.size());
		for (CartItem cartItem:cartItems) {
			CartItemModel model = new CartItemModel();
			model.copyFrom(cartItem);
			models.add(model);
		}
		return models;
	}
	
}
