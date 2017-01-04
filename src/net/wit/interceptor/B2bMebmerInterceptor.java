/**
 *====================================================
 * 文件名称: UCTokenInterceptor.java
 * 修订记录：
 * No    日期				作者(操作:具体内容)
 * 1.    2014年6月28日			Administrator(创建:创建文件)
 *====================================================
 * 类描述：(说明未实现或其它不应生成javadoc的内容)
 * 
 */
package net.wit.interceptor;

import net.wit.Principal;
import net.wit.constant.Constant;
import net.wit.constant.Constant.Cookies;
import net.wit.entity.Cart;
import net.wit.entity.Member;
import net.wit.entity.Tenant;
import net.wit.service.*;
import net.wit.uic.api.UICService;
import net.wit.util.DESUtil;
import net.wit.util.WebUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Map;

/**
 * @ClassName: UCTokenInterceptor
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author Administrator
 * @date 2014年6月28日 上午10:35:06
 */
public class B2bMebmerInterceptor extends HandlerInterceptorAdapter {

	/** 重定向视图名称前缀 */
	private static final String REDIRECT_VIEW_NAME_PREFIX = "redirect:";

	/** "重定向URL"参数名称 */
	private static final String REDIRECT_URL_PARAMETER_NAME = "redirectUrl";

	/** "会员"属性名称 */
	private static final String MEMBER_ATTRIBUTE_NAME = "member";

	/** 默认登录URL */
	private static final String DEFAULT_LOGIN_URL = "/login.jhtml";

	/** 登录URL */
	private String loginUrl = DEFAULT_LOGIN_URL;

	@Value("${url_escaping_charset}")
	private String urlEscapingCharset;

	@Resource(name = "uicService")
	private UICService uicService;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "cartServiceImpl")
	private CartService cartService;

	@Resource(name = "employeeServiceImpl")
	private EmployeeService employeeService;

	@Resource(name = "productServiceImpl")
	private ProductService productService;

	@Resource(name = "messageServiceImpl")
	MessageService messageService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		HttpSession session = request.getSession();
		Principal principal = (Principal) session.getAttribute(Member.PRINCIPAL_ATTRIBUTE_NAME);

		return true;

	}

	/**
	 * 拦截b2b下（非b2b/member/supplier/下）非零售商清空会话中的会员
	 * @param request
	 * @param response
	 * @param handler
	 * @param modelAndView
	 * @throws Exception
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		if (modelAndView != null) {
			String viewName = modelAndView.getViewName();
			HttpSession session = request.getSession();
			if (!StringUtils.startsWith(viewName, REDIRECT_VIEW_NAME_PREFIX)) {
				Member member = memberService.getCurrent();
				modelAndView.addObject(MEMBER_ATTRIBUTE_NAME, member);
				if(member != null){
					Tenant tenant = member.getTenant();
					if (tenant != null) {
						if(tenant.getTenantType()!= Tenant.TenantType.retailer){
							session.removeAttribute(Member.PRINCIPAL_ATTRIBUTE_NAME);
							WebUtils.removeCookie(request, response, Member.USERNAME_COOKIE_NAME);
							WebUtils.removeCookie(request, response, Constant.Cookies.UC_TOKEN);
						}
					}
				}
			}

		}
	}

	public String getLoginUrl() {
		return loginUrl;
	}

	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}

}
