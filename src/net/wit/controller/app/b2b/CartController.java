/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.app.b2b;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
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

import net.wit.controller.app.BaseController;
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
 * @author rsico Team
 * @version 3.0
 */
@Controller("appB2bCartController")
@RequestMapping("/app/b2b/cart")
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
	 * 添加
	 */
	@RequestMapping(value = "/get_cart_count")
	public @ResponseBody DataBlock getCartCount(HttpServletRequest request, HttpServletResponse response) {
		Cart cart = cartService.getCurrent();
		Map<String, Object> data = new HashMap<String, Object>();
		if (cart!=null) {
			data.put("count", cart.getCartItems().size());
			data.put("quantity", cart.getQuantity());
			data.put("effectiveQuantity", cart.getEffectiveQuantity());
			data.put("effectivePoint", cart.getEffectivePoint());
			data.put("effectivePrice", cart.getEffectivePrice());
			data.put("freightPrice", cart.getFreight());
			data.put("discountPrice", cart.getDiscount());
			data.put("isLowStock", cart.getIsLowStock());
		} else {
			data.put("count", 0);
			data.put("quantity", 0);
			data.put("effectiveQuantity", 0);
			data.put("effectivePoint", 0);
			data.put("effectivePrice", 0);
			data.put("freightPrice", 0);
			data.put("discountPrice", 0);
			data.put("isLowStock", false);
		}
		return DataBlock.success(data,"执行成功");
	}


	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public @ResponseBody DataBlock add(Long id, Integer quantity, Long packagUnitId,String type, HttpServletRequest request, HttpServletResponse response) {
		if (quantity == null || quantity < 1) {
			return DataBlock.error("无效数量");
		}
		Product product = productService.find(id);
		if (product == null) {
			return DataBlock.error("b2b.cart.productNotExsit");
		}
		if (!product.getIsMarketable()) {
			return DataBlock.error("b2b.cart.productNotMarketable");
		}
		//if (product.getIsGift()) {
		//	return DataBlock.error("b2b.cart.notForSale");
		//}
		
		Cart cart = cartService.getCurrent();
		Member member = memberService.getCurrent();

		if (member != null)
		{
			if (product.getTenant().equals(member.getTenant())) {
				return DataBlock.error("您无法选购自己店铺的商品");
			}
		}
		
		if (cart == null) {
			cart = new Cart();
			cart.setKey(UUID.randomUUID().toString() + DigestUtils.md5Hex(RandomStringUtils.randomAlphabetic(30)));
			cart.setMember(member);
			cartService.save(cart);
		}

		if (Cart.MAX_PRODUCT_COUNT != null && cart.getCartItems().size() >= Cart.MAX_PRODUCT_COUNT) {
			return DataBlock.error("b2b.cart.addCountNotAllowed", Cart.MAX_PRODUCT_COUNT);
		}
		
		if ("buy".equals(type)) {
			for (CartItem cartItem:cart.getCartItems()) {
				cartItem.setSelected(false);
				cartItemService.save(cartItem);
			}
		}
		
		PackagUnit packagUnit = packagUnitService.find(packagUnitId);
		Integer currentQuantity = 0;
		BigDecimal price = BigDecimal.ZERO;
		if (cart.contains(product)) {
			CartItem cartItem = cart.getCartItem(product);
			if (CartItem.MAX_QUANTITY != null && cartItem.getQuantity() + quantity > CartItem.MAX_QUANTITY) {
				return DataBlock.error("b2b.cart.maxCartItemQuantity", CartItem.MAX_QUANTITY);
			}
			if (product.getStock() != null && cartItem.getQuantity() + quantity > product.getAvailableStock()) {
				return DataBlock.error("b2b.cart.productLowStock");
			}
			cartItem.setSelected(true);
			if ("buy".equals(type)) {
				cartItem.setQuantity(quantity);
			} else {
				cartItem.add(quantity, packagUnit);
			}
			currentQuantity = cartItem.getQuantity();
			price = cartItem.getEffectivePrice();
			cartItemService.update(cartItem);
		} else {
			if (CartItem.MAX_QUANTITY != null && quantity > CartItem.MAX_QUANTITY) {
				return DataBlock.error("b2b.cart.maxCartItemQuantity", CartItem.MAX_QUANTITY);
			}
			if (product.getStock() != null && quantity > product.getAvailableStock()) {
				return DataBlock.error("b2b.cart.productLowStock");
			}
			CartItem cartItem = new CartItem();
			cartItem.setSelected(true);
			cartItem.setQuantity(quantity);
			cartItem.setProduct(product);
			cartItem.setPackagUnit(packagUnit);
			cartItem.setCart(cart);
			currentQuantity = cartItem.getQuantity();
			price = cartItem.getEffectivePrice();
			cartItemService.save(cartItem);
			cart.getCartItems().add(cartItem);
		}
		
		WebUtils.addCookie(request, response, Cart.CART_COUNT, cart.getQuantity() + "", Cart.TIMEOUT);
		if (member == null) {
			WebUtils.addCookie(request, response, Cart.ID_COOKIE_NAME, cart.getId().toString(), Cart.TIMEOUT);
			WebUtils.addCookie(request, response, Cart.KEY_COOKIE_NAME, cart.getKey(), Cart.TIMEOUT);
		}
		
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("count", cart.getCartItems().size());
		data.put("effectiveQuantity", cart.getEffectiveQuantity());
		data.put("currentQuantity", currentQuantity);
		data.put("quantity", cart.getQuantity());
		data.put("effectivePoint", cart.getEffectivePoint());
		data.put("effectivePrice", cart.getEffectivePrice());
		data.put("price", price);
		data.put("freightPrice", cart.getFreight());
		data.put("discountPrice", cart.getDiscount());
		data.put("isLowStock", cart.getIsLowStock());
		return DataBlock.success(data,"执行成功");
	}

	/**
	 * 减
	 */
	@RequestMapping(value = "/dec", method = RequestMethod.POST)
	public @ResponseBody DataBlock dec(Long id, Integer quantity, Long packagUnitId, HttpServletRequest request, HttpServletResponse response) {

		if (quantity == null || quantity < 1) {
			return DataBlock.error("无效数量");
		}
		
		Product product = productService.find(id);
		if (product == null) {
			return DataBlock.error("b2b.cart.productNotExsit");
		}
		if (!product.getIsMarketable()) {
			return DataBlock.error("b2b.cart.productNotMarketable");
		}
		//if (product.getIsGift()) {
		//	return DataBlock.error("b2b.cart.notForSale");
		//}
		
		Cart cart = cartService.getCurrent();  
		Member member = memberService.getCurrent();

		if (member != null)
		{
			if (product.getTenant().equals(member.getTenant())) {
				return DataBlock.error("您无法选购自己店铺的商品");
			}
		}
		
		if (cart == null) {
			cart = new Cart();
			cart.setKey(UUID.randomUUID().toString() + DigestUtils.md5Hex(RandomStringUtils.randomAlphabetic(30)));
			cart.setMember(member);
			cartService.save(cart);
		}

		if (Cart.MAX_PRODUCT_COUNT != null && cart.getCartItems().size() >= Cart.MAX_PRODUCT_COUNT) {
			return DataBlock.error("b2b.cart.addCountNotAllowed", Cart.MAX_PRODUCT_COUNT);
		}
		
		PackagUnit packagUnit = packagUnitService.find(packagUnitId);
		Integer currentQuantity = 0;
		BigDecimal price = BigDecimal.ZERO;
		if (cart.contains(product)) {
			CartItem cartItem = cart.getCartItem(product);
			if (CartItem.MAX_QUANTITY != null && cartItem.getQuantity() - quantity > CartItem.MAX_QUANTITY) {
				return DataBlock.error("b2b.cart.maxCartItemQuantity", CartItem.MAX_QUANTITY);
			}
			if (product.getStock() != null && cartItem.getQuantity() - quantity > product.getAvailableStock()) {
				return DataBlock.error("b2b.cart.productLowStock");
			}
			cartItem.dec(quantity, packagUnit);
			cartItem.setSelected(true);
			currentQuantity = cartItem.getQuantity();
			price = cartItem.getEffectivePrice();
			if (currentQuantity==0) {
			   cartItemService.delete(cartItem);
			   cart.getCartItems().remove(cartItem);
			} else {
		   	   cartItemService.update(cartItem);
			}
		} else {
			if (CartItem.MAX_QUANTITY != null && quantity > CartItem.MAX_QUANTITY) {
				return DataBlock.error("b2b.cart.maxCartItemQuantity", CartItem.MAX_QUANTITY);
			}
			if (product.getStock() != null && quantity > product.getAvailableStock()) {
				return DataBlock.error("b2b.cart.productLowStock");
			}
			CartItem cartItem = new CartItem();
			cartItem.setSelected(true);
			cartItem.setQuantity(quantity);
			cartItem.setProduct(product);
			cartItem.setPackagUnit(packagUnit);
			cartItem.setCart(cart);
			currentQuantity = cartItem.getQuantity();
			price = cartItem.getEffectivePrice();
			cartItemService.save(cartItem);
			cart.getCartItems().add(cartItem);
		}

		WebUtils.addCookie(request, response, Cart.CART_COUNT, cart.getQuantity() + "", Cart.TIMEOUT);
		if (member == null) {
			WebUtils.addCookie(request, response, Cart.ID_COOKIE_NAME, cart.getId().toString(), Cart.TIMEOUT);
			WebUtils.addCookie(request, response, Cart.KEY_COOKIE_NAME, cart.getKey(), Cart.TIMEOUT);
		}
		
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("count", cart.getCartItems().size());
		data.put("effectiveQuantity", cart.getEffectiveQuantity());
		data.put("currentQuantity", currentQuantity);
		data.put("quantity", cart.getQuantity());
		data.put("effectivePoint", cart.getEffectivePoint());
		data.put("effectivePrice", cart.getEffectivePrice());
		data.put("freightPrice", cart.getFreight());
		data.put("discountPrice", cart.getDiscount());
		data.put("isLowStock", cart.getIsLowStock());
		data.put("price", price);
		return DataBlock.success(data,"执行成功");
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public @ResponseBody DataBlock list() {
		Cart cart = cartService.getCurrent();
		CartModel model = new CartModel();
		if (cart!=null) {
			model.copyFrom(cart);
		}
		return DataBlock.success(model,"执行成功");
	}

	/**
	 * 编辑
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public @ResponseBody DataBlock edit(Long id, Integer quantity, Long packagUnitId) {
		Map<String, Object> data = new HashMap<String, Object>();
		if (quantity == null || quantity < 1) {
			return DataBlock.error("无效数量");
		}
		Cart cart = cartService.getCurrent();
		//if (cart == null || cart.isEmpty()) {
		//	return DataBlock.error("购物车为空");
		//}
		CartItem cartItem = cartItemService.find(id);
		if (cartItem == null) {
			return DataBlock.error("购物车商品不存在");
		}
		if (packagUnitId == null) {
			cartItem.setPackagUnit(null);
		} else {
			PackagUnit packagUnit = packagUnitService.find(packagUnitId);
			cartItem.setPackagUnit(packagUnit);
		}
		Set<CartItem> cartItems = cart.getCartItems();
		if (cartItems == null || !cartItems.contains(cartItem)) {
			return DataBlock.error("b2b.cart.cartItemNotExsit");
		}
		if (CartItem.MAX_QUANTITY != null && quantity > CartItem.MAX_QUANTITY) {
			return DataBlock.error("b2b.cart.maxCartItemQuantity", CartItem.MAX_QUANTITY);
		}
		Product product = cartItem.getProduct();
		if (product.getStock() != null && quantity > product.getAvailableStock()) {
			return DataBlock.error("b2b.cart.productLowStock");
		}
		cartItem.setQuantity(quantity);
		cartItem.setSelected(true);
		cartItemService.save(cartItem);

		data.put("count", cart.getCartItems().size());
		data.put("effectiveQuantity", cart.getEffectiveQuantity());
		data.put("price", cartItem.getEffectivePrice());
		data.put("quantity", cart.getQuantity());
		data.put("effectivePoint", cart.getEffectivePoint());
		data.put("effectivePrice", cart.getEffectivePrice());
		data.put("freightPrice", cart.getFreight());
		data.put("discountPrice", cart.getDiscount());
		data.put("isLowStock", cart.getIsLowStock());
		return DataBlock.success(data,"执行成功");
	}

	/**
	 * 选择
	 */
	@RequestMapping(value = "/selected", method = RequestMethod.POST)
	public @ResponseBody DataBlock selected(Long[] ids,Boolean flag) {
		Map<String, Object> data = new HashMap<String, Object>();
		Cart cart = cartService.getCurrent();
		if (cart==null) {
			return DataBlock.success(data,"执行成功");
		}
			for (Long id:ids) {
		   	  CartItem cartItem = cartItemService.find(id);
		      cartItem.setSelected(flag);
			  cartItemService.update(cartItem);
			}

		data.put("count", cart.getCartItems().size());
		data.put("effectiveQuantity", cart.getEffectiveQuantity());
		data.put("quantity", cart.getQuantity());
		data.put("effectivePoint", cart.getEffectivePoint());
		data.put("effectivePrice", cart.getEffectivePrice());
		data.put("freightPrice", cart.getFreight());
		data.put("discountPrice", cart.getDiscount());
		data.put("isLowStock", cart.getIsLowStock());
		return DataBlock.success(data,"执行成功");
	}
	
	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody DataBlock delete(Long[] ids) {
		Map<String, Object> data = new HashMap<String, Object>();
		Cart cart = cartService.getCurrent();
		if (cart == null || cart.isEmpty()) {
			return DataBlock.error("b2b.cart.notEmpty");
		}
		List<CartItem> cartItems = cartItemService.findList(ids);
		for (CartItem cartItem:cartItems) {
			cartItem.setSelected(false);
			cartItemService.delete(cartItem);
		}
	    cart = cartService.getCurrent();
		data.put("count", cart.getCartItems().size());
		data.put("effectiveQuantity", cart.getEffectiveQuantity());
		data.put("quantity", cart.getQuantity());
		data.put("effectivePoint", cart.getEffectivePoint());
		data.put("effectivePrice", cart.getEffectivePrice());
		data.put("freightPrice", cart.getFreight());
		data.put("discountPrice", cart.getDiscount());
		data.put("isLowStock", cart.getIsLowStock());
		return DataBlock.success(data,"执行成功");
	}


	
	/**
	 * 清空
	 */
	@RequestMapping(value = "/clear", method = RequestMethod.POST)
	public @ResponseBody DataBlock clear() {
		Cart cart = cartService.getCurrent();
		if (cart==null) {
			return DataBlock.success("success","执行成功");
		}
		cartService.delete(cart);
		return DataBlock.success("success", "执行成功");
		
	}

}