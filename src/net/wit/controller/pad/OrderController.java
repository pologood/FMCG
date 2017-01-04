package net.wit.controller.pad;

import net.wit.controller.assistant.model.DataBlock;
import net.wit.controller.wap.BaseController;
import net.wit.entity.*;
import net.wit.service.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by ruanx on 2016/12/1.
 */
@Controller("padOrderController")
@RequestMapping("/pad/order")
public class OrderController extends BaseController {
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

    @Resource(name = "couponCodeServiceImpl")
    private CouponCodeService couponCodeService;

    @Resource(name = "couponServiceImpl")
    private CouponService couponService;

    @Resource(name = "consumerServiceImpl")
    private ConsumerService consumerService;

    @Resource(name = "orderServiceImpl")
    private OrderService orderService;

    @Resource(name = "couponNumberServiceImpl")
    private CouponNumberService couponNumberService;

    /**
     * 扫码将商品添加到我的购物车，并跳转到确认订单
     */
    @RequestMapping(value = "/addAndPay", method = RequestMethod.GET)
    public String addAndPay(String cart_key,String coupons,String token_key, Long packagUnitId) {
        Member member = memberService.getCurrent();
        if (member == null) {
            return "redirect:/wap/index.jhtml";
        }
        Cart cart = cartService.getCurrent();
        Cart padCart = cartService.find(Long.parseLong(cart_key));
        if (cart == null) {
            cart = new Cart();
            cart.setKey(UUID.randomUUID().toString() + DigestUtils.md5Hex(RandomStringUtils.randomAlphabetic(30)));
            cart.setMember(member);
            cartService.save(cart);
        }
        //将当前购物车商品状态设置为未选择
        for (CartItem cartItem : cart.getCartItems()) {
            cartItem.setSelected(false);
            cartItemService.save(cartItem);
        }
        PackagUnit packagUnit = packagUnitService.find(packagUnitId);
        //会员保存该商品
        for (CartItem padCartItem : padCart.getCartItems()) {
            if(padCartItem.getSelected()){
                Product product = padCartItem.getProduct();
                if (cart.contains(product)) {
                    CartItem cartItem = cart.getCartItem(product);
                    cartItem.setSelected(true);
                    cartItem.setQuantity(padCartItem.getQuantity());
                    cartItemService.update(cartItem);
                } else {
                    CartItem cartItem = new CartItem();
                    cartItem.setQuantity(padCartItem.getQuantity());
                    cartItem.setProduct(product);
                    cartItem.setPackagUnit(packagUnit);
                    cartItem.setSelected(true);
                    cartItem.setCart(cart);
                    cartItemService.save(cartItem);
                    cart.getCartItems().add(cartItem);
                }
            }
            //删除购物屏所有商品
            cartItemService.delete(padCartItem);
        }
        //领取优惠券
        if(!"".equals(coupons)&&coupons!=null){
            String[] str = coupons.split(",");
            for (int i = 0; i < str.length; i++) {
                Long couponing = Long.valueOf(str[i]);
                judge(couponing,null);
            }
        }
        //更新购物屏key
        cartService.updateKey(padCart,token_key);
        //跳转微信确认订单
        return "redirect:/wap/member/order/orderPay.jhtml?token_key="+token_key;
    }

    /**
     * 查询当前二维码是否被扫以及是否支付
     */
    @RequestMapping(value = "/status", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock status(String token_key) {
        Cart cart = cartService.getCurrent();
        Map map = new HashMap();
        map.put("type","0");
        map.put("description","待扫码中");
        //若查到更新的key，则用户已扫码
        if(cart==null){
            map.put("type","1");
            map.put("description","已扫码，等待下单");
        }
        //若查到订单备注有该key，且状态已支付完成，返回
        Order order = orderService.findByTokenKey(token_key);
        if(order!=null){
            map.put("type","2");
            map.put("description","下单成功");
        }
        return  DataBlock.success(map, "执行成功");
    }

    /**
     * 优惠券信息
     * code 优惠券编码
     */
    public void judge(Long id, Long no) {
        Coupon coupon = couponService.find(id);
        if (coupon == null) {
            return ;
        }
        Member member = memberService.getCurrent();
        if (member == null) {
            return ;
        }
        if (coupon.getExpired()) {
            if (coupon.getType().equals(Coupon.Type.multipleCoupon)) {
                List<CouponNumber> couponNumbers = couponNumberService.findList(coupon, null,null, no);
                if(couponNumbers!=null&&couponNumbers.size()>0){
                    CouponNumber couponNumber=couponNumbers.get(0);
                    if(couponNumber.getStatus().equals(CouponNumber.Status.bound)){
                        CouponCode couponCode = couponCodeService.build(coupon, member, 1, no, CouponNumber.Status.receive);// 生成优惠码
                        if (couponCode != null) {
                            return ;
                        } else {
                            return ;
                        }
                    }else {
                        return ;
                    }
                }else {
                    CouponCode couponCode = couponCodeService.build(coupon, member, 1, no, CouponNumber.Status.receive);// 生成优惠码
                    if (couponCode != null) {
                        return ;
                    } else {
                        return ;
                    }
                }
            }else {
                if (couponCodeService.findCouponCodeByCouponAndMember(coupon, member) == null) {
                    List<CouponCode> couponCode = couponCodeService.build(coupon, member, 1);// 生成优惠码
                    if (couponCode != null) {
                        return ;
                    } else {
                        return ;
                    }
                } else {
                    return ;
                }
            }
        }
    }
}
