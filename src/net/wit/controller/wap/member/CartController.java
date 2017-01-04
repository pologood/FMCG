/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.wap.member;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import net.wit.Message;
import net.wit.controller.wap.BaseController;
import net.wit.entity.Cart;
import net.wit.entity.CartItem;
import net.wit.entity.Member;
import net.wit.entity.Product;
import net.wit.entity.Receiver;
import net.wit.service.AppointmentService;
import net.wit.service.CartItemService;
import net.wit.service.CartService;
import net.wit.service.MemberService;
import net.wit.service.PaymentMethodService;
import net.wit.service.ProductService;
import net.wit.service.ReceiverService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller - 购物车
 * 
 * @author rsico Team
 * @version 3.0
 */
@Controller("wapMemberCartController")
@RequestMapping("/wap/member/cart")
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

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Long receiverId, Long productId,ModelMap model) {
		Cart cart = cartService.getCurrent();
		model.addAttribute("cart", cart);
		if (cart != null) {
			model.addAttribute("cartToken", cart.getToken());
		} else {
			model.addAttribute("cartToken", "");
		}
		Member member = memberService.getCurrent();
		if (receiverId != null) {
			Receiver receiver = receiverService.find(receiverId);
			model.addAttribute("receiver", receiver);
		} else {
			Receiver defaultReceiver = receiverService.findDefault(member);
			model.addAttribute("receiver", defaultReceiver);
		}
		model.addAttribute("paymentmethods", paymentMethodService.findAll());
		model.addAttribute("defaultPaymentmethod", paymentMethodService.findFirst());
		model.addAttribute("appointments", appointmentService.findAll());
		model.addAttribute("defaultAppointment", appointmentService.findFirst());
		if(productId!=null){
			model.addAttribute("productId", productId);
		}else{
			model.addAttribute("productId", 362);
		}
		return "/wap/member/cart/list";
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
			data.put("message", Message.error("wap.cart.notEmpty"));
			return data;
		}
		CartItem cartItem = cartItemService.find(id);
		Set<CartItem> cartItems = cart.getCartItems();
		if (cartItem == null || cartItems == null || !cartItems.contains(cartItem)) {
			data.put("message", Message.error("wap.cart.cartItemNotExsit"));
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
	 * 编辑
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> edit(Long id, Integer quantity) {
		Map<String, Object> data = new HashMap<String, Object>();
		if (quantity == null || quantity < 1) {
			data.put("message", ERROR_MESSAGE);
			return data;
		}
		Cart cart = cartService.getCurrent();
		if (cart == null || cart.isEmpty()) {
			data.put("message", Message.error("wap.cart.notEmpty"));
			return data;
		}
		CartItem cartItem = cartItemService.find(id);
		Set<CartItem> cartItems = cart.getCartItems();
		if (cartItem == null || cartItems == null || !cartItems.contains(cartItem)) {
			data.put("message", Message.error("wap.cart.cartItemNotExsit"));
			return data;
		}
		if (CartItem.MAX_QUANTITY != null && quantity > CartItem.MAX_QUANTITY) {
			data.put("message", Message.warn("wap.cart.maxCartItemQuantity", CartItem.MAX_QUANTITY));
			return data;
		}
		Product product = cartItem.getProduct();
		if (product.getStock() != null && quantity > product.getAvailableStock()) {
			data.put("message", Message.warn("wap.cart.productLowStock"));
			return data;
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
		return data;
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
	 * 删除
	 */
	@RequestMapping(value = "/batchDelete", method = RequestMethod.POST)
	public @ResponseBody
	Message batchDelete(String idsStr) {
		String[] ids = idsStr.split("-");
		for (int i = 0; i < ids.length; i++) {
			Long id = Long.parseLong(ids[i]);
			CartItem item = cartItemService.find(id);
			if (item == null) {
				return ERROR_MESSAGE;
			}
			Member member = memberService.getCurrent();
			if (!member.equals(item.getCart().getMember())) {
				return ERROR_MESSAGE;
			}
			cartItemService.delete(id);
		}
		return SUCCESS_MESSAGE;
	}

}