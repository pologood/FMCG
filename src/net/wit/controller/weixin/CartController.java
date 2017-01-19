/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.weixin;

import net.wit.controller.weixin.model.CartModel;
import net.wit.controller.weixin.model.DataBlock;
import net.wit.entity.*;
import net.wit.service.*;
import net.wit.util.WebUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;

/**
 * Controller - 购物车
 *
 * @author rsico Team
 * @version 3.0
 */
@Controller("weixinCartController")
@RequestMapping("/weixin/cart")
public class CartController extends BaseController {

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "cartServiceImpl")
    private CartService cartService;

    @Resource(name = "cartItemServiceImpl")
    private CartItemService cartItemService;

    @Resource(name = "tenantServiceImpl")
    private TenantService tenantService;

    @Resource(name = "packagUnitServiceImpl")
    private PackagUnitService packagUnitService;
    @Resource(name = "productServiceImpl")
    private ProductService productService;


    /**
     * 添加至
     * params id 商品
     * quantity 添加数量
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public
    @ResponseBody
    DataBlock add(Long id, Integer quantity, String type, Long packagUnitId, HttpServletRequest request, HttpServletResponse response) {
        if (quantity == null || quantity < 1) {
            return DataBlock.error("无效数量");
        }
        Product product = productService.find(id);
        if (product == null) {
            return DataBlock.error("weixin.cart.productNotExsit");
        }
        if (!product.getIsMarketable()) {
            return DataBlock.error("weixin.cart.productNotMarketable");
        }
        if (product.getIsGift()) {
            return DataBlock.error("weixin.cart.notForSale");
        }

        if(product.getTenant()!=null){
            if(product.getTenant().getEnd()!=null&&product.getTenant().getEnd()){
                return DataBlock.error("商家已打烊");
            }
        }

        Cart cart = cartService.getCurrent();
        Member member = memberService.getCurrent();

        if (member != null) {
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
            return DataBlock.error("weixin.cart.addCountNotAllowed", Cart.MAX_PRODUCT_COUNT);
        }

        if ("buy".equals(type)) {
            for (CartItem cartItem : cart.getCartItems()) {
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
                return DataBlock.error("weixin.cart.maxCartItemQuantity", CartItem.MAX_QUANTITY);
            }
            if (product.getStock() != null && cartItem.getQuantity() + quantity > product.getAvailableStock()) {
                return DataBlock.error("weixin.cart.productLowStock");
            }
            if ("buy".equals(type)) {
                cartItem.setQuantity(quantity);
            } else {
                cartItem.add(quantity, packagUnit);
            }
            cartItem.setSelected(true);
            currentQuantity = cartItem.getQuantity();
            cartItemService.update(cartItem);
            price = cartItem.getEffectivePrice();
        } else {
            if (CartItem.MAX_QUANTITY != null && quantity > CartItem.MAX_QUANTITY) {
                return DataBlock.error("weixin.cart.maxCartItemQuantity", CartItem.MAX_QUANTITY);
            }
            if (product.getStock() != null && quantity > product.getAvailableStock()) {
                return DataBlock.error("weixin.cart.productLowStock");
            }
            CartItem cartItem = new CartItem();
            cartItem.setSelected(true);
            cartItem.setQuantity(quantity);
            cartItem.setProduct(product);
            cartItem.setPackagUnit(packagUnit);
            cartItem.setCart(cart);
            currentQuantity = cartItem.getQuantity();
            cartItemService.save(cartItem);
            cart.getCartItems().add(cartItem);
            price = cartItem.getEffectivePrice();
        }

        WebUtils.addCookie(request, response, Cart.CART_COUNT, cart.getQuantity() + "", Cart.TIMEOUT);
        if (member == null) {
            WebUtils.addCookie(request, response, Cart.ID_COOKIE_NAME, cart.getId().toString(), Cart.TIMEOUT);
            WebUtils.addCookie(request, response, Cart.KEY_COOKIE_NAME, cart.getKey(), Cart.TIMEOUT);
        }
        Map<String, Object> data = new HashMap<>();
        data.put("effectivePrice", cart.getEffectivePrice());
        data.put("cartCount", cart.getQuantity());
        return DataBlock.success(data, "添加购物车成功");
    }

    /**
     * 购物车列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public
    @ResponseBody
    DataBlock list() {
        Cart cart = cartService.getCurrent();
        CartModel model = new CartModel();
        if (cart != null) {
            model.copyFrom(cart);
        }
        return DataBlock.success(model, "执行成功");
    }

    /**
     * 编辑数量
     * id 商品
     * quantity 数量
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public
    @ResponseBody
    DataBlock edit(Long id, Integer quantity) {
        Map<String, Object> data = new HashMap<>();
        if (quantity == null || quantity < 1) {
            return DataBlock.error("无效数量");
        }
        Cart cart = cartService.getCurrent();
        CartItem cartItem = cartItemService.find(id);
        if (cartItem == null) {
            return DataBlock.error("购物车商品不存在");
        }
        Set<CartItem> cartItems = cart.getCartItems();
        if (cartItems == null || !cartItems.contains(cartItem)) {
            return DataBlock.error("weixin.cart.cartItemNotExsit");
        }
        if (CartItem.MAX_QUANTITY != null && quantity > CartItem.MAX_QUANTITY) {
            return DataBlock.error("weixin.cart.maxCartItemQuantity", CartItem.MAX_QUANTITY);
        }
        Product product = cartItem.getProduct();
        if (product.getStock() != null && quantity > product.getAvailableStock()) {
            return DataBlock.error("weixin.cart.productLowStock");
        }
        cartItem.setQuantity(quantity);
        cartItem.setSelected(true);
        cartItemService.save(cartItem);
        data.put("effectivePrice", cart.getEffectivePrice());
        return DataBlock.success(data, "执行成功");
    }

    /**
     * 选择
     */
    @RequestMapping(value = "/selected", method = RequestMethod.POST)
    public
    @ResponseBody
    DataBlock selected(Long[] ids) {
        Map<String, Object> data;
        try {
            data = new HashMap<>();
            Cart cart = cartService.getCurrent();
            if (cart == null) {
                return DataBlock.success(data, "执行成功");
            }
            for(CartItem cartItem:cart.getCartItems()){
                cartItem.setSelected(false);
                cartItemService.update(cartItem);
            }
            if(ids!=null){
                for (Long id : ids) {
                    CartItem cartItem = cartItemService.find(id);
                    if (cartItem == null) {
                        return DataBlock.error("无效购物项id");
                    }
                    cartItem.setSelected(true);
                    cartItemService.update(cartItem);
                }
            }
            data.put("effectivePrice", cart.getEffectivePrice());
        } catch (Exception e) {
            System.out.print("购物车商品选择接口调用失败：");
            e.printStackTrace();
            return DataBlock.error("未知错误");
        }
        return DataBlock.success(data, "执行成功");
    }

    /**
     * 删除
     * ids 商品数组，多个商品
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public
    @ResponseBody
    DataBlock delete(Long[] ids) {
        Map<String, Object> data = new HashMap<>();
        Cart cart = cartService.getCurrent();
        if (cart == null) {
            return DataBlock.error("weixin.cart.notEmpty");
        }
        List<CartItem> cartItems = cartItemService.findList(ids);
        if (cartItems.size() == 0) {
            return DataBlock.error("购物项id无效");
        }
        for (CartItem cartItem : cartItems) {
            cartItem.setSelected(false);
            cartItemService.delete(cartItem);
        }
        data.put("effectivePrice", cart.getEffectivePrice());
        return DataBlock.success(data, "执行成功");
    }

    /**
     * 清空
     */
    @RequestMapping(value = "/clear", method = RequestMethod.POST)
    public
    @ResponseBody
    DataBlock clear() {
        Cart cart = cartService.getCurrent();
        if (cart == null) {
            return DataBlock.success("success", "执行成功");
        }
        cartService.delete(cart);
        return DataBlock.success("success", "执行成功");

    }


}