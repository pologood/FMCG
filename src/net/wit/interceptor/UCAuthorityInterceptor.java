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

import net.wit.*;
import net.wit.constant.Constant;
import net.wit.constant.Constant.Cookies;
import net.wit.entity.*;
import net.wit.service.*;
import net.wit.template.directive.FlashMessageDirective;
import net.wit.uic.api.UICService;
import net.wit.util.DESUtil;
import net.wit.util.WebUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: UCAuthorityInterceptor
 * @Description:用于权限拦截
 * @author Administrator
 * @date 2016年8月1日 上午10:35:06
 */
public class UCAuthorityInterceptor implements HandlerInterceptor {

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "cartServiceImpl")
	private CartService cartService;

	@Resource(name = "employeeServiceImpl")
	private EmployeeService employeeService;

	@Resource(name = "tenantRulesRoleServiceImpl")
	private TenantRulesRoleService tenantRulesRoleService;

	@Override
	public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
//		RedirectAttributes redirectAttributes
		///app/member/**
		///helper/member/**
		///b2c/member/**
//		String name=o.();
		HandlerMethod handlerMethod=(HandlerMethod)o;
		String methodName= handlerMethod.getMethod().getName();
		HttpSession session = httpServletRequest.getSession();
		String redirectUrl = httpServletRequest.getQueryString() != null ? httpServletRequest.getRequestURI() + "?" + httpServletRequest.getQueryString() : httpServletRequest.getRequestURI();
		if (session != null && session.getAttribute(Member.PRINCIPAL_ATTRIBUTE_NAME) != null) { // 已经登陆
			String[]heads={"helper","app"};

			boolean authority = false;

			for (String item:heads)
			{
				int requestUrlIndex = httpServletRequest.getRequestURI().indexOf(item);
				if (requestUrlIndex > 0) {
					//截取url后面部分
					String requestUrl = httpServletRequest.getRequestURI().substring(requestUrlIndex);
					if (requestUrl.equals("helper/member/index.jhtml")) {
						return true;
					}
//					if (requestUrl.equals("b2c/member/order/list.jhtml")) {
//						return true;
//					}


					authority= Authority( httpServletRequest, requestUrl,methodName);
					break;
				}
			}

			String base = httpServletRequest.getContextPath();
			//httpServletResponse.sendRedirect(base+ "您没有" + "权限!");

			if (!authority){
			httpServletRequest.setAttribute(FlashMessageDirective.FLASH_MESSAGE_ATTRIBUTE_NAME,net.wit.Message.error("Helper.message.noAuthority"));
				httpServletRequest.getRequestDispatcher("/helper/member/index.jhtml").forward(httpServletRequest, httpServletResponse);
			}
			return authority;
		}
		return true;
	}

	private boolean  Authority(HttpServletRequest httpServletRequest,String requestUrl,String methodName){
		Member member = memberService.getCurrent();
		if (member != null) {
			Tenant tenant = member.getTenant();
			if (tenant != null) {
				Member owner = tenant.getMember();
				String role = "";
				if (member != owner) {
					Pageable pageable = new Pageable();
					List<Filter> filters = new ArrayList<Filter>();
					filters.add(new Filter("tenant", Filter.Operator.eq, tenant));
					filters.add(new Filter("member", Filter.Operator.eq, member));
					pageable.setFilters(filters);
					Page<Employee> emps = employeeService.findPage(pageable);
					if (!emps.getContent().isEmpty()) {
						role = emps.getContent().get(0).getRole();
						String[] roleIds = role.split(Employee.RoleSplit);
						for (String id : roleIds) {
							if(!"".equals(id)){
								try {
									List<TenantRulesRole> tenantRulesRoles = tenantRulesRoleService.findByRoleId(Long.valueOf(id), requestUrl);
									if (tenantRulesRoles.size() > 0) {
										return true;
									}
								} catch (Exception e) {
									continue;
								}
							}

						}
					}
				} else {
					//店长-默认拥有所有权限
					return true;
				}
			}

		}
		return  false;
	}
	@Override
	public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

	}
}
