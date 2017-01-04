package net.wit.controller.wap;

import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.wit.Message;
import net.wit.entity.Area;
import net.wit.entity.BindUser;
import net.wit.entity.BindUser.Type;
import net.wit.entity.Cart;
import net.wit.entity.Member;
import net.wit.entity.Member.RegType;
import net.wit.service.AreaService;
import net.wit.service.BindUserService;
import net.wit.service.CartService;
import net.wit.service.MemberRankService;
import net.wit.service.MemberService;
import net.wit.service.RSAService;
import net.wit.service.SmsSendService;
import net.wit.uic.api.UICService;
import net.wit.util.BrowseUtil;
import net.wit.util.WebUtils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller - 账号注册
 * @author rsico Team
 * @version 3.0
 */
@Controller("wapRegisterController")
@RequestMapping("/wap/register")
public class RegisterController extends BaseController {

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "memberRankServiceImpl")
	private MemberRankService memberRankService;

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	@Resource(name = "rsaServiceImpl")
	private RSAService rsaService;

	@Resource(name = "bindUserServiceImpl")
	private BindUserService bindUserService;

	@Resource(name = "smsSendServiceImpl")
	private SmsSendService smsSendService;

	@Resource(name = "uicService")
	private UICService uicService;

	@Resource(name = "cartServiceImpl")
	private CartService cartService;

	/**
	 * 账号注册页面
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(ModelMap model) {
		Area area = areaService.getCurrent();
		model.addAttribute("area", area);
		return "wap/register/index";
	}

	/**
	 * 注册提交(手机/邮箱)
	 */
	@RequestMapping(value = "/submit", method = RequestMethod.POST)
	public @ResponseBody Message submit(String username, String code, Long areaId, String password, String enPassword, HttpServletRequest request, HttpServletResponse response, HttpSession session, RedirectAttributes redirectAttributes) {
		//username为手机
		if (StringUtils.isEmpty(password)) {
			password = rsaService.decryptParameter("enPassword", request);
		}
		rsaService.removePrivateKey(request);
		ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
		if (areaId == null) {
			areaId = Long.valueOf(bundle.getString("localArea"));
		}
		Cart cart = cartService.getCurrent();
		Message msg = uicService.register(username, password, code, areaId, RegType.mobile, request, response);
		if (msg.getType().equals(Message.Type.success)) {
			String openId = (String) request.getSession().getAttribute(Member.WEIXIN_OPENT_ID);
			Member member = memberService.findByUsername(username);
			if (openId != null ) {
				BindUser user = bindUserService.findByUsername(openId, Type._wx);

				if(user==null){
					user = new BindUser();
					user.setUsername(openId);
					user.setPassword(password);
					user.setMember(member);
					user.setType(Type._wx);
					bindUserService.save(user);
				}else {
					user.setUsername(openId);
					user.setPassword(password);
					user.setMember(member);
					user.setType(Type._wx);
					bindUserService.update(user);
				}
			}
			if (cart != null) {
				if (cart.getMember() == null) {
					cart.getCartItems().iterator();
					cartService.merge(member, cart);
					WebUtils.removeCookie(request, response, Cart.ID_COOKIE_NAME);
					WebUtils.removeCookie(request, response, Cart.KEY_COOKIE_NAME);
				}
			}

			WebUtils.addCookie(request, response, Member.USERNAME_COOKIE_NAME, member.getUsername());
			return Message.success("注册成功");
		} else {
			return msg;
		}

	}

	/**
	 * 检查用户是否存在
	 */
	@RequestMapping(value = "/check_username", method = RequestMethod.GET)
	public @ResponseBody boolean checkUsername(String username) {
		return memberService.usernameExists(username);
	}

	/**
	 * 获取验证码
	 */
	@RequestMapping(value = "/getCheckCode", method = RequestMethod.POST)
	@ResponseBody
	public Message getCheckCode(String mobile, HttpServletRequest request) {
		String header = request.getHeader("User-Agent");
        if (!BrowseUtil.isWeixin(header)) {
        	return Message.error("安全检测不合法");
        }
		//return uicService.sendAuthcodeByTel(mobile, null, request);
		return Message.error("安全检测不合法");
	}

	/**
	 * 选择区域
	 */
	@RequestMapping(value = "/areaSelect", method = RequestMethod.POST)
	public String areaSelect(String openid, String areaId, HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model) {

		Area area = null;
		if (areaId != null) {
			area = areaService.find(Long.valueOf(areaId));
			model.addAttribute("cityList", area.getParent().getParent().getChildren());
			model.addAttribute("districtList", area.getParent().getChildren());
		}
		List<Area> areaList = areaService.findRoots();

		String modulus = request.getParameter("modulus");
		String exponent = request.getParameter("exponent");
		String jsessionid = request.getParameter("jsessionid");
		String password = request.getParameter("password");
		String rePassword = request.getParameter("rePassword");
		String mobile = request.getParameter("mobile");
		String checkCode = request.getParameter("checkCode");

		model.addAttribute("modulus", modulus);
		model.addAttribute("exponent", exponent);
		model.addAttribute("jsessionid", jsessionid);
		model.addAttribute("password", password);
		model.addAttribute("rePassword", rePassword);
		model.addAttribute("area", area);
		model.addAttribute("mobile", mobile);
		model.addAttribute("checkCode", checkCode);
		model.addAttribute("openid", openid);
		model.addAttribute("areaList", areaList);
		return "wap/register/area_select";
	}

	/**
	 * 地区
	 */
	@RequestMapping(value = "/getArea", method = RequestMethod.GET)
	public @ResponseBody List<Area> getArea(Long areaId) {
		List<Area> areas = null;
		Area parent = areaService.find(areaId);
		if (parent != null) {
			areas = new ArrayList<Area>();
			areas = new ArrayList<Area>(parent.getChildren());
		}
		return areas;
	}

	/**
	 * 账号注册页面
	 */
	@RequestMapping(value = "/go_index", method = RequestMethod.POST)
	public String goIndex(String modulus, String exponent, String jsessionid, String password, String rePassword, String areaId, String mobile, String checkCode, String openid, HttpServletRequest request, HttpServletResponse response, HttpSession session,
			ModelMap model) {
		Area area = null;
		if (areaId != null) {
			area = areaService.find(Long.valueOf(areaId));
			model.addAttribute("area", area);
			model.addAttribute("modulus", modulus);
			model.addAttribute("exponent", exponent);
			model.addAttribute("jsessionid", jsessionid);
			model.addAttribute("password", password);
			model.addAttribute("rePassword", rePassword);
			model.addAttribute("mobile", mobile);
			model.addAttribute("checkCode", checkCode);
			model.addAttribute("openid", openid);
		} else {
			RSAPublicKey publicKey = rsaService.generateKey(request);
			model.addAttribute("modulus", Base64.encodeBase64String(publicKey.getModulus().toByteArray()));
			model.addAttribute("exponent", Base64.encodeBase64String(publicKey.getPublicExponent().toByteArray()));
			model.addAttribute("jsessionid", "");
		}
		return "wap/register/index";
	}
}
