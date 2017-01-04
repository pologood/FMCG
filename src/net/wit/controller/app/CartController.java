/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.app;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import net.wit.controller.app.model.CartModel;
import net.wit.controller.app.model.DataBlock;
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
import net.wit.util.WebUtils;

/**
 * Controller - 购物车
 * 
 * @author rsico Team
 * @version 3.0
 */
@Controller("appCartController")
@RequestMapping("/app/cart")
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
	DataBlock getCartCount(HttpServletRequest request, HttpServletResponse response) {
		Cart cart = cartService.getCurrent();
		Integer quantity = 0;
		if(cart!=null){
			quantity = cart.getQuantity();
		}
		return DataBlock.success(quantity,"执行成功");
	}

	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public @ResponseBody
	DataBlock add(Long id, Integer quantity,Long packagUnitId, HttpServletRequest request, HttpServletResponse response) {
		if (quantity == null || quantity < 1) {
			return DataBlock.error("数量无效");
		}
		Product product = productService.find(id);
		if (product == null) {
			return DataBlock.error("ajax.cart.productNotExsit");
		}
		if (!product.getIsMarketable()) {
			return DataBlock.error("ajax.cart.productNotMarketable");
		}
		if (product.getIsGift()) {
			return DataBlock.error("ajax.cart.notForSale");
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
			return DataBlock.error("ajax.cart.addCountNotAllowed", Cart.MAX_PRODUCT_COUNT);
		}
		PackagUnit packagUnit = packagUnitService.find(packagUnitId);
		if (cart.contains(product)) {
			CartItem cartItem = cart.getCartItem(product);
			if (CartItem.MAX_QUANTITY != null && cartItem.getQuantity() + quantity > CartItem.MAX_QUANTITY) {
				return DataBlock.error("ajax.cart.maxCartItemQuantity", CartItem.MAX_QUANTITY);
			}
			if (product.getStock() != null && cartItem.getQuantity() + quantity > product.getAvailableStock()) {
				return DataBlock.error("ajax.cart.productLowStock");
			}
			cartItem.add(quantity,packagUnit);
			cartItemService.update(cartItem);
		} else {
			if (CartItem.MAX_QUANTITY != null && quantity > CartItem.MAX_QUANTITY) {
				return DataBlock.error("ajax.cart.maxCartItemQuantity", CartItem.MAX_QUANTITY);
			}
			if (product.getStock() != null && quantity > product.getAvailableStock()) {
				return DataBlock.error("ajax.cart.productLowStock");
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
		return DataBlock.success(cart.getQuantity(),"ajax.cart.addSuccess", cart.getQuantity(), currency(cart.getEffectivePrice(), true,
				false));
	}


	/**
	 * 查看购物车
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock list() {
		Cart cart = cartService.getCurrent();
		CartModel model = new CartModel();
		model.copyFrom(cart);
		return DataBlock.success(model,"执行成功");
	}
	
	/**
	 * 编辑
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public @ResponseBody
	DataBlock edit(Long id, Integer quantity) {
		Map<String, Object> data = new HashMap<String, Object>();
		if (quantity == null || quantity < 1) {
			return DataBlock.error("ajax.cart.quantity.gtZero");
		}
		Cart cart = cartService.getCurrent();
		if (cart == null || cart.isEmpty()) {
			return DataBlock.error("ajax.cart.notEmpty");
		}
		CartItem cartItem = cartItemService.find(id);
		Set<CartItem> cartItems = cart.getCartItems();
		if (cartItem == null || cartItems == null || !cartItems.contains(cartItem)) {
			return DataBlock.error("ajax.cart.cartItemNotExsit");
		}
		if (CartItem.MAX_QUANTITY != null && quantity > CartItem.MAX_QUANTITY) {
			return DataBlock.error("ajax.cart.maxCartItemQuantity", CartItem.MAX_QUANTITY);
		}
		Product product = cartItem.getProduct();
		if (product.getStock() != null && quantity > product.getAvailableStock()) {
			return DataBlock.error("ajax.cart.productLowStock");
		}
		cartItem.setQuantity(quantity);
		cartItemService.update(cartItem);

		data.put("subtotal", cartItem.getSubtotal());
		data.put("isLowStock", cartItem.getIsLowStock());
		data.put("quantity", cart.getQuantity());
		data.put("effectivePoint", cart.getEffectivePoint());
		data.put("effectivePrice", cart.getEffectivePrice());
		return DataBlock.success(data,"执行成功");
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	DataBlock delete(Long id) {
		Map<String, Object> data = new HashMap<String, Object>();
		Cart cart = cartService.getCurrent();
		if (cart == null || cart.isEmpty()) {
			return DataBlock.error("ajax.cart.notEmpty");
		}
		CartItem cartItem = cartItemService.find(id);
		Set<CartItem> cartItems = cart.getCartItems();
		if (cartItem == null || cartItems == null || !cartItems.contains(cartItem)) {
			return DataBlock.error("ajax.cart.cartItemNotExsit");
		}
		cartItems.remove(cartItem);
		cartItemService.delete(cartItem);

		data.put("quantity", cart.getQuantity());
		data.put("effectivePoint", cart.getEffectivePoint());
		data.put("effectivePrice", cart.getEffectivePrice());
		data.put("isLowStock", cart.getIsLowStock());
		return DataBlock.success(data,"执行成功");
	}
	
	/**
	 * 清空
	 */
	@RequestMapping(value = "/clear", method = RequestMethod.POST)
	public @ResponseBody
	DataBlock clear() {
		Cart cart = cartService.getCurrent();
		cartService.delete(cart);
		return DataBlock.success("success", "清除成功");
	}
	
}