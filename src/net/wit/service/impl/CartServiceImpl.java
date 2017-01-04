/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.service.impl;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.LockModeType;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.wit.Principal;
import net.wit.dao.ActivityPlanningDao;
import net.wit.dao.CartDao;
import net.wit.dao.CartItemDao;
import net.wit.dao.CouponCodeDao;
import net.wit.dao.MemberDao;
import net.wit.entity.*;
import net.wit.service.CartService;
import net.wit.util.WebUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * Service - 购物车
 *
 * @author rsico Team
 * @version 3.0
 */
@Service("cartServiceImpl")
public class CartServiceImpl extends BaseServiceImpl<Cart, Long> implements CartService {

    private static final boolean CouponCode = false;

    @Resource(name = "cartDaoImpl")
    private CartDao cartDao;

    @Resource(name = "cartItemDaoImpl")
    private CartItemDao cartItemDao;

    @Resource(name = "memberDaoImpl")
    private MemberDao memberDao;

    @Resource(name = "couponCodeDaoImpl")
    private CouponCodeDao couponCodeDao;

    @Resource(name = "activityPlanningDaoImpl")
    private ActivityPlanningDao activityPlanningDao;

    @Resource(name = "cartDaoImpl")
    public void setBaseDao(CartDao cartDao) {
        super.setBaseDao(cartDao);
    }

    public Cart getCurrent() {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        if (requestAttributes != null) {
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            Principal principal = (Principal) request.getSession().getAttribute(Member.PRINCIPAL_ATTRIBUTE_NAME);
            Member member = principal != null ? memberDao.find(principal.getId()) : null;
            if (member != null) {
                Cart cart = member.getCart();
                if (cart != null) {
                    if (!cart.hasExpired()) {
                        if (!DateUtils.isSameDay(cart.getModifyDate(), new Date())) {
                            cart.setModifyDate(new Date());
                            cartDao.merge(cart);
                        }
                        return cart;
                    } else {
                        cartDao.remove(cart);
                    }
                }
            } else {
                String id = WebUtils.getCookie(request, Cart.ID_COOKIE_NAME);
                String key = WebUtils.getCookie(request, Cart.KEY_COOKIE_NAME);
                if (StringUtils.isNotEmpty(id) && StringUtils.isNumeric(id) && StringUtils.isNotEmpty(key)) {
                    Cart cart = cartDao.find(Long.valueOf(id));
                    if (cart != null && cart.getMember() == null && StringUtils.equals(cart.getKey(), key)) {
                        if (!cart.hasExpired()) {
                            if (!DateUtils.isSameDay(cart.getModifyDate(), new Date())) {
                                cart.setModifyDate(new Date());
                                cartDao.merge(cart);
                            }
                            return cart;
                        } else {
                            cartDao.remove(cart);
                        }
                    }
                }
            }
        }
        return null;
    }

    @Transactional(readOnly = false)
    public void merge(Member member, Cart cart) {
        if (member != null && cart != null && cart.getMember() == null) {
            Cart memberCart = member.getCart();
            if (memberCart != null) {
                for (Iterator<CartItem> iterator = memberCart.getCartItems().iterator(); iterator.hasNext(); ) {
                    CartItem cartItem = iterator.next();
                    cartItem.setSelected(false);
                    cartItemDao.merge(cartItem);
                }
                for (Iterator<CartItem> iterator = cart.getCartItems().iterator(); iterator.hasNext(); ) {
                    CartItem cartItem = iterator.next();
                    Product product = cartItem.getProduct();
                    if (memberCart.contains(product)) {
                        if (Cart.MAX_PRODUCT_COUNT != null && memberCart.getCartItems().size() > Cart.MAX_PRODUCT_COUNT) {
                            continue;
                        }
                        CartItem item = memberCart.getCartItem(product);
                        item.setSelected(cartItem.getSelected());
                        item.setQuantity(cartItem.getQuantity());
                        cartItemDao.merge(item);
                    } else {
                        if (Cart.MAX_PRODUCT_COUNT != null && memberCart.getCartItems().size() >= Cart.MAX_PRODUCT_COUNT) {
                            continue;
                        }
                        iterator.remove();
                        cartItem.setCart(memberCart);
                        memberCart.getCartItems().add(cartItem);
                        cartItemDao.merge(cartItem);
                    }
                }
                cartDao.remove(cart);
            } else {
                member.setCart(cart);
                cart.setMember(member);
                cartDao.merge(cart);
            }
        }
    }

    public void evictExpired() {
        List<Cart> carts = cartDao.findExpired();
        for (Cart cart : carts) {
            if (cart.getCouponCode() != null) {
                if (!cart.getCouponCode().getIsUsed()) {
                    cart.getCouponCode().setMember(null);
                    couponCodeDao.merge(cart.getCouponCode());
                }
            }
        }
        cartDao.evictExpired();
    }

    public void clearActivity() {
        Cart cart = getCurrent();
        if (cart.getCouponCode() != null) {
            Boolean tenanted = false;
            if (cart.getCouponCode().getTenant() != null) {
                for (Tenant tenant : cart.getTenants()) {
                    if (cart.getCouponCode().getTenant().equals(tenant) && cart.getCouponCode().getCoupon().getMinimumPrice().compareTo(cart.getEffectivePrice(tenant)) <= 0) {
                        tenanted = true;
                    }
                }
            }

            if (cart.getCouponCode().getIsUsed() || !cart.getCouponCode().isLocked(cart.getPrice()) || cart.getCouponCode().getTenant() == null || !tenanted) {
                cart.getCouponCode().setMember(null);
                couponCodeDao.merge(cart.getCouponCode());
                cart.setCouponCode(null);
                cartDao.merge(cart);
            }
        }
    }

    public void bindActivity() {
        clearActivity();
        Cart cart = getCurrent();
        if (cart.getCouponCode() == null) {
            for (Tenant tenant : cart.getTenants()) {
                ActivityPlanning activityPlanning = activityPlanningDao.getCurrent(tenant, ActivityPlanning.Type.random);
                if (activityPlanning != null) {
                    CouponCode couponCode = activityPlanningDao.lockCoupon(activityPlanning, cart.getEffectivePrice(tenant));
                    if (couponCode != null && couponCode.getCoupon().getType().equals(Coupon.Type.coupon)) {
                        couponCode.locked(cart.getMember());
                        couponCode.setTenant(tenant);
                        couponCodeDao.merge(couponCode);
                        cart.setCouponCode(couponCode);
                    }
                }
            }
            cartDao.merge(cart);
        }
    }

    public void updateKey(Cart cart,String token_key){
        cartDao.updateKey(cart,token_key);
    }

}