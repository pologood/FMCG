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

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.wit.*;
import net.wit.constant.Constant;
import net.wit.constant.Constant.Cookies;
import net.wit.entity.*;
import net.wit.service.*;
import net.wit.uic.api.UICService;
import net.wit.util.DESUtil;
import net.wit.util.WebUtils;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * @ClassName: UCTokenInterceptor
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author Administrator
 * @date 2014年6月28日 上午10:35:06
 */
public class UCTokenInterceptor extends HandlerInterceptorAdapter {

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

	@Resource(name = "monthlyServiceImpl")
	MonthlyService monthlyService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		HttpSession session = request.getSession();
		if (session != null && session.getAttribute(Member.PRINCIPAL_ATTRIBUTE_NAME) != null) { // 已经登陆
			//System.out.println("inter:" + session.getId());
			return true;
		}
		String uctoken = WebUtils.getCookie(request, Cookies.UC_TOKEN);
		//System.out.println("inter uctoken:" + uctoken);
		if (StringUtils.isNotBlank(uctoken)) { // 获取到uctoken
			try {
				uctoken = DESUtil.decrypt(uctoken, Constant.generateKey);
				Cart cart = cartService.getCurrent();
				Member member = uicService.tokenLoginTo(uctoken, request, response);
				if (member != null) {
					Principal principal = new Principal(member.getId(), member.getUsername());
					session.setAttribute(Member.PRINCIPAL_ATTRIBUTE_NAME, principal);
					WebUtils.addCookie(request, response, Member.USERNAME_COOKIE_NAME, member.getUsername());
					if (cart != null) {
						if (cart.getMember() == null) {
							cart.getCartItems().iterator();
							cartService.merge(member, cart);
							WebUtils.removeCookie(request, response, Cart.ID_COOKIE_NAME);
							WebUtils.removeCookie(request, response, Cart.KEY_COOKIE_NAME);
						}
					}
				}
				WebUtils.removeCookie(request, response, Cookies.UC_TOKEN);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		String base = request.getContextPath();
		if (request.getMethod().equalsIgnoreCase("GET")) {
			String redirectUrl = request.getQueryString() != null ? request.getRequestURI() + "?" + request.getQueryString() : request.getRequestURI();
			response.sendRedirect(base + loginUrl + "?" + REDIRECT_URL_PARAMETER_NAME + "=" + URLEncoder.encode(redirectUrl, urlEscapingCharset));
		} else {
			response.sendRedirect(base + loginUrl);
		}

		return false;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		if (modelAndView != null) {
			String viewName = modelAndView.getViewName();
			if (!StringUtils.startsWith(viewName, REDIRECT_VIEW_NAME_PREFIX)) {
				Member member = memberService.getCurrent();
				modelAndView.addObject(MEMBER_ATTRIBUTE_NAME, member);
				if(member != null){
					Tenant tenant = member.getTenant();
					HttpSession session=request.getSession();
					session.removeAttribute("hasTenant");
					session.removeAttribute("member_role");
					if (tenant != null) {
						Member owner  = tenant.getMember();
						modelAndView.addObject("owner", owner);
						if(!tenant.getStatus().equals(Tenant.Status.fail) && !tenant.getStatus().equals(Tenant.Status.none)){
							session.setAttribute("hasTenant","true");
						}
						String role="";
//						if(member!=owner){
//							Pageable pageable = new Pageable();
//							List<Filter> filters = new ArrayList<Filter>();
//							filters.add(new Filter("tenant", Filter.Operator.eq, tenant));
//							filters.add(new Filter("member", Filter.Operator.eq, member));
//							pageable.setFilters(filters);
//							Page<Employee> emps = employeeService.findPage(pageable);
//							if (!emps.getContent().isEmpty()) {
//								role=emps.getContent().get(0).getRole();
//							}
//						}else{
							role=",owner";//给旧权限为管理员身份。
//						}
						String isMonthly="false";
						if(monthlyService.isMonthly(member,null)){
							isMonthly="true";
						}
						Map<String,BigDecimal> map =productService.getStockAmount(tenant.getId());
						modelAndView.addObject("StockAmount",map.get("amount"));
						modelAndView.addObject("SuppilerAmount",map.get("SuppilerAmount"));
						session.setAttribute("member_role",role);
						modelAndView.addObject("findMessageList", messageService.findMessageList(owner,false,null, null,null));//系统消息
						modelAndView.addObject("findMsgOrderList", messageService.findMsgOrderList(owner,false,null, null,null));//消息类型(订单、账单)
						modelAndView.addObject("isMonthly", isMonthly);//聚德惠是否月结
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
