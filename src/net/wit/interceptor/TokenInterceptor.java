/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.interceptor;

import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import net.wit.Principal;
import net.wit.entity.BindUser;
import net.wit.entity.BindUser.Type;
import net.wit.entity.Cart;
import net.wit.entity.Member;
import net.wit.service.BindUserService;
import net.wit.service.CartService;
import net.wit.util.MD5Utils;
import net.wit.util.WebUtils;

/**
 * Interceptor - 令牌
 * 
 * @author rsico Team
 * @version 3.0
 */
public class TokenInterceptor extends HandlerInterceptorAdapter {

	@Resource(name = "cartServiceImpl")
	private CartService cartService;
	@Resource(name = "bindUserServiceImpl")
	private BindUserService bindUserService;
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//		response.addHeader("Access-Control-Allow-Origin", "*");
		String extension = request.getParameter("extension");
		if (extension!=null) {
			request.getSession().setAttribute(Member.EXTENSION_ATTRIBUTE_NAME, extension);
		}
		
		HttpSession session = request.getSession();
		if (session != null && session.getAttribute(Member.PRINCIPAL_ATTRIBUTE_NAME) != null) { // 已经登陆
			return true;
		}
		
		//if (session==null) {
	        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
	       
			String xuid = request.getHeader("x-uid");
			//System.out.println("xuid="+xuid);
			String xapp = request.getHeader("x-app");
			String xkey = request.getHeader("x-key");
			if (xkey!=null && xapp!=null && xuid!=null && xkey.equals(MD5Utils.getMD5Str(xuid+xapp+bundle.getString("appKey")))) {
				BindUser bindUser = bindUserService.findByUsername(xuid, Type._mac);
				if (bindUser!=null) {
					Cart cart = cartService.getCurrent();
					Principal principal = new Principal(bindUser.getMember().getId(), bindUser.getMember().getUsername());
					session.setAttribute(Member.PRINCIPAL_ATTRIBUTE_NAME, principal);
					if (cart != null) {
						if (cart.getMember() == null) {
							cart.getCartItems().iterator();
							cartService.merge(bindUser.getMember(), cart);
							WebUtils.removeCookie(request, response, Cart.ID_COOKIE_NAME);
							WebUtils.removeCookie(request, response, Cart.KEY_COOKIE_NAME);
						}
					}
				}
			}
		//}
		
		return true;
	}

}