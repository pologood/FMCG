package net.wit.controller.pad;

import net.wit.Message;
import net.wit.controller.assistant.model.DataBlock;
import net.wit.controller.b2c.BaseController;
import net.wit.controller.pad.model.CartModel;
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

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;

/**
 * Controller - 购物车
 * @author rsico Team
 * @version 3.0
 */
@Controller("padCartController")
@RequestMapping("/pad/cart")
public class CartController extends BaseController {
    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "cartServiceImpl")
    private CartService cartService;

    @Resource(name = "productServiceImpl")
    private ProductService productService;

    @Resource(name = "cartItemServiceImpl")
    private CartItemService cartItemService;

    @Resource(name = "packagUnitServiceImpl")
    private PackagUnitService packagUnitService;

    /**
     * 购物车
     * type:order 确认订单
     * coupons 已选优惠券
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock shoppingCart(String type,String coupons,HttpServletRequest request,HttpServletResponse response) {
        Cart cart = cartService.getCurrent();
        if(cart==null){
            return  DataBlock.success(null, "执行成功");
        }
        CartModel model = new CartModel();
        model.copyFrom(cart,type,coupons);
        return  DataBlock.success(model, "执行成功");
    }

    /**
     * 根据商品条目id获取商品库存
     */
    @RequestMapping(value = "/stock", method = RequestMethod.GET)
    public @ResponseBody
    DataBlock stock(Long id) {
        Integer stock = 0;
        CartItem cartItem = cartItemService.find(id);
        if(cartItem==null){
            stock = 0;
        }else {
            Product product = cartItem.getProduct();
            stock = product.getAvailableStock();
            if (product == null) {
                stock = 0;
            }
            if (!product.getIsMarketable()) {
                stock = 0;
            }
            if (product.getIsGift()) {
                stock = 0;
            }
        }
        Map map =new HashMap();
        map.put("stock",stock);
        return DataBlock.success(map,"执行成功");
    }

    /**
     * 添加到购物车
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public DataBlock add(Long id, Integer quantity, String type, Long packagUnitId, HttpServletRequest request, HttpServletResponse response) {
        if (quantity == null || quantity < 1) {
            return DataBlock.error("操作错误！");
        }
        Product product = productService.find(id);
        if (product == null) {
            return DataBlock.error("该商品不存在！");
        }
        if (!product.getIsMarketable()) {
            return DataBlock.error("该商品未上架！");
        }
        if (product.getIsGift()) {
            return DataBlock.error("该商品是赠品！");
        }

        Cart cart = cartService.getCurrent();
        if (cart == null) {
            cart = new Cart();
            cart.setKey(UUID.randomUUID().toString() + DigestUtils.md5Hex(RandomStringUtils.randomAlphabetic(30)));
            cartService.save(cart);
        }

        if (Cart.MAX_PRODUCT_COUNT != null && cart.getCartItems().size() >= Cart.MAX_PRODUCT_COUNT) {
            return DataBlock.error("超出购物车放置上限！");
        }

        if ("buy".equals(type)) {
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
                return DataBlock.error("超出商品最大购买数！");
            }
            if (product.getStock() != null && cartItem.getQuantity() + quantity > product.getAvailableStock()) {
                return DataBlock.error("商品库存不足！");
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
            }
        } else {
            if (CartItem.MAX_QUANTITY != null && quantity > CartItem.MAX_QUANTITY) {
                return DataBlock.error("超出商品最大购买数！");
            }
            if (product.getStock() != null && quantity > product.getAvailableStock()) {
                return DataBlock.error("商品库存不足！");
            }
            CartItem cartItem = new CartItem();
            cartItem.setQuantity(quantity);
            cartItem.setProduct(product);
            cartItem.setPackagUnit(packagUnit);
            cartItem.setSelected(true);
            cartItem.setCart(cart);
            cartItemService.save(cartItem);
            cart.getCartItems().add(cartItem);
        }
        WebUtils.addCookie(request, response, Cart.ID_COOKIE_NAME, cart.getId().toString(), Cart.TIMEOUT);
        WebUtils.addCookie(request, response, Cart.KEY_COOKIE_NAME, cart.getKey(), Cart.TIMEOUT);
        return DataBlock.success("success", "执行成功");
    }

    /**
     * 获取购物车物品数量
     */
    @RequestMapping(value = "/cart_count", method = RequestMethod.POST)
    public @ResponseBody DataBlock getCartCount() {
        Cart cart = cartService.getCurrent();
        Map<String, Object> data = new HashMap<String, Object>();
        if (cart == null) {
            data.put("count", 0);
        } else {
            data.put("count", cart.getCartItems().size());
        }
        return DataBlock.success(data, "执行成功");
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/deletes", method = RequestMethod.POST)
    public @ResponseBody DataBlock deletes(HttpServletRequest request, HttpServletResponse response, String ids) {
        Map<String, Object> data = new HashMap<String, Object>();
        Cart cart = cartService.getCurrent();
        if (cart == null || cart.isEmpty()) {
            return DataBlock.error("购物车暂无商品！");
        }
        String[] idss = ids.split(",");
        for (String id:idss) {
            CartItem cartItem = cartItemService.find(Long.valueOf(id).longValue());
            Set<CartItem> cartItems = cart.getCartItems();
            if (cartItem == null || cartItems == null || !cartItems.contains(cartItem)) {
                return DataBlock.error("购物车暂无商品！");
            }
            cartItems.remove(cartItem);
            cartItemService.delete(cartItem);
        }
        return DataBlock.success("success", "执行成功");
    }

    /**
     * 选择
     */
    @RequestMapping(value = "/selected", method = RequestMethod.POST)
    public @ResponseBody DataBlock selected(String ids) {
        Cart cart = cartService.getCurrent();
        Map<String, Object> data = new HashMap<String, Object>();
        if (cart == null) {
            return DataBlock.error("购物车暂无商品！");
        }
        for (CartItem cartItem:cart.getCartItems()) {
            cartItem.setSelected(false);
            cartItemService.save(cartItem);
        }
        if (ids!=null && !"".equals(ids)) {
            String[] idss = ids.split(",");
            for (String id:idss) {
                CartItem cartItem = cartItemService.find(Long.valueOf(id).longValue());
                cartItem.setSelected(true);
                cartItemService.save(cartItem);
            }
        }
        data.put("totalPrice", cart.getEffectivePrice());
        data.put("freight", cart.getFreight());
        data.put("totalQuantity", cart.getEffectiveQuantity());
        return DataBlock.success(data, "执行成功");
    }

    /**
     * 编辑
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public @ResponseBody DataBlock edit(Long id, Integer quantity) {
        Map<String, Object> data = new HashMap<String, Object>();
        if (quantity == null || quantity < 1) {
            return DataBlock.error("操作错误！");
        }
        Cart cart = cartService.getCurrent();
//		if (cart == null || cart.isEmpty()) {
//			data.put("message", Message.error("shop.cart.notEmpty"));
//			return data;
//		}
        CartItem cartItem = cartItemService.find(id);
        Set<CartItem> cartItems = cart.getCartItems();
        if (cartItem == null || cartItems == null || !cartItems.contains(cartItem)) {
            return DataBlock.error("购物车暂无商品！");
        }
        if (CartItem.MAX_QUANTITY != null && quantity > CartItem.MAX_QUANTITY) {
            return DataBlock.error("超出商品最大可购买数！");
        }
        Product product = cartItem.getProduct();
        if (product.getStock() != null && quantity > product.getAvailableStock()) {
            return DataBlock.error("超出该商品库存！");
        }
        cartItem.setQuantity(quantity);
        cartItemService.update(cartItem);

        data.put("totalQuantity", cart.getEffectiveQuantity());
        data.put("totalPrice", cart.getEffectivePrice());
        data.put("freight", cart.getFreight());
        //WebUtils.addCookie(request, response, Cart.CART_COUNT, cart.getQuantity() + "", Cart.TIMEOUT);
        return DataBlock.success(data, "执行成功");
    }

}
