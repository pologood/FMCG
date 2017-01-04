/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import net.wit.Setting;
import net.wit.util.SettingUtils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.time.DateUtils;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @ClassName: Cart
 * @Description: 购物车
 * @author Administrator
 * @date 2014年10月14日 上午9:08:56
 */
@Entity
@Table(name = "xx_cart")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_cart_sequence")
public class Cart extends BaseEntity {

	private static final long serialVersionUID = -6565967051825794561L;

	/** 超时时间 */
	public static final int TIMEOUT = 604800;

	/** 最大商品数 */
	public static final Integer MAX_PRODUCT_COUNT = 100;

	/** "ID"Cookie名称 */
	public static final String ID_COOKIE_NAME = "cartId";

	/** "密钥"Cookie名称 */
	public static final String KEY_COOKIE_NAME = "cartKey";


	public static final String CART_COUNT = "cartCount";

	/** 密钥 */
	@Column(name = "cart_key", nullable = false)
	private String key;

	/** 会员 */
	@OneToOne(fetch = FetchType.LAZY)
	private Member member;
	
	/** 平台活动 */
	@ManyToOne(fetch = FetchType.LAZY)
	private CouponCode couponCode;

	/** 购物车项 */
	@JsonProperty
	@OneToMany(mappedBy = "cart", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	
	private Set<CartItem> cartItems = new HashSet<CartItem>();

	/** 获取商品重量 */
	public int getWeight() {
		int weight = 0;
		if (getCartItems() != null) {
			for (CartItem cartItem : getEffectiveCartItems()) {
				if (cartItem != null) {
					weight += cartItem.getWeight();
				}
			}
		}
		return weight;
	}

	/** 获取商品重量 */
	public int getWeight(Tenant tenant) {
		int weight = 0;
		if (getCartItems() != null) {
			for (CartItem cartItem : getEffectiveCartItems(tenant)) {
				if (cartItem != null) {
					weight += cartItem.getWeight();
				}
			}
		}
		return weight;
	}

	/** 获取商品数量 */
	@JsonProperty
	public int getQuantity() {
		int quantity = 0;
		if (getCartItems() != null) {
			for (CartItem cartItem : getCartItems()) {
				if (cartItem != null && cartItem.getQuantity() != null) {
					quantity += cartItem.getQuantity();
				}
			}
		}
		return quantity;
	}
	
	/** 获取商品数量 */
	@JsonProperty
	public int getEffectiveQuantity() {
		int quantity = 0;
		if (getCartItems() != null) {
			for (CartItem cartItem : getEffectiveCartItems()) {
				if (cartItem != null && cartItem.getQuantity() != null) {
					quantity += cartItem.getQuantity();
				}
			}
		}
		return quantity;
	}

	
	/** 获取商品数量 */
	@JsonProperty
	public int getEffectiveQuantity(Product product) {
		int quantity = 0;
		CartItem cartItem = getCartItem(product);
		if (cartItem!=null) {
			quantity = cartItem.getQuantity();
		}
		return quantity;
	}
	
	/** 获取商品数量 */
	@JsonProperty
	public int getEffectiveQuantity(Tenant tenant) {
		int quantity = 0;
		if (getCartItems() != null) {
			for (CartItem cartItem : getEffectiveCartItems(tenant)) {
				if (cartItem != null && cartItem.getQuantity() != null) {
					quantity += cartItem.getQuantity();
				}
			}
		}
		return quantity;
	}
	
	/** 获取赠送积分 */
	public long getEffectivePoint() {
		long productPoint = 0L;
		BigDecimal productAmountPoint = BigDecimal.ZERO;
		for (CartItem cartItem : getEffectiveCartItems()) {
			if (cartItem != null) {
				if (cartItem.isProductPoint()) {
					productPoint += cartItem.getProductPoint() + cartItem.getProductAddedPoint();
				} else {
					productAmountPoint.add(cartItem.getConsumePointAmount()).add(cartItem.getConsumeAddedPointAmount());
				}
			}
		}
		Setting setting = SettingUtils.get();
		Double defaultPointScale = setting.getDefaultPointScale();
		return productPoint + productAmountPoint.multiply(new BigDecimal(defaultPointScale.toString())).longValue();
	}
	
	/** 获取赠送积分 */
	public long getEffectivePoint(Tenant tenant) {
		long productPoint = 0L;
		BigDecimal productAmountPoint = BigDecimal.ZERO;
		for (CartItem cartItem : getEffectiveCartItems()) {
			if (cartItem != null && cartItem.getProduct().getTenant().equals(tenant)) {
				if (cartItem.isProductPoint()) {
					productPoint += cartItem.getProductPoint() + cartItem.getProductAddedPoint();
				} else {
					productAmountPoint.add(cartItem.getConsumePointAmount()).add(cartItem.getConsumeAddedPointAmount());
				}
			}
		}
		Setting setting = SettingUtils.get();
		Double defaultPointScale = setting.getDefaultPointScale();
		return productPoint + productAmountPoint.multiply(new BigDecimal(defaultPointScale.toString())).longValue();
	}

	/** 获取商品价格 */
	public BigDecimal getPrice() {
		BigDecimal price = BigDecimal.ZERO;
		for (CartItem cartItem : getEffectiveCartItems()) {
			price = price.add(cartItem.getSubtotal());
		}
		return price;
	}

	/** 获取商品价格 */
	public BigDecimal getPrice(Tenant tenant) {
		BigDecimal price = BigDecimal.ZERO;
		for (CartItem cartItem : getEffectiveCartItems(tenant)) {
			price = price.add(cartItem.getSubtotal());
		}
		return price;
	}

	/** 获取折扣价 */
	public BigDecimal getDiscount() {
		BigDecimal discount = BigDecimal.ZERO;
		for (CartItem cartItem : getEffectiveCartItems()) {
			discount.add(cartItem.getDiscount());
		}
		return discount;
	}


	/** 获取折扣价 */
	public BigDecimal getDiscount(Tenant tenant) {
		BigDecimal discount = BigDecimal.ZERO;
		for (CartItem cartItem : getEffectiveCartItems(tenant)) {
			discount.add(cartItem.getDiscount());
		}
		return discount;
	}

	/** 获取运费 */
	public BigDecimal getFreight() {
		BigDecimal totalFreight = BigDecimal.ZERO;
		for (Tenant tenant:getTenants()) {
			totalFreight = totalFreight.add(getFreight(tenant));
		}
		return totalFreight;
	}

	/** 获取运费 */
	public BigDecimal getFreight(Tenant tenant) {
		
		BigDecimal totalFreight = BigDecimal.ZERO;
		BigDecimal freight = tenant.calculateFreight(getWeight(tenant),getEffectiveQuantity(tenant));
		for (Promotion promotion : getMailPromotions(tenant)) {
			if (promotion.getIsFreeShipping()) {
				freight = BigDecimal.ZERO;
				break;
			}
		}
		totalFreight = totalFreight.add(freight);
		return totalFreight;
	}
	
	/** 获取包邮方案 */
	public Set<Promotion> getMailPromotions(Tenant tenant) {
		Set<Promotion> allPromotions = tenant.getMailPromotions();
		Set<Promotion> promotions = new TreeSet<Promotion>();
		for (Promotion promotion : allPromotions) {
			if (isMailValid(promotion)) {
				promotions.add(promotion);
			}
		}
		return promotions;
	}
	/** 获取有效商品价格 */
	@JsonProperty
	public BigDecimal getEffectivePrice() {
		BigDecimal price = BigDecimal.ZERO;
		for (CartItem cartItem : getEffectiveCartItems()) {
			price = price.add(cartItem.getEffectiveAmount());
		}
		BigDecimal effectivePrice = price.subtract(getDiscount());
		return effectivePrice.compareTo(BigDecimal.ZERO) > 0 ? effectivePrice : BigDecimal.ZERO;
	}

	/** 获取有效商品价格 */
	@JsonProperty
	public BigDecimal getEffectivePrice(Tenant tenant) {
		BigDecimal price = BigDecimal.ZERO;
		for (CartItem cartItem : getEffectiveCartItems(tenant)) {
			price = price.add(cartItem.getEffectiveAmount());
		}
		BigDecimal effectivePrice = price.subtract(getDiscount(tenant));
		return effectivePrice.compareTo(BigDecimal.ZERO) > 0 ? effectivePrice : BigDecimal.ZERO;
	}

	/** 获取促销 */
	public Set<Promotion> getPromotions() {
		Set<Promotion> allPromotions = new HashSet<Promotion>();
		if (getCartItems() != null) {
			for (CartItem cartItem : getEffectiveCartItems()) {
				if (cartItem != null && cartItem.getProduct() != null) {
					allPromotions.addAll(cartItem.getProduct().getValidPromotions());
				}
			}
		}
		Set<Promotion> promotions = new TreeSet<Promotion>();
		for (Promotion promotion : allPromotions) {
			if (isValid(promotion)) {
				promotions.add(promotion);
			}
		}
		return promotions;
	}

	/** 获取商家 */
	public Set<Tenant> getTenants() {
		Set<Tenant> tenants = new HashSet<Tenant>();
		for (CartItem cartItem : getCartItems()) {
			if (!tenants.contains(cartItem.getProduct().getTenant())) {
				tenants.add(cartItem.getProduct().getTenant());
			}
		}
		return tenants;
	}

	/**
	 * 获取商家购物车项
	 * @param tenant 商家
	 * @return 商家购物车项
	 */
	@JsonProperty
	public Set<CartItem> getCartItems(Tenant tenant) {
		Set<CartItem> cartItems = new HashSet<CartItem>();
		if (tenant != null && getCartItems() != null) {
			for (CartItem cartItem : getCartItems()) {
				if (cartItem != null && cartItem.getProduct() != null && cartItem.getProduct().getTenant().equals(tenant)) {
					cartItems.add(cartItem);
				}
			}
		}
		return cartItems;
	}

	
	/** 获取赠品项 */
	public Set<GiftItem> getGiftItems() {
		Set<GiftItem> giftItems = new HashSet<GiftItem>();
		for (Promotion promotion : getPromotions()) {
			if (promotion.getGiftItems() != null) {
				for (final GiftItem giftItem : promotion.getGiftItems()) {
					GiftItem target = (GiftItem) CollectionUtils.find(giftItems, new Predicate() {
						public boolean evaluate(Object object) {
							GiftItem other = (GiftItem) object;
							return other != null && other.getGift().equals(giftItem.getGift());
						}
					});
					
					GiftItem item = new GiftItem();
					item.setId(giftItem.getId());
					item.setGift(giftItem.getGift());
					item.setPromotion(giftItem.getPromotion());
					item.setQuantity(giftItem.getQuantity());
					
					int quantity = getEffectiveQuantity(promotion.getPromotionProducts().get(0).getProduct());
					int p = 1;
					if (promotion.getMinimumQuantity().compareTo(0)>0)  {
					    p = quantity / promotion.getMinimumQuantity();
					}
					
					if (target != null) {
						target.setQuantity(target.getQuantity() + giftItem.getQuantity()*p);
					} else {
						giftItems.add(item);
						item.setQuantity(giftItem.getQuantity()*p);
					}
				}
			}
		}
		return giftItems;
	}
	
	/** 获取赠品项 */
	public Set<GiftItem> getGiftItems(Tenant tenant) {
		Set<GiftItem> giftItems = new HashSet<GiftItem>();
		for (Promotion promotion : getPromotions()) {
			if (promotion.getTenant()!=null && promotion.getTenant().equals(tenant) && promotion.getGiftItems() != null) {
				for (final GiftItem giftItem : promotion.getGiftItems()) {
					GiftItem target = (GiftItem) CollectionUtils.find(giftItems, new Predicate() {
						public boolean evaluate(Object object) {
							GiftItem other = (GiftItem) object;
							return other != null && other.getGift().equals(giftItem.getGift());
						}
					});
					
					GiftItem item = new GiftItem();
					item.setId(giftItem.getId());
					item.setGift(giftItem.getGift());
					item.setPromotion(giftItem.getPromotion());
					item.setQuantity(giftItem.getQuantity());
					
					int quantity = getEffectiveQuantity(promotion.getPromotionProducts().get(0).getProduct());
					int p = 1;
					if (promotion.getMinimumQuantity().compareTo(0)>0)  {
					    p = quantity / promotion.getMinimumQuantity();
					}
					
					if (target != null) {
						target.setQuantity(target.getQuantity() + giftItem.getQuantity()*p);
					} else {
						giftItems.add(item);
						item.setQuantity(giftItem.getQuantity()*p);
					}
				}
			}
		}
		return giftItems;
	}

	/**
	 * 获取促销购物车项
	 * @param promotion 促销
	 * @return 促销购物车项
	 */
	@JsonProperty
	private Set<CartItem> getCartItems(Promotion promotion) {
		Set<CartItem> cartItems = new HashSet<CartItem>();
		if (promotion != null && getCartItems() != null) {
			for (CartItem cartItem : getCartItems()) {
				if (cartItem != null && cartItem.getProduct() != null && cartItem.getProduct().isValid(promotion)  && cartItem.getSelected() ) {
					cartItems.add(cartItem);
				}
			}
		}
		return cartItems;
	}

	/**
	 * 获取促销购物车项
	 * @param promotion 促销
	 * @return 促销购物车项
	 */
	@JsonProperty
	private Set<CartItem> getCartItems(Promotion promotion,Tenant tenant) {
		Set<CartItem> cartItems = new HashSet<CartItem>();
		if (promotion != null && getCartItems() != null) {
			for (CartItem cartItem : getCartItems()) {
				if (cartItem != null && cartItem.getProduct() != null && cartItem.getProduct().isValid(promotion) && cartItem.getProduct().getTenant().equals(tenant) && cartItem.getSelected() ) {
					cartItems.add(cartItem);
				}
			}
		}
		return cartItems;
	}
	
	/**
	 * 获取促销商品总价格
	 * @param promotion 促销
	 * @return 促销商品价格
	 */
	private BigDecimal getPrice(Promotion promotion) {
		BigDecimal price = BigDecimal.ZERO;
		for (CartItem cartItem : getCartItems(promotion)) {
			if (cartItem != null && cartItem.getSubtotal() != null) {
				price = price.add(cartItem.getSubtotal());
			}
		}
		return price;
	}

	/**
	 * 获取促销商品总数量
	 * @param promotion 促销
	 * @return 促销商品总数量
	 */
	private int getQuantity(Promotion promotion) {
		int quantity = 0;
		for (CartItem cartItem : getCartItems(promotion)) {
			if (cartItem != null && cartItem.getQuantity() != null) {
				quantity += cartItem.calculateQuantity().intValue();
			}
		}
		return quantity;
	}

	/**
	 * 获取促销商品总数量
	 * @param promotion 促销
	 * @return 促销商品总数量
	 */
	private int getTotalQuantity(Promotion promotion) {
		int quantity = 0;
		for (CartItem cartItem : getCartItems(promotion)) {
			if (cartItem != null && cartItem.getQuantity() != null) {
				if (cartItem.getTotal()!=null) {
				   quantity += cartItem.calculateQuantity().intValue()+cartItem.getTotal();
				} else {
					   quantity += cartItem.calculateQuantity().intValue();
				}
			}
		}
		return quantity;
	}
	
	/**
	 * 获取促销商品总价格
	 * @param promotion 促销
	 * @return 促销商品价格
	 */
	private BigDecimal getPrice(Promotion promotion,Tenant tenant) {
		BigDecimal price = BigDecimal.ZERO;
		for (CartItem cartItem : getCartItems(promotion,tenant)) {
			if (cartItem != null && cartItem.getSubtotal() != null) {
				price = price.add(cartItem.getSubtotal());
			}
		}
		return price;
	}

	/**
	 * 获取促销商品总数量
	 * @param promotion 促销
	 * @return 促销商品总数量
	 */
	private int getQuantity(Promotion promotion,Tenant tenant) {
		int quantity = 0;
		for (CartItem cartItem : getCartItems(promotion,tenant)) {
			if (cartItem != null && cartItem.getQuantity() != null) {
				quantity += cartItem.calculateQuantity().intValue();
			}
		}
		return quantity;
	}

	/**
	 * 判断促销是否有效
	 * @param promotion 促销
	 * @return 促销是否有效
	 */
	public boolean isMailValid(Promotion promotion) {
		if (promotion == null || !promotion.hasBegun() || promotion.hasEnded()) {
			return false;
		}
		BigDecimal price = getEffectivePrice(promotion.getTenant());
		if ((promotion.getMinimumPrice() != null && promotion.getMinimumPrice().compareTo(price) > 0) || (promotion.getMaximumPrice() != null && promotion.getMaximumPrice().compareTo(price) < 0)) {
			return false;
		}
		return true;
	}
	
	/**
	 * 判断促销是否有效
	 * @param promotion 促销
	 * @return 促销是否有效
	 */
	public boolean isValid(Promotion promotion) {
		if (promotion == null || !promotion.hasBegun() || promotion.hasEnded()) {
			return false;
		}
		Integer quantity = getQuantity(promotion);
		Integer maxQuantity = getTotalQuantity(promotion);
		if ((promotion.getMinimumQuantity() != null && promotion.getMinimumQuantity() > quantity) || (promotion.getMaximumQuantity() != null && promotion.getMaximumQuantity() < maxQuantity)) {
			return false;
		}
		BigDecimal price = BigDecimal.ZERO;
		price = getPrice(promotion);
		if ((promotion.getMinimumPrice() != null && promotion.getMinimumPrice().compareTo(price) > 0) || (promotion.getMaximumPrice() != null && promotion.getMaximumPrice().compareTo(price) < 0)) {
			return false;
		}
		return true;
	}

	/**
	 * 判断优惠券是否有效
	 * @param coupon 优惠券
	 * @return 优惠券是否有效
	 */
	public boolean isValid(Coupon coupon) {
		if (coupon == null || !coupon.getIsEnabled() ) {
			return false;
		}
		Integer quantity = 0;
		if (coupon.getTenant()==null) {
			quantity = getEffectiveQuantity();
		} else {
			quantity = getEffectiveQuantity(coupon.getTenant());
		}
		
		if ((coupon.getMinimumQuantity() != null && coupon.getMinimumQuantity() > quantity) || (coupon.getMaximumQuantity() != null && coupon.getMaximumQuantity() < quantity)) {
			return false;
		}
		BigDecimal price = BigDecimal.ZERO;
		if (coupon.getTenant()==null) {
			price = getPrice();
		} else {
			price = getPrice(coupon.getTenant());
		}
		if ((coupon.getMinimumPrice() != null && coupon.getMinimumPrice().compareTo(price) > 0) || (coupon.getMaximumPrice() != null && coupon.getMaximumPrice().compareTo(price) < 0)) {
			return false;
		}
		return true;
	}

	/**
	 * 获取购物车项
	 * @return 购物车项
	 */
	public Set<CartItem> getEffectiveCartItems() {
			Set<CartItem> cartItems = new HashSet<CartItem>();
			if (getCartItems() != null) {
				for (CartItem cartItem : getCartItems()) {
					if (cartItem != null && cartItem.getProduct() != null && cartItem.getSelected()) {
						cartItems.add(cartItem);
					}
				}
			}
			return cartItems;
	}
	
	/**
	 * 获取购物车项
	 * @param tenant 商品
	 * @return 购物车项
	 */
	public Set<CartItem> getEffectiveCartItems(Tenant tenant) {
			Set<CartItem> cartItems = new HashSet<CartItem>();
			if (getCartItems() != null) {
				for (CartItem cartItem : getCartItems()) {
					if (cartItem != null && cartItem.getProduct() != null && cartItem.getSelected()  && cartItem.getProduct().getTenant().equals(tenant)) {
						cartItems.add(cartItem);
					}
				}
			}
			return cartItems;
	}

	/**
	 * 获取购物车项
	 * @param product 商品
	 * @return 购物车项
	 */
	public CartItem getCartItem(Product product) {
		if (product != null && getCartItems() != null) {
			for (CartItem cartItem : getCartItems()) {
				if (cartItem != null && cartItem.getProduct() != null && cartItem.getProduct().equals(product)) {
					return cartItem;
				}
			}
		}
		return null;
	}

	/**
	 * 判断是否包含商品
	 * @param product 商品
	 * @return 是否包含商品
	 */
	public boolean contains(Product product) {
		if (product != null && getCartItems() != null) {
			for (CartItem cartItem : getCartItems()) {
				if (cartItem != null && cartItem.getProduct() != null && cartItem.getProduct().equals(product)) {
					return true;
				}
			}
		}
		return false;
	}

	/** 获取令牌 */
	@JsonProperty
	public String getToken() {
		HashCodeBuilder hashCodeBuilder = new HashCodeBuilder(17, 37).append(getKey());
		if (getCartItems() != null) {
			for (CartItem cartItem : getCartItems()) {
				hashCodeBuilder.append(cartItem.getProduct()).append(cartItem.getQuantity()).append(cartItem.getPrice());
			}
		}
		return DigestUtils.md5Hex(hashCodeBuilder.toString());
	}

	/** 获取是否库存不足 */
	public boolean getIsLowStock() {
		for (CartItem cartItem : getEffectiveCartItems()) {
			if (cartItem != null && cartItem.getIsLowStock()) {
				return true;
			}
		}
		return false;
	}

	/** 判断是否已过期 */
	public boolean hasExpired() {
		return new Date().after(DateUtils.addSeconds(getModifyDate(), TIMEOUT));
	}

	/** 判断是否允许使用促销优惠券 */
	public boolean isCouponAllowed() {
		for (Promotion promotion : getPromotions()) {
			if (promotion != null && !promotion.getIsCouponAllowed()) {
				return false;
			}
		}
		return true;
	}

	/** 判断是否为空 */
	public boolean isEmpty() {
		return getCartItems() == null || getEffectiveCartItems().isEmpty();
	}

	// ===========================================getter/setter===========================================//
	/**
	 * 获取密钥
	 * @return 密钥
	 */
	public String getKey() {
		return key;
	}

	/**
	 * 设置密钥
	 * @param key 密钥
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * 获取会员
	 * @return 会员
	 */
	public Member getMember() {
		return member;
	}

	/**
	 * 设置会员
	 * @param member 会员
	 */
	public void setMember(Member member) {
		this.member = member;
	}

	/**
	 * 获取购物车项
	 * @return 购物车项
	 */
	public Set<CartItem> getCartItems() {
		return cartItems;
	}

	/**
	 * 设置购物车项
	 * @param cartItems 购物车项
	 */
	public void setCartItems(Set<CartItem> cartItems) {
		this.cartItems = cartItems;
	}


	public void setCouponCode(CouponCode couponCode) {
		this.couponCode = couponCode;
	}

	public CouponCode getCouponCode() {
		return couponCode;
	}



}