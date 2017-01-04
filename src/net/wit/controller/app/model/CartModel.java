package net.wit.controller.app.model;

import net.wit.entity.Cart;
import net.wit.entity.Tenant;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public class CartModel extends BaseModel {
	/*等级ID*/
	private Long id;
	/*有效商品数*/
	private int  quantity;
	/*有效商品数*/
	private int  effectiveQuantity;
	/*有效商品积分*/
	private Long effectivePoint;
	
	/*有效金额*/
	private BigDecimal freightPrice;
	/*优惠立减*/
	private BigDecimal discountPrice;
	/*有效金额*/
	private BigDecimal effectivePrice;
	/*订单项*/
	private List<CartItemModel> cartItems;
	/*礼物项*/
	private Set<GiftItemModel> giftItems;
	/*店铺*/
	private List<CartTenantModel> tenants;
		
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getEffectiveQuantity() {
		return effectiveQuantity;
	}

	public void setEffectiveQuantity(int effectiveQuantity) {
		this.effectiveQuantity = effectiveQuantity;
	}

	public BigDecimal getEffectivePrice() {
		return effectivePrice;
	}

	public void setEffectivePrice(BigDecimal effectivePrice) {
		this.effectivePrice = effectivePrice;
	}	
	
	public Long getEffectivePoint() {
		return effectivePoint;
	}

	public void setEffectivePoint(Long effectivePoint) {
		this.effectivePoint = effectivePoint;
	}

	public List<CartItemModel> getCartItems() {
		return cartItems;
	}

	public void setCartItems(List<CartItemModel> cartItems) {
		this.cartItems = cartItems;
	}

	public BigDecimal getFreightPrice() {
		return freightPrice;
	}

	public void setFreightPrice(BigDecimal freightPrice) {
		this.freightPrice = freightPrice;
	}

	public BigDecimal getDiscountPrice() {
		return discountPrice;
	}

	public void setDiscountPrice(BigDecimal discountPrice) {
		this.discountPrice = discountPrice;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Set<GiftItemModel> getGiftItems() {
		return giftItems;
	}

	public void setGiftItems(Set<GiftItemModel> giftItems) {
		this.giftItems = giftItems;
	}

	public List<CartTenantModel> getTenants() {
		return tenants;
	}

	public void setTenants(List<CartTenantModel> tenants) {
		this.tenants = tenants;
	}

	public void copyFrom(Cart cart) {
		this.id = cart.getId();
		this.quantity = cart.getQuantity();
		this.effectivePrice = cart.getEffectivePrice();
		this.effectivePoint = cart.getEffectivePoint();
		this.effectiveQuantity = cart.getEffectiveQuantity();
		this.discountPrice = cart.getDiscount();
		this.freightPrice = cart.getFreight();
		if (cart.getCartItems()!=null) {
		   this.cartItems = CartItemModel.bindData(cart.getCartItems());
		}
		if (cart.getGiftItems()!=null) {
		   this.giftItems = GiftItemModel.bindData(cart.getGiftItems());
		}
		if(cart.getTenants()!=null){
			this.tenants = CartTenantModel.bindData(cart);
		}
	}
	
}
