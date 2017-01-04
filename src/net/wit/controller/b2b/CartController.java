/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.b2b;

import java.math.BigDecimal;
import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.wit.Filter;
import net.wit.Message;
import net.wit.entity.*;
import net.wit.service.*;
import net.wit.util.WebUtils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller - 购物车
 * @author rsico Team
 * @version 3.0
 */
@Controller("b2bCartController")
@RequestMapping("/b2b/cart")
public class CartController extends BaseController {

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "productServiceImpl")
	private ProductService productService;

	@Resource(name = "cartServiceImpl")
	private CartService cartService;

	@Resource(name = "cartItemServiceImpl")
	private CartItemService cartItemService;

	@Resource(name = "packagUnitServiceImpl")
	private PackagUnitService packagUnitService;

	@Resource(name = "couponCodeServiceImpl")
	private CouponCodeService couponCodeService;

	@Resource(name = "couponServiceImpl")
	private CouponService couponService;

	@Resource(name = "tenantServiceImpl")
	private TenantService tenantService;

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	/**
	 * 添加
	 */
	@RequestMapping(value = "/get_cart_count", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getCartCount(HttpServletRequest request, HttpServletResponse response) {
		Cart cart = cartService.getCurrent();
		Map<String, Object> data = new HashMap<String, Object>();
		if (cart == null) {
			data.put("count", 0);
		} else {
			data.put("count", cart.getCartItems().size());
		}
		return data;
	}

	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public @ResponseBody Message add(Long id, Integer quantity, String type,Long packagUnitId, HttpServletRequest request, HttpServletResponse response) {
		if (quantity == null || quantity < 1) {
			return ERROR_MESSAGE;
		}
		Product product = productService.find(id);
		if (product == null) {
			return Message.warn("shop.cart.productNotExsit");
		}
		if (!product.getIsMarketable()) {
			return Message.warn("shop.cart.productNotMarketable");
		}
		if (product.getIsGift()) {
			return Message.warn("shop.cart.notForSale");
		}

		Cart cart = cartService.getCurrent();
		Member member = memberService.getCurrent();
		if (cart == null) {
			cart = new Cart();
			cart.setKey(UUID.randomUUID().toString() + DigestUtils.md5Hex(RandomStringUtils.randomAlphabetic(30)));
			cart.setMember(member);
			cartService.save(cart);
		}

		if (Cart.MAX_PRODUCT_COUNT != null && cart.getCartItems().size() >= Cart.MAX_PRODUCT_COUNT) {
			return Message.warn("shop.cart.addCountNotAllowed", Cart.MAX_PRODUCT_COUNT);
		}
		
		if ("buy".equals(type)) {
			if(member==null){
				return Message.warn("请先登录"); 
			}
			for (CartItem cartItem:cart.getCartItems()) {
				cartItem.setSelected(false);
				cartItemService.save(cartItem);
			}
		}
		
		PackagUnit packagUnit = packagUnitService.find(packagUnitId);
		BigDecimal price = BigDecimal.ZERO;
		Integer currentQuantity = 0;
		if (cart.contains(product)) {
			CartItem cartItem = cart.getCartItem(product);
			if (CartItem.MAX_QUANTITY != null && cartItem.getQuantity() + quantity > CartItem.MAX_QUANTITY) {
				return Message.warn("shop.cart.maxCartItemQuantity", CartItem.MAX_QUANTITY);
			}
			if (product.getStock() != null && cartItem.getQuantity() + quantity > product.getAvailableStock()) {
				return Message.warn("shop.cart.productLowStock");
			}
			if("buy".equals(type)){
				cartItem.setSelected(true);
				cartItem.setQuantity(quantity);
				cartItemService.update(cartItem);
				price = cartItem.getEffectivePrice();
			}else{
				cartItem.add(quantity, packagUnit);
				cartItem.setSelected(true);
				currentQuantity = cartItem.getQuantity();
				cartItemService.update(cartItem);
				price = cartItem.getEffectivePrice();
			}
		} else {
			if (CartItem.MAX_QUANTITY != null && quantity > CartItem.MAX_QUANTITY) {
				return Message.warn("shop.cart.maxCartItemQuantity", CartItem.MAX_QUANTITY);
			}
			if (product.getStock() != null && quantity > product.getAvailableStock()) {
				return Message.warn("shop.cart.productLowStock");
			}
			CartItem cartItem = new CartItem();
			cartItem.setQuantity(quantity);
			cartItem.setProduct(product);
			cartItem.setPackagUnit(packagUnit);
			cartItem.setSelected(true);
			cartItem.setCart(cart);
			cartItemService.save(cartItem);
			cart.getCartItems().add(cartItem);
			price = cartItem.getEffectivePrice();
		}
//		for (CartItem cartItem:cart.getCartItems()) {
//			if (cartItem.getSelected() && cartItem.getProduct().getTenant().equals(member.getTenant())){
//			 	return Message.warn("您无法选购自己店铺的商品");
//		 	}
//		}
		if (member == null) {
			WebUtils.addCookie(request, response, Cart.ID_COOKIE_NAME, cart.getId().toString(), Cart.TIMEOUT);
			WebUtils.addCookie(request, response, Cart.KEY_COOKIE_NAME, cart.getKey(), Cart.TIMEOUT);
		}
		return Message.success("shop.cart.addSuccess", cart.getQuantity(), currency(cart.getEffectivePrice(), true, false));
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String shoppingCart(ModelMap model) {
		Member member=memberService.getCurrent();
		model.addAttribute("member",member);
		model.addAttribute("menu","cart");
		model.addAttribute("area",areaService.getCurrent());
		model.addAttribute("cart", cartService.getCurrent());
		return "/b2b/cart/list";
	}

	/**
	 * 列表json
	 */
	@RequestMapping(value = "/list_ajax", method = RequestMethod.GET)
	@ResponseBody
	public Cart list_ajax() {
		Cart cart = cartService.getCurrent();
		return cart;
	}

	/**
	 * 编辑
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> edit(Long id, Integer quantity, Long packagUnitId) {
		Map<String, Object> data = new HashMap<String, Object>();
		if (quantity == null || quantity < 1) {
			data.put("message", ERROR_MESSAGE);
			return data;
		}
		Cart cart = cartService.getCurrent();
		if (cart == null || cart.isEmpty()) {
			data.put("message", Message.error("shop.cart.notEmpty"));
			return data;
		}
		CartItem cartItem = cartItemService.find(id);
		if (cartItem == null) {
			data.put("message", Message.error("b2b.cart.cartItemNotExsit"));
			return data;
		}
		if (packagUnitId == null) {
			cartItem.setPackagUnit(null);
		} else {
			PackagUnit packagUnit = packagUnitService.find(packagUnitId);
			cartItem.setPackagUnit(packagUnit);
		}
		Set<CartItem> cartItems = cart.getCartItems();
		if (cartItems == null || !cartItems.contains(cartItem)) {
			data.put("message", Message.error("shop.cart.cartItemNotExsit"));
			return data;
		}
		if (CartItem.MAX_QUANTITY != null && quantity > CartItem.MAX_QUANTITY) {
			data.put("message", Message.warn("shop.cart.maxCartItemQuantity", CartItem.MAX_QUANTITY));
			return data;
		}
		Product product = cartItem.getProduct();
		if (product.getStock() != null && quantity > product.getAvailableStock()) {
			data.put("message", Message.warn("shop.cart.productLowStock"));
			return data;
		}
		cartItem.setQuantity(quantity);
		cartItemService.save(cartItem);

		data.put("message", SUCCESS_MESSAGE);
		data.put("subtotal", cartItem.getSubtotal());
		data.put("price", cartItem.getPrice());
		data.put("isLowStock", cartItem.getIsLowStock());
		data.put("quantity", cart.getQuantity());
		data.put("effectivePoint", cart.getEffectivePoint());
		data.put("effectivePrice", cart.getEffectivePrice());
		data.put("promotions", cart.getPromotions());
		data.put("giftItems", cart.getGiftItems());
		return data;
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> delete(Long id) {
		Map<String, Object> data = new HashMap<String, Object>();
		Cart cart = cartService.getCurrent();
		if (cart == null || cart.isEmpty()) {
			data.put("message", Message.error("shop.cart.notEmpty"));
			return data;
		}
		CartItem cartItem = cartItemService.find(id);
		Set<CartItem> cartItems = cart.getCartItems();
		if (cartItem == null || cartItems == null || !cartItems.contains(cartItem)) {
			data.put("message", Message.error("shop.cart.cartItemNotExsit"));
			return data;
		}
		cartItems.remove(cartItem);
		cartItemService.delete(cartItem);

		data.put("message", SUCCESS_MESSAGE);
		data.put("quantity", cart.getQuantity());
		data.put("effectivePoint", cart.getEffectivePoint());
		data.put("effectivePrice", cart.getEffectivePrice());
		data.put("promotions", cart.getPromotions());
		data.put("isLowStock", cart.getIsLowStock());
		return data;
	}



	/**
	 * 选择
	 */
	@RequestMapping(value = "/selected", method = RequestMethod.POST)
	public @ResponseBody Message selected(Long id) {
		Cart cart = cartService.getCurrent();
		if (cart == null || cart.isEmpty()) {
			return Message.error("b2b.cart.notEmpty");
		}
		CartItem cartItem = cartItemService.find(id);
		Set<CartItem> cartItems = cart.getCartItems();
		if (cartItem == null || cartItems == null || !cartItems.contains(cartItem)) {
			return  Message.error("b2b.cart.cartItemNotExsit");
		}
		cartItem.setSelected(true);
		cartItemService.save(cartItem);
		return SUCCESS_MESSAGE;
	}

	/**
	 * 清除选择
	 */
	@RequestMapping(value = "/clearSelected", method = RequestMethod.POST)
	public @ResponseBody Message clearSelected() {
		Cart cart = cartService.getCurrent();
		if (cart == null || cart.isEmpty()) {
			return Message.error("b2b.cart.notEmpty");
		}
		Set<CartItem> cartItems = cart.getCartItems();
		if (cartItems == null) {
			return Message.error("b2b.cart.cartItemNotExsit");
		}
		for (CartItem cartItem:cartItems) {
  		  cartItem.setSelected(false);
		  cartItemService.save(cartItem);
		}
		return SUCCESS_MESSAGE;
	}
	
	/**
	 * 不选择
	 */
	@RequestMapping(value = "/unSelected", method = RequestMethod.POST)
	public @ResponseBody Message unSelected(Long id) {
		Cart cart = cartService.getCurrent();
		if (cart == null || cart.isEmpty()) {
			return Message.error("b2b.cart.notEmpty");
		}
		CartItem cartItem = cartItemService.find(id);
		Set<CartItem> cartItems = cart.getCartItems();
		if (cartItem == null || cartItems == null || !cartItems.contains(cartItem)) {
			return  Message.error("b2b.cart.cartItemNotExsit");
		}
		cartItem.setSelected(false);
		cartItemService.save(cartItem);
		return SUCCESS_MESSAGE;
	}
	
	/**
	 * 清空
	 */
	@RequestMapping(value = "/clear", method = RequestMethod.POST)
	public @ResponseBody Message clear() {
		Cart cart = cartService.getCurrent();
		cartService.delete(cart);
		return SUCCESS_MESSAGE;
	}

	/**
	 * 批量采购---添加购物车
	 */
	@RequestMapping(value = "/addALL", method = RequestMethod.POST)
	public @ResponseBody Message addAll(String idAndQuantity, HttpServletRequest request, HttpServletResponse response) {
		String[] ArrayIdAndQuantity = idAndQuantity.split(",");
		Cart cart = cartService.getCurrent();
		Member member = memberService.getCurrent();
		if (cart == null) {
			cart = new Cart();
			cart.setKey(UUID.randomUUID().toString() + DigestUtils.md5Hex(RandomStringUtils.randomAlphabetic(30)));
			cart.setMember(member);
			cartService.save(cart);
		}
		if (Cart.MAX_PRODUCT_COUNT != null && cart.getCartItems().size() >= Cart.MAX_PRODUCT_COUNT) {
			return Message.warn("shop.cart.addCountNotAllowed", Cart.MAX_PRODUCT_COUNT);
		}
		for (int i = 0; i < ArrayIdAndQuantity.length; i++) {
			String tmp = ArrayIdAndQuantity[i];
			String[] tmpIdAndQuantity = tmp.split("-");
			Long id = Long.parseLong(tmpIdAndQuantity[0]);
			Product product = productService.find(id);
			int quantity = Integer.parseInt(tmpIdAndQuantity[1]);

			Long packagUnitId = null;
			if (tmpIdAndQuantity.length > 2) {
				packagUnitId = Long.parseLong(tmpIdAndQuantity[2]);
			}
			if (product == null) {
				return Message.warn("shop.cart.productNotExsit");
			}
			if (!product.getIsMarketable() && quantity > 0) {
				return Message.warn(product.getName() + "已下架,不能采购,请将采购数量置为0");
				// return Message.warn("shop.cart.productNotMarketable");
			}
			if (product.getIsGift() && quantity > 0) {
				return Message.warn(product.getName() + "是非卖品,不能采购");
				// return Message.warn("shop.cart.notForSale");
			}
			if (quantity > 0) {
				PackagUnit packagUnit = packagUnitService.find(packagUnitId);
				if (cart.contains(product)) {
					CartItem cartItem = cart.getCartItem(product);
					if ((packagUnit == null && cartItem.getPackagUnit() == null) || (packagUnit != null && packagUnit.equals(cartItem.getPackagUnit()))) { // 同类单位信息
						if (CartItem.MAX_QUANTITY != null && cartItem.getQuantity() + quantity > CartItem.MAX_QUANTITY) {
							return Message.warn("shop.cart.maxCartItemQuantity", CartItem.MAX_QUANTITY);
						}
						if (product.getStock() != null && cartItem.getQuantity() + quantity > product.getAvailableStock()) {
							return Message.warn("shop.cart.productLowStock");
						}
						cartItem.add(quantity, packagUnit);
					} else {
						cartItem.setPackagUnit(packagUnit);
						cartItem.setQuantity(quantity);
					}
					cartItemService.update(cartItem);
				} else {
					if (CartItem.MAX_QUANTITY != null && quantity > CartItem.MAX_QUANTITY) {
						return Message.warn("shop.cart.maxCartItemQuantity", CartItem.MAX_QUANTITY);
					}
					if (product.getStock() != null && quantity > product.getAvailableStock()) {
						return Message.warn("shop.cart.productLowStock");
					}
					CartItem cartItem = new CartItem();
					cartItem.setSelected(false);
					cartItem.setQuantity(quantity);
					cartItem.setProduct(product);
					cartItem.setPackagUnit(packagUnit);
					cartItem.setCart(cart);
					cartItemService.save(cartItem);
					cart.getCartItems().add(cartItem);
				}
			}
		}
		return Message.success("shop.cart.addSuccess", cart.getQuantity(), currency(cart.getEffectivePrice(), true, false));
	}

}