/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.wap;

import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.wit.Message;
import net.wit.entity.*;
import net.wit.service.AppointmentService;
import net.wit.service.AreaService;
import net.wit.service.CartItemService;
import net.wit.service.CartService;
import net.wit.service.MemberService;
import net.wit.service.PackagUnitService;
import net.wit.service.PaymentMethodService;
import net.wit.service.ProductCategoryService;
import net.wit.service.ProductService;
import net.wit.service.ReceiverService;
import net.wit.util.JsonUtils;
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
@Controller("wapCartController")
@RequestMapping("/wap/cart")
public class CartController extends BaseController {

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "productServiceImpl")
	private ProductService productService;

	@Resource(name = "cartServiceImpl")
	private CartService cartService;

	@Resource(name = "cartItemServiceImpl")
	private CartItemService cartItemService;

	@Resource(name = "receiverServiceImpl")
	private ReceiverService receiverService;

	@Resource(name = "paymentMethodServiceImpl")
	private PaymentMethodService paymentMethodService;

	@Resource(name = "appointmentServiceImpl")
	private AppointmentService appointmentService;

	@Resource(name = "packagUnitServiceImpl")
	private PackagUnitService packagUnitService;

	@Resource(name = "productCategoryServiceImpl")
	private ProductCategoryService productCategoryService;

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	/**
	 * 获取当前购物车数量
	 */
	@RequestMapping(value = "/get_cart_count", method = RequestMethod.POST)
	public @ResponseBody Message getCartCount(HttpServletRequest request, HttpServletResponse response) {
		Cart cart = cartService.getCurrent();
		Map<String, Object> data = new HashMap<String, Object>();
		if (cart == null) {
			data.put("count", 0);
		} else {
			data.put("count", cart.getQuantity());
		}
		return Message.success(JsonUtils.toJson(data));
	}

	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public @ResponseBody Message add(Long id, Integer quantity, Long packagUnitId, HttpServletRequest request, HttpServletResponse response) {

		if (quantity == null || quantity < 1) {
			return ERROR_MESSAGE;
		}
		Product product = productService.find(id);
		if (product == null) {
			return Message.warn("mobile.cart.productNotExsit");
		}
		if (!product.getIsMarketable()) {
			return Message.warn("mobile.cart.productNotMarketable");
		}
		if (product.getIsGift()) {
			return Message.warn("mobile.cart.notForSale");
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
			return Message.warn("mobile.cart.addCountNotAllowed", Cart.MAX_PRODUCT_COUNT);
		}
		PackagUnit packagUnit = packagUnitService.find(packagUnitId);
		if (cart.contains(product)) {
			CartItem cartItem = cart.getCartItem(product);
			if (CartItem.MAX_QUANTITY != null && cartItem.getQuantity() + quantity > CartItem.MAX_QUANTITY) {
				return Message.warn("mobile.cart.maxCartItemQuantity", CartItem.MAX_QUANTITY);
			}
			if (product.getStock() != null && cartItem.getQuantity() + quantity > product.getAvailableStock()) {
				return Message.warn("mobile.cart.productLowStock");
			}
			cartItem.add(quantity, packagUnit);
			cartItemService.update(cartItem);
		} else {
			if (CartItem.MAX_QUANTITY != null && quantity > CartItem.MAX_QUANTITY) {
				return Message.warn("mobile.cart.maxCartItemQuantity", CartItem.MAX_QUANTITY);
			}
			if (product.getStock() != null && quantity > product.getAvailableStock()) {
				return Message.warn("mobile.cart.productLowStock");
			}
			CartItem cartItem = new CartItem();
			cartItem.setSelected(true);
			cartItem.setQuantity(quantity);
			cartItem.setProduct(product);
			cartItem.setCart(cart);
			cartItem.setPackagUnit(packagUnit);
			cartItemService.save(cartItem);
			cart.getCartItems().add(cartItem);
		}

		WebUtils.addCookie(request, response, Cart.CART_COUNT, cart.getQuantity() + "", Cart.TIMEOUT);
		if (member == null) {
			WebUtils.addCookie(request, response, Cart.ID_COOKIE_NAME, cart.getId().toString(), Cart.TIMEOUT);
			WebUtils.addCookie(request, response, Cart.KEY_COOKIE_NAME, cart.getKey(), Cart.TIMEOUT);
		}
		return Message.success(cart.getQuantity() + "");
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list-old", method = RequestMethod.GET)
	public String list(String username, HttpServletRequest request, ModelMap model) {
		Member member = memberService.getCurrent();
		if (member != null) {
			model.addAttribute("cart", member.getCart());
		} else {
			model.addAttribute("cart", cartService.getCurrent());
		}
		List<ProductCategory> productCategoryRootList = productCategoryService.findRoots();
		model.addAttribute("member", member);
		model.addAttribute("productCategoryRootList", productCategoryRootList);
		return "/wap/cart/list-old";
	}
	// 新版购物车列表
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list1(String username, HttpServletRequest request, ModelMap model) {
		Member member = memberService.getCurrent();
		if (member != null) {
			model.addAttribute("cart", member.getCart());
		} else {
			model.addAttribute("cart", cartService.getCurrent());
		}
		List<ProductCategory> productCategoryRootList = productCategoryService.findRoots();
		model.addAttribute("member", member);
		model.addAttribute("productCategoryRootList", productCategoryRootList);
		return "/wap/cart/list";
	}

	/**
	 * 选择
	 */
	@RequestMapping(value = "/selected", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> selected(String ids) {
		Cart cart = cartService.getCurrent();
		Map<String, Object> data = new HashMap<String, Object>();
//		if (cart == null || cart.isEmpty()) {
//			data.put("message", Message.error("b2b.cart.notEmpty"));
//			return data;
//		}
		for (CartItem cartItem:cart.getCartItems()) {
			cartItem.setSelected(false);
			cartItemService.save(cartItem);
		}
		if (ids!=null && !"".equals(ids)) {
			String[] dds = ids.split(",");
			for (String id:dds) {
				CartItem cartItem = cartItemService.find(Long.parseLong(id));
				cartItem.setSelected(true);
				cartItemService.save(cartItem);
			}
		}
		data.put("message", SUCCESS_MESSAGE);
		data.put("quantity", cart.getQuantity());
		data.put("effectivePoint", cart.getEffectivePoint());
		data.put("effectivePrice", cart.getEffectivePrice());
		data.put("freight", cart.getFreight());
		data.put("discount", cart.getDiscount());
		data.put("effectiveQuantity", cart.getEffectiveQuantity());
		return data;
	}

	/**
	 * 编辑
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> edit(HttpServletRequest request, HttpServletResponse response, Long id, Integer quantity) {
		Map<String, Object> data = new HashMap<String, Object>();
		if (quantity == null || quantity < 1) {
			data.put("message", ERROR_MESSAGE);
			return data;
		}
		Cart cart = cartService.getCurrent();
//		if (cart == null || cart.isEmpty()) {
//			data.put("message", Message.error("shop.cart.notEmpty"));
//			return data;
//		}
		CartItem cartItem = cartItemService.find(id);
		Set<CartItem> cartItems = cart.getCartItems();
		if (cartItem == null || cartItems == null || !cartItems.contains(cartItem)) {
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
		cartItemService.update(cartItem);

		data.put("message", SUCCESS_MESSAGE);
		data.put("subtotal", cartItem.getEffectiveAmount());//cartItem.getSubtotal()
		data.put("isLowStock", cartItem.getIsLowStock());
		data.put("quantity", cart.getQuantity());
		data.put("effectivePoint", cart.getEffectivePoint());
		data.put("effectivePrice", cart.getEffectivePrice());
		data.put("freight", cart.getFreight());
		data.put("promotions", cart.getPromotions());
		data.put("giftItems", cart.getGiftItems());
		data.put("discount", cart.getDiscount());
		data.put("effectiveQuantity", cart.getEffectiveQuantity());
		WebUtils.addCookie(request, response, Cart.CART_COUNT, cart.getQuantity() + "", Cart.TIMEOUT);
		return data;
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> delete(HttpServletRequest request, HttpServletResponse response, Long[] ids) {
		Map<String, Object> data = new HashMap<String, Object>();
		Cart cart = cartService.getCurrent();
		if (cart == null || cart.isEmpty()) {
			data.put("message", Message.error("shop.cart.notEmpty"));
			return data;
		}
		for (Long id : ids) {
			CartItem cartItem = cartItemService.find(id);
			Set<CartItem> cartItems = cart.getCartItems();
			if (cartItem == null || cartItems == null || !cartItems.contains(cartItem)) {
				data.put("message", Message.error("shop.cart.cartItemNotExsit"));
				return data;
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
		WebUtils.addCookie(request, response, Cart.CART_COUNT, cart.getQuantity() + "", Cart.TIMEOUT);
		return data;
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "/del", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> del(HttpServletRequest request, HttpServletResponse response, Long[] ids) {
		Map<String, Object> data = new HashMap<String, Object>();
		Cart cart = cartService.getCurrent();
//		if (cart == null || cart.isEmpty()) {
//			data.put("message", Message.error("shop.cart.notEmpty"));
//			return data;
//		}
		for (Long id : ids) {
			CartItem cartItem = cartItemService.find(id);
			Set<CartItem> cartItems = cart.getCartItems();
			if (cartItem == null || cartItems == null || !cartItems.contains(cartItem)) {
				data.put("message", Message.error("shop.cart.cartItemNotExsit"));
				return data;
			}
			cartItems.remove(cartItem);
			cartItemService.delete(cartItem);
		}
		data.put("message", SUCCESS_MESSAGE);
		return data;
	}

	/**
	 * 清空
	 */
	@RequestMapping(value = "/clear", method = RequestMethod.POST)
	public @ResponseBody Message clear(HttpServletRequest request, HttpServletResponse response) {
		Cart cart = cartService.getCurrent();
		cartService.delete(cart);
		WebUtils.addCookie(request, response, Cart.CART_COUNT, "0", Cart.TIMEOUT);
		return SUCCESS_MESSAGE;
	}

}