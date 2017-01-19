package net.wit.interceptor;

import java.net.URLEncoder;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;
import net.wit.Principal;
import net.wit.entity.BindUser;
import net.wit.entity.Cart;
import net.wit.entity.Member;
import net.wit.entity.BindUser.Type;
import net.wit.service.AreaService;
import net.wit.service.BindUserService;
import net.wit.service.CartService;
import net.wit.service.MemberRankService;
import net.wit.service.MemberService;
import net.wit.support.PushMessage;
import net.wit.util.BrowseUtil;
import net.wit.util.WebUtils;
import net.wit.weixin.main.MenuManager;
import net.wit.weixin.pojo.AccessToken;
import net.wit.weixin.util.WeixinUtil;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * @ClassName: WechatMemberInterceptor
 * @Description: 寰俊浼氬憳鎵撳紑绯荤粺URL鑷姩鐧婚檰锛屼繚璇佽璇锋眰涓�畾浼氭湁鐢ㄦ埛Session
 * @author Administrator
 * @date 2014骞�鏈�9鏃�涓婂崍9:46:45
 */
public class WeiXinOpenIdInterceptor extends HandlerInterceptorAdapter {

	@Resource(name = "bindUserServiceImpl")
	private BindUserService bindUserService;
	
	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	@Resource(name = "cartServiceImpl")
	private CartService cartService;
	public  String filterEmoji(String source) {
		if(source != null)
		{
			Pattern emoji = Pattern.compile ("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",Pattern.UNICODE_CASE | Pattern . CASE_INSENSITIVE ) ;
			Matcher emojiMatcher = emoji.matcher(source);
			if ( emojiMatcher.find())
			{
				source = emojiMatcher.replaceAll("*");
				return source ;
			}
			return source;
		}
		return source;
	}
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		try {
			HttpSession session = request.getSession();
			String header = request.getHeader("User-Agent");
			String browseVersion = BrowseUtil.checkBrowse(header);
			if (BrowseUtil.WEIXIN.equalsIgnoreCase(browseVersion)) {
				String openId = (String) request.getSession().getAttribute(Member.WEIXIN_OPENT_ID);
				if (openId == null) {
					String code = request.getParameter("code");
					ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
					if (code != null) {
						AccessToken token = WeixinUtil.getOauth2AccessToken(bundle.getString("APPID"), bundle.getString("APPSECRET"), code);
						if (token!=null) {
							openId = token.getOpenid();
							request.getSession().setAttribute(Member.WEIXIN_OPENT_ID, openId);
						} else {
							String redirectUrl = request.getQueryString() != null ? request.getRequestURI() + "?" + request.getQueryString() : request.getRequestURI();

							redirectUrl = URLEncoder.encode(bundle.getString("WeiXinSiteUrl") + redirectUrl);
							response.sendRedirect(MenuManager.codeUrlO2(redirectUrl));
							return false;
						}
						
					} else {
						String redirectUrl = request.getQueryString() != null ? request.getRequestURI() + "?" + request.getQueryString() : request.getRequestURI();

						redirectUrl = URLEncoder.encode(bundle.getString("WeiXinSiteUrl") + redirectUrl);
				        response.sendRedirect(MenuManager.codeUrlO2(redirectUrl));
						return false;
					}
				} 
				if (session != null && (session.getAttribute(Member.PRINCIPAL_ATTRIBUTE_NAME) != null)) { // 已经登陆
					return true;
				}
				if (openId!=null) {
					// 获取会话中微信id
					String extension = request.getParameter("extension");

					//System.out.println("open id ext = "+extension);
					if (extension==null) {
						extension =(String)	request.getSession().getAttribute(Member.EXTENSION_ATTRIBUTE_NAME);
						//System.out.println("open id 1 ext = "+extension);
					}
					Member extMember=null;
					if(extension!=null){
						extMember = memberService.findByUsername(extension);
					}

					Member member = null;
					BindUser bindUser = bindUserService.findByUsername(openId, Type._wx);
					if (bindUser==null) {
			            member = memberService.createAndBind(openId, areaService.getCurrent(), extMember, request.getRemoteAddr());
					} else {
						member = bindUser.getMember();
					}

					if (member != null) {
			            //if (member.getJmessage() == null || !member.getJmessage()) {
			            //    if (PushMessage.jpush_register(member.getUsername(), "rzico@2015")) {
			            //        member.setJmessage(true);
			            //    }
			            //}
			            if (member.getEmessage() == null || !member.getEmessage()) {
			                if (PushMessage.ease_register(member.getId().toString(), "rzico@2015", member.getDisplayName())) {
			                    member.setEmessage(true);
			                }
			            }
			            memberService.update(member);
						Cart cart = cartService.getCurrent();
						Principal principal = new Principal(member.getId(), member.getUsername());
						session.setAttribute(Member.PRINCIPAL_ATTRIBUTE_NAME, principal);
						if (cart != null) {
							if (cart.getMember() == null) {
								cart.getCartItems().iterator();
								cartService.merge(member, cart);
								WebUtils.removeCookie(request, response, Cart.ID_COOKIE_NAME);
								WebUtils.removeCookie(request, response, Cart.KEY_COOKIE_NAME);
							}
						}

					    if (member.getHeadImg()==null) {
					       ResourceBundle bundle=PropertyResourceBundle.getBundle("config");
						   AccessToken token = WeixinUtil.getAccessToken(bundle.getString("APPID"), bundle.getString("APPSECRET"));
						   JSONObject userinfo = WeixinUtil.getUserInfo(token.getToken(), openId);
						   if (userinfo.containsKey("nickname")) {
							  // System.out.println("nickName"+userinfo.getString("nickname"));
							   member.setNickName(filterEmoji(userinfo.getString("nickname")));
							   member.setHeadImg(userinfo.getString("headimgurl"));
							   //if (member.getName()==null) {
								//   member.setName(member.getNickName());
							   //}
							   memberService.update(member);
						   }
						}

						return true;
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			return true;
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		if (modelAndView != null) {
			String browseVersion = WebUtils.getCookie(request, BrowseUtil.BROWSE_VERSION);
			// 判断是否微信浏览器
			if (StringUtils.isBlank(browseVersion)) {
				String header = request.getHeader("User-Agent");
				browseVersion = BrowseUtil.checkBrowse(header);
				WebUtils.addCookie(request, response, BrowseUtil.BROWSE_VERSION, browseVersion);
			} else {
				String checkBrowse = BrowseUtil.checkBrowse(request.getHeader("User-Agent"));
				if (!browseVersion.equals(checkBrowse)) {
					WebUtils.addCookie(request, response, BrowseUtil.BROWSE_VERSION, checkBrowse);
				}
			}
			// System.out.println("session=" + WebUtils.getCookie(request, BrowseUtil.BROWSE_VERSION));
			modelAndView.addObject(BrowseUtil.BROWSE_VERSION, browseVersion);
		}
	}
}