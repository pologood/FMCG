package net.wit.interceptor;

import java.net.URLEncoder;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.wit.Principal;
import net.wit.constant.Constant;
import net.wit.constant.Constant.Cookies;
import net.wit.entity.BindUser;
import net.wit.entity.BindUser.Type;
import net.wit.entity.Cart;
import net.wit.entity.Member;
import net.wit.service.BindUserService;
import net.wit.service.CartService;
import net.wit.service.MemberService;
import net.wit.uic.api.UICService;
import net.wit.util.BrowseUtil;
import net.wit.util.DESUtil;
import net.wit.util.WebUtils;
import net.wit.weixin.main.MenuManager;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;

/**
 * @ClassName: WechatMemberInterceptor
 * @Description: 微信会员打开系统URL自动登陆，保证该请求一定会有用户Session
 * @author Administrator
 * @date 2014年8月19日 上午9:46:45
 */
public class WapMemberInterceptor extends UCTokenInterceptor {

	private String loginUrl = "/wap/bound/indexNew.jhtml";

	private String mobileLoginUrl = "/wap/login.jhtml";

	/** "重定向URL"参数名称 */
	private static final String REDIRECT_URL_PARAMETER_NAME = "redirectUrl";

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "bindUserServiceImpl")
	private BindUserService bindUserService;

	@Value("${url_escaping_charset}")
	private String urlEscapingCharset;

	@Resource(name = "uicService")
	private UICService uicService;

	@Resource(name = "cartServiceImpl")
	private CartService cartService;

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		return true;
		/**
		try {
			HttpSession session = request.getSession();
			String header = request.getHeader("User-Agent");
			String browseVersion = BrowseUtil.checkBrowse(header);
				String openId = (String) request.getSession().getAttribute(Member.WEIXIN_OPENT_ID);
				if (openId==null) {
			        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
					String base = bundle.getString("WeiXinSiteUrl");
					if (request.getMethod().equalsIgnoreCase("GET")) {
						String redirectUrl = request.getQueryString() != null ? request.getRequestURI() + "?" + request.getQueryString() : request.getRequestURI();
				        response.sendRedirect(MenuManager.codeUrlO2(base + loginUrl + "?" + REDIRECT_URL_PARAMETER_NAME + "=" + URLEncoder.encode(redirectUrl, urlEscapingCharset) ));
					} else {
					    response.sendRedirect(MenuManager.codeUrlO2(base + loginUrl));
					}
					return false;
				}
				if (session != null && session.getAttribute(Member.PRINCIPAL_ATTRIBUTE_NAME) != null) { // 已经登陆
					return true;
				}
				// 获取会话中微信id
				Member member = memberService.findByUsername(openId);
				if (member != null) {
					Cart cart = cartService.getCurrent();
					Principal principal = new Principal(member.getId(), member.getUsername());
					session.setAttribute(Member.PRINCIPAL_ATTRIBUTE_NAME,principal);
					if (cart != null) {
						if (cart.getMember() == null) {
							cart.getCartItems().iterator();
							cartService.merge(member, cart);
							WebUtils.removeCookie(request, response, Cart.ID_COOKIE_NAME);
							WebUtils.removeCookie(request, response, Cart.KEY_COOKIE_NAME);
						}
					}
					return true;
				} else  {
					String base = request.getContextPath();
					if (request.getMethod().equalsIgnoreCase("GET")) {
						String redirectUrl = request.getQueryString() != null ? request.getRequestURI() + "?" + request.getQueryString() : request.getRequestURI();
				        response.sendRedirect(base + loginUrl + "?" + REDIRECT_URL_PARAMETER_NAME + "=" + URLEncoder.encode(redirectUrl, urlEscapingCharset));
					} else {
					    response.sendRedirect(base + loginUrl);
					}
					return false;
				}  
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
		**/
	}

}