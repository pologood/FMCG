/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.ajax;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.wit.Message;
import net.wit.entity.Cart;
import net.wit.entity.CartItem;
import net.wit.entity.Member;
import net.wit.entity.PackagUnit;
import net.wit.entity.Product;
import net.wit.service.CartItemService;
import net.wit.service.CartService;
import net.wit.service.MemberService;
import net.wit.service.PackagUnitService;
import net.wit.service.ProductService;
import net.wit.util.JsonUtils;
import net.wit.util.WebUtils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller - 购物车
 * 
 * @author rsico Team
 * @version 3.0
 */
@Controller("ajaxCartController")
@RequestMapping("/ajax/cart")
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


	/**
	 * 获取当前购物车数量
	 */
	@RequestMapping(value = "/get_cart_count", method = RequestMethod.POST)
	public @ResponseBody
	Message getCartCount(HttpServletRequest request, HttpServletResponse response) {
		Cart cart = cartService.getCurrent();
		Map<String, Object> data = new HashMap<String, Object>();
		if(cart==null){
			data.put("count", 0);
		}else{
			data.put("count", cart.getQuantity());
		}
		return Message.success(JsonUtils.toJson(data));
	}

	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public @ResponseBody
	Message add(Long id, Integer quantity,Long packagUnitId, HttpServletRequest request, HttpServletResponse response) {
		if (quantity == null || quantity < 1) {
			return ERROR_MESSAGE;
		}
		Product product = productService.find(id);
		if (product == null) {
			return Message.error("ajax.cart.productNotExsit");
		}
		if (!product.getIsMarketable()) {
			return Message.error("ajax.cart.productNotMarketable");
		}
		if (product.getIsGift()) {
			return Message.error("ajax.cart.notForSale");
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
			return Message.error("ajax.cart.addCountNotAllowed", Cart.MAX_PRODUCT_COUNT);
		}
		PackagUnit packagUnit = packagUnitService.find(packagUnitId);
		if (cart.contains(product)) {
			CartItem cartItem = cart.getCartItem(product);
			if (CartItem.MAX_QUANTITY != null && cartItem.getQuantity() + quantity > CartItem.MAX_QUANTITY) {
				return Message.error("ajax.cart.maxCartItemQuantity", CartItem.MAX_QUANTITY);
			}
			if (product.getStock() != null && cartItem.getQuantity() + quantity > product.getAvailableStock()) {
				return Message.error("ajax.cart.productLowStock");
			}
			cartItem.add(quantity,packagUnit);
			cartItemService.update(cartItem);
		} else {
			if (CartItem.MAX_QUANTITY != null && quantity > CartItem.MAX_QUANTITY) {
				return Message.error("ajax.cart.maxCartItemQuantity", CartItem.MAX_QUANTITY);
			}
			if (product.getStock() != null && quantity > product.getAvailableStock()) {
				return Message.error("ajax.cart.productLowStock");
			}
			CartItem cartItem = new CartItem();
			cartItem.setSelected(true);
			cartItem.setQuantity(quantity);
			cartItem.setProduct(product);
			cartItem.setPackagUnit(packagUnit);
			cartItem.setCart(cart);
			cartItemService.save(cartItem);
			cart.getCartItems().add(cartItem);
		}

		if (member == null) {
			WebUtils.addCookie(request, response, Cart.ID_COOKIE_NAME, cart.getId().toString(), Cart.TIMEOUT);
			WebUtils.addCookie(request, response, Cart.KEY_COOKIE_NAME, cart.getKey(), Cart.TIMEOUT);
		}
		return Message.success("ajax.cart.addSuccess", cart.getQuantity(), currency(cart.getEffectivePrice(), true,
				false));
	}


	/**
	 * 查看购物车
	 */
	@RequestMapping(value = "/list_ajax", method = RequestMethod.GET)
	@ResponseBody
	public Message list_ajax() {
		Cart cart = cartService.getCurrent();
		return Message.success(JsonUtils.toJson(cart));
	}
	
	/**
	 * 编辑
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public @ResponseBody
	Message edit(Long id, Integer quantity) {
		Map<String, Object> data = new HashMap<String, Object>();
		if (quantity == null || quantity < 1) {
			return Message.error("ajax.cart.quantity.gtZero");
		}
		Cart cart = cartService.getCurrent();
		if (cart == null || cart.isEmpty()) {
			return Message.error("ajax.cart.notEmpty");
		}
		CartItem cartItem = cartItemService.find(id);
		Set<CartItem> cartItems = cart.getCartItems();
		if (cartItem == null || cartItems == null || !cartItems.contains(cartItem)) {
			return Message.error("ajax.cart.cartItemNotExsit");
		}
		if (CartItem.MAX_QUANTITY != null && quantity > CartItem.MAX_QUANTITY) {
			return Message.error("ajax.cart.maxCartItemQuantity", CartItem.MAX_QUANTITY);
		}
		Product product = cartItem.getProduct();
		if (product.getStock() != null && quantity > product.getAvailableStock()) {
			return Message.error("ajax.cart.productLowStock");
		}
		cartItem.setQuantity(quantity);
		cartItemService.update(cartItem);

		data.put("message", SUCCESS_MESSAGE);
		data.put("subtotal", cartItem.getSubtotal());
		data.put("isLowStock", cartItem.getIsLowStock());
		data.put("quantity", cart.getQuantity());
		data.put("effectivePoint", cart.getEffectivePoint());
		data.put("effectivePrice", cart.getEffectivePrice());
		data.put("promotions", cart.getPromotions());
		data.put("giftItems", cart.getGiftItems());
		return Message.success(JsonUtils.toJson(data));
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> delete(Long id) {
		Map<String, Object> data = new HashMap<String, Object>();
		Cart cart = cartService.getCurrent();
		if (cart == null || cart.isEmpty()) {
			data.put("message", Message.error("ajax.cart.notEmpty"));
			return data;
		}
		CartItem cartItem = cartItemService.find(id);
		Set<CartItem> cartItems = cart.getCartItems();
		if (cartItem == null || cartItems == null || !cartItems.contains(cartItem)) {
			data.put("message", Message.error("ajax.cart.cartItemNotExsit"));
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
	 * 批量删除
	 */
	@RequestMapping(value = "/batchDelete", method = RequestMethod.POST)
	public @ResponseBody
	Message batchDelete(String ids) {
		Map<String, Object> data = new HashMap<String, Object>();
		Cart cart = cartService.getCurrent();
		if (cart == null || cart.isEmpty()) {
			return Message.error("ajax.cart.notEmpty");
			
		}
		Set<CartItem> cartItems = cart.getCartItems();
		String[] idArray = ids.split("-");
		for(int i=0;i<idArray.length;i++ ){
			String idStr = idArray[i];
			Long id = Long.parseLong(idStr);
			CartItem cartItem = cartItemService.find(id);
			if (cartItem == null || cartItems == null || !cartItems.contains(cartItem)) {
				return Message.error("ajax.cart.cartItemNotExsit");
			}
			cartItems.remove(cartItem);
			cartItemService.delete(cartItem);  
		}
		
		data.put("message", SUCCESS_MESSAGE);
		data.put("quantity", cart.getQuantity());
		data.put("effectivePoint", cart.getEffectivePoint());
		data.put("effectivePrice", cart.getEffectivePrice());
		data.put("promotions", cart.getPromotions());
		data.put("isLowStock", cart.getIsLowStock());
		return Message.success(JsonUtils.toJson(data));
	}

	/**
	 * 清空
	 */
	@RequestMapping(value = "/clear", method = RequestMethod.POST)
	public @ResponseBody
	Message clear() {
		Cart cart = cartService.getCurrent();
		cartService.delete(cart);
		return SUCCESS_MESSAGE;
	}
	
	/**
	 * 批量采购---添加购物车
	 */
	@RequestMapping(value = "/addALL", method = RequestMethod.POST)
	public @ResponseBody
	Message addAll(String idAndQuantity, HttpServletRequest request, HttpServletResponse response) {
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
			return Message.error("ajax.cart.addCountNotAllowed", Cart.MAX_PRODUCT_COUNT);
		}
		for(int i=0;i<ArrayIdAndQuantity.length;i++){
			String tmp = ArrayIdAndQuantity[i];
			String[]tmpIdAndQuantity = tmp.split("-");
			Long id = Long.parseLong(tmpIdAndQuantity[0]);
			Product product = productService.find(id);
			int quantity = Integer.parseInt(tmpIdAndQuantity[1]);
			Long packagUnitId = Long.parseLong(tmpIdAndQuantity[2]);
			if (product == null) {
				return Message.error("ajax.cart.productNotExsit");
			}
			if (!product.getIsMarketable()&& quantity > 0) {
				return Message.error(product.getName()+"已下架,不能采购,请将采购数量置为0");
			}
			if (product.getIsGift()&& quantity > 0) {
				return Message.error(product.getName()+"是非卖品,不能采购");
			}
			if(quantity > 0){
				PackagUnit packagUnit = packagUnitService.find(packagUnitId);
				if (cart.contains(product)) {
					CartItem cartItem = cart.getCartItem(product);
					if (CartItem.MAX_QUANTITY != null && cartItem.getQuantity() + quantity > CartItem.MAX_QUANTITY) {
						return Message.error("ajax.cart.maxCartItemQuantity", CartItem.MAX_QUANTITY);
					}
					if (product.getStock() != null && cartItem.getQuantity() + quantity > product.getAvailableStock()) {
						return Message.error("ajax.cart.productLowStock");
					}
					cartItem.add(quantity,packagUnit);
					cartItemService.update(cartItem);
				} else {
					if (CartItem.MAX_QUANTITY != null && quantity > CartItem.MAX_QUANTITY) {
						return Message.error("ajax.cart.maxCartItemQuantity", CartItem.MAX_QUANTITY);
					}
					if (product.getStock() != null && quantity > product.getAvailableStock()) {
						return Message.error("ajax.cart.productLowStock");
					}
					CartItem cartItem = new CartItem();
					cartItem.setSelected(true);
					cartItem.setQuantity(quantity);
					cartItem.setProduct(product);
					cartItem.setPackagUnit(packagUnit);
					cartItem.setCart(cart);
					cartItemService.save(cartItem);
					cart.getCartItems().add(cartItem);
				}
			}
			
		}
		return Message.success("ajax.cart.addSuccess", cart.getQuantity(), currency(cart.getEffectivePrice(), true,
				false));
	}

}