package net.wit.uic.api;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.wit.Message;
import net.wit.Principal;
import net.wit.Setting;
import net.wit.constant.Constant;
import net.wit.constant.Constant.Cookies;
import net.wit.dao.MemberRankDao;
import net.wit.entity.Member;
import net.wit.helper.HttpClientHelper;
import net.wit.service.AreaService;
import net.wit.service.MemberService;
import net.wit.support.EntitySupport;
import net.wit.support.PushMessage;
import net.wit.util.Base64Util;
import net.wit.util.DESUtil;
import net.wit.util.JsonUtils;
import net.wit.util.SettingUtils;
import net.wit.util.WebUtils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.HttpClient;
import org.jsoup.helper.StringUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service("uicService")
public class UICService {
	@Value("${vbox.ucServer}")
	private String ucServer;
	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	@Resource(name = "areaServiceImpl")
	private AreaService areaService;
	@Resource(name = "memberRankDaoImpl")
	private MemberRankDao memberRankDao;
	
	public String getCaptcha(HttpServletRequest request) {
		HttpClient ucClient = HttpClientHelper.getUCHttpClient(request);
		try {
		// 先获取登录校验码
			String url = UICConfig.UC.getAuthUrl;
       		String auth = HttpClientHelper.get(ucClient, url, null);
       		return auth;
		} catch(Exception e) {
		   e.printStackTrace();
		    return "";
		}
		
	}
	
	public Message autoLoginTo() {
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		if (requestAttributes != null) {
			HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
			Principal principal = (Principal) request.getSession().getAttribute(Member.PRINCIPAL_ATTRIBUTE_NAME);
			principal.createSign();
			String token = JsonUtils.toJson(principal);
			HttpClient httpClient = HttpClientHelper.getUCHttpClient(null);
			try {
				Map<String, String> map = new HashMap<String, String>();
				map.put("token", token);
				String url = UICConfig.UC.tokenLogin;
				String json_result = HttpClientHelper.get(httpClient, url, map) ;

				JSONObject jsonObject = JSONObject.fromObject(json_result);

				String type = jsonObject.getString("type");
				if ("success".equals(type)) {
					return Message.success(jsonObject.getString("content"));
				} else {
					// 获取验证码失败
					return Message.error(jsonObject.getString("content"));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return Message.error("登录失败");
	}
	
	public Member tokenLoginTo(String uctoken, HttpServletRequest request, HttpServletResponse response) {
		try {
			HttpClient ucClient = HttpClientHelper.getUCHttpClient(request);
			String url = UICConfig.UC.getUserUrl;
			Map<String, String> params = new HashMap<String, String>();
			params.put("token", uctoken);
			String responseText = HttpClientHelper.get(ucClient, url, params);
			JSONObject jsonObject = JSONObject.fromObject(responseText);
			if (StringUtil.isBlank(jsonObject.getString("username"))) {
				throw new java.lang.IllegalArgumentException("用户认证失败!");
			}
			// 判断本地有无该账号，无账号则同步账号到本地
			Member member = memberService.findByUsername(jsonObject.getString("username"));
			if (member == null) {
				member = EntitySupport.createInitMember();
				member.setUsername(jsonObject.getString("username"));
				if (jsonObject.containsKey("name")) {
					member.setName(jsonObject.getString("name"));
				}
				if (jsonObject.containsKey("mobile")) {
					member.setMobile(jsonObject.getString("mobile"));
				}
				member.setEmail(jsonObject.getString("email"));
				member.setMemberRank(memberRankDao.findDefault());
				memberService.save(member);
			} else {
				member.setUsername(jsonObject.getString("username"));
				if (jsonObject.containsKey("name")) {
					member.setName(jsonObject.getString("name"));
				}
				member.setEmail(jsonObject.getString("email"));
				if (jsonObject.containsKey("mobile")) {
					member.setMobile(jsonObject.getString("mobile"));
				}
				memberService.save(member);
			}
			Setting setting = SettingUtils.get();
			WebUtils.addCookie(request, response, UICConfig.Cookies.HOST, setting.getSiteUrl(), -1, "/", setting.getCookieDomain(), null);
			WebUtils.addCookie(request, response, Member.USERNAME_COOKIE_NAME, member.getUsername(), -1, "/", setting.getCookieDomain(), null);
			Principal principal = new Principal(member.getId(), member.getUsername());
			request.getSession().setAttribute(Member.PRINCIPAL_ATTRIBUTE_NAME, principal);
			principal.createSign();
			String uctoken_encrypt = DESUtil.encrypt(JsonUtils.toJson(principal), UICConfig.generateKey);
			WebUtils.addCookie(request, response, UICConfig.Cookies.UC_TOKEN, uctoken_encrypt, -1, "/", setting.getCookieDomain(), null);
			return member;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Member createMember(HttpClient ucClient) {
		try {
			String url = UICConfig.UC.getUserInfoUrl;
			String responseText = HttpClientHelper.get(ucClient, url, null);
			JSONObject jsonObject = JSONObject.fromObject(responseText);
			if (StringUtil.isBlank(jsonObject.getString("username"))) {
				throw new java.lang.IllegalArgumentException("会话无效!");
			}
			// 判断本地有无该账号，无账号则同步账号到本地
			Member member = memberService.findByUsername(jsonObject.getString("username"));
			if (member == null) {
				member = EntitySupport.createInitMember();
				member.setUsername(jsonObject.getString("username"));
				if (jsonObject.containsKey("name")) {
					member.setName(jsonObject.getString("name"));
				}
				if (jsonObject.containsKey("mobile")) {
					member.setMobile(jsonObject.getString("mobile"));
				}
				member.setEmail(jsonObject.getString("email"));
				member.setMemberRank(memberRankDao.findDefault());
				
				JSONObject area = jsonObject.getJSONObject("area");
			    member.setArea(areaService.find(area.getLong("id")));
			} else {
				member.setUsername(jsonObject.getString("username"));
				if (jsonObject.containsKey("name")) {
					member.setName(jsonObject.getString("name"));
				}
				member.setEmail(jsonObject.getString("email"));
				if (jsonObject.containsKey("mobile")) {
					member.setMobile(jsonObject.getString("mobile"));
				}
				JSONObject area = jsonObject.getJSONObject("area");
			    member.setArea(areaService.find(area.getLong("id")));
			}
			if (member.getJmessage()==null || !member.getJmessage()) {
				if (PushMessage.jpush_register(member.getUsername(), "rzico@2015")) {
					   member.setJmessage(true);
					}
			}
			if (member.getEmessage() == null || !member.getEmessage()) {
				if (PushMessage.ease_register(member.getId().toString(), "rzico@2015", member.getDisplayName())) {
					member.setEmessage(true);
				}
			}
			memberService.save(member);
			return member;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Message loginTo(String username, String password, HttpServletRequest request, HttpServletResponse response) {
		HttpClient ucClient = HttpClientHelper.getUCHttpClient(request);
		try {
			String url = null;
			String enPassword = null;
			if (password!=null) {
			// 先获取登录校验码
			url = UICConfig.UC.getAuthUrl;
			String auth = HttpClientHelper.get(ucClient, url, null);
			enPassword = DigestUtils.md5Hex(DigestUtils.md5Hex(password) + auth + "vst@2014-2020$$");
			} else {
				enPassword = (String) request.getParameter("enPassword");
			}
			// UC登录
			url = UICConfig.UC.loginUrl;
			Map<String, String> params = new HashMap<String, String>();
			params.put("username", username);
			params.put("password", enPassword);
			params.put("openid", (String)request.getSession().getAttribute(Member.WEIXIN_OPENT_ID));
			String responseText = HttpClientHelper.get(ucClient, url, params);
			JSONObject jsonObject = JSONObject.fromObject(responseText);
			String type = jsonObject.getString("type");
			String content = jsonObject.getString("content");
			if ("success".equals(type)) {
				try {
					Member member = createMember(ucClient);
					Setting setting = SettingUtils.get();
					WebUtils.addCookie(request, response, UICConfig.Cookies.HOST, setting.getSiteUrl(), -1, "/", setting.getCookieDomain(), null);
					WebUtils.addCookie(request, response, Member.USERNAME_COOKIE_NAME, member.getUsername(), -1, "/", setting.getCookieDomain(), null);
					Principal principal = new Principal(member.getId(), member.getUsername());
					request.getSession().setAttribute(Member.PRINCIPAL_ATTRIBUTE_NAME, principal);
					principal.createSign();
					String uctoken_encrypt = DESUtil.encrypt(JsonUtils.toJson(principal), UICConfig.generateKey);
					WebUtils.addCookie(request, response, UICConfig.Cookies.UC_TOKEN, uctoken_encrypt, -1, "/", setting.getCookieDomain(), null);
				} catch (Exception e) {
					e.printStackTrace();
					return Message.error(content);
				}
				return Message.success(content);
			} else { // 登录失败则返回失败原因
				if("warn".equals(type)){
					return Message.warn(content);
				}
				return Message.error(content);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Message.error("未知错误");
	}
	
	
	public Message sendAuthcodeByTel(String tel,String username, HttpServletRequest request) {
		HttpClient httpClient = HttpClientHelper.getUCHttpClient(null);
		try {
			Map<String, String> map = new HashMap<String, String>();
			map.put("mobile", tel);
			map.put("username", username);

			// 这里URL不要部署成有项目名的
			String url = UICConfig.UC.sendMobileUrl;


			String json_result = HttpClientHelper.post(httpClient, url, map) ;

			JSONObject jsonObject = JSONObject.fromObject(json_result);

			String type = jsonObject.getString("type");
			if ("success".equals(type)) {
				return Message.success(jsonObject.getString("content"));
			} else {
				// 获取验证码失败
				return Message.error(jsonObject.getString("content"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	return Message.error("末知错误");
}
	
/**
 * 检查验证码正确
 * @param tel
 * @return
 */
public Message checkCaptcha(String captcha, HttpServletRequest request) {
		HttpClient httpClient = HttpClientHelper.getUCHttpClient(null);
		try {
			Map<String, String> map = new HashMap<String, String>();
			map.put("captcha", captcha);
			// 这里URL不要部署成有项目名的
			String url = UICConfig.UC.checkCaptchaUrl;


			String json_result = HttpClientHelper.post(httpClient, url, map) ;

			JSONObject jsonObject = JSONObject.fromObject(json_result);

			String type = jsonObject.getString("type");
			if ("success".equals(type)) {
				return Message.success(jsonObject.getString("content"));
			} else {
				// 获取验证码失败
				return Message.error(jsonObject.getString("content"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	return Message.error("验证码无效");
}

public Message sendAuthcodeByMail(String email,String username, HttpServletRequest request) {
		HttpClient httpClient = HttpClientHelper.getUCHttpClient(null);
		try {
			Map<String, String> map = new HashMap<String, String>();
			map.put("email", email);
			map.put("username", username);

			// 这里URL不要部署成有项目名的
			String url = UICConfig.UC.sendMailUrl;
            
			String json_result = HttpClientHelper.post(httpClient, url, map) ;

			JSONObject jsonObject = JSONObject.fromObject(json_result);

			String type = jsonObject.getString("type");
			if ("success".equals(type)) {
				return Message.success(jsonObject.getString("content"));
			} else {
				// 获取验证码失败
				return Message.error(jsonObject.getString("content"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	return Message.error("末知错误");
}
	
public Message register(String username, String password, String authcode, Long areaId, Member.RegType regType, HttpServletRequest request, HttpServletResponse response) {
	HttpClient httpClient = HttpClientHelper.getUCHttpClient(request);
	try {
		// 先调用用户中心注册接口,成功后添加账户
		String url = UICConfig.UC.registerUrl;
		Map<String, String> map = new HashMap<String, String>();
		if (regType.equals(Member.RegType.mobile)) {
			map.put("mobile", username);
		}
		if (regType.equals(Member.RegType.email)) {
			map.put("email", username);
		}
		map.put("username", username);
		map.put("captcha", authcode);
		map.put("areaId", areaId.toString());
		map.put("openid", (String)request.getSession().getAttribute(Member.WEIXIN_OPENT_ID));
		map.put("enPassword", Base64Util.encode(password));
		String json_result = HttpClientHelper.post(httpClient, url, map);

		JSONObject json_object = JSONObject.fromObject(json_result);
		String result = json_object.getString("type");
		String desc = json_object.getString("content");

		if ("success".equals(result)) {
			try {
				Member member = createMember(httpClient);
				if (member != null) {
					Setting setting = SettingUtils.get();
					WebUtils.addCookie(request, response, Cookies.HOST, setting.getSiteUrl(), -1, "/", setting.getCookieDomain(), null);
					WebUtils.addCookie(request, response, Member.USERNAME_COOKIE_NAME, member.getUsername(), -1, "/", setting.getCookieDomain(), null);
				}
				Principal principal = new Principal(member.getId(), member.getUsername());
				request.getSession().setAttribute(Member.PRINCIPAL_ATTRIBUTE_NAME, principal);
				principal.createSign();
				String uctoken_encrypt = DESUtil.encrypt(JsonUtils.toJson(principal), Constant.generateKey);
				Setting setting = SettingUtils.get();
				WebUtils.addCookie(request, response, Cookies.UC_TOKEN, uctoken_encrypt, -1, "/", setting.getCookieDomain(), null);
			} catch (Exception e) {
				e.printStackTrace();
				return Message.error("注册失败");
			}
			return Message.success("注册成功");// 成功
		} else {
			return Message.error(desc);// 失败描述
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	return Message.error("未知错误");
}

public Message addMember(String username, String password, String authcode, Long areaId,Member.RegType regType,String Extension,HttpServletRequest request, HttpServletResponse response) {
	HttpClient httpClient = HttpClientHelper.getUCHttpClient(request);
	try {
		// 先调用用户中心注册接口,成功后添加账户
		String url = UICConfig.UC.registerUrl;
		Map<String, String> map = new HashMap<String, String>();
		if (regType.equals(Member.RegType.mobile)) {
			map.put("mobile", username);
		}
		if (regType.equals(Member.RegType.email)) {
			map.put("email", username);
		}
		map.put("username", username);
		map.put("captcha", authcode);
		map.put("areaId", areaId.toString());
		map.put("openid", (String)request.getSession().getAttribute(Member.WEIXIN_OPENT_ID));
		map.put("enPassword", Base64Util.encode(password));
		map.put("Extension",Extension);
		String json_result = HttpClientHelper.post(httpClient, url, map);

		JSONObject json_object = JSONObject.fromObject(json_result);
		String result = json_object.getString("type");
		String desc = json_object.getString("content");

		if ("success".equals(result)) {
			try {
				Member member = createMember(httpClient);
			} catch (Exception e) {
				e.printStackTrace();
				return Message.error("注册失败");
			}
			return Message.success("注册成功");// 成功
		} else {
			return Message.error(desc);// 失败描述
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	return Message.error("未知错误");
}


	public Message updateUserInfo(String name, String birth, String address,String phone, String zipCode, String sex, String areaId, String headImg, HttpServletRequest request) {
		Message message = autoLoginTo();
		if (message.getType()== Message.Type.success) {
			HttpClient httpClient = HttpClientHelper.getUCHttpClient(request);
			try {
				// crm调度，修改视频账号密码
				String url = UICConfig.UC.updateUserInfoUrl;
				Map<String, String> map = new HashMap<String, String>();
				map.put("name",name);
				map.put("birth",birth);
				map.put("address",address);
				map.put("phone",phone);
				map.put("zipCode",zipCode);
				map.put("sex",sex);
				map.put("areaId",areaId);
				map.put("headImg",headImg);
				String json_result = HttpClientHelper.post(httpClient, url, map);

				JSONObject jsonObject = JSONObject.fromObject(json_result);
				String result = jsonObject.getString("type");
				String desc = jsonObject.getString("content");
				if ("success".equals(result)) {
					return Message.success("修改成功");
				} else {
					return Message.error(desc);
				}
			} catch (Exception e) {
				e.printStackTrace();
				return Message.error("修改失败");
			}
		} else {
	      return message;
		}
		
	}
	
	public Message updatePass(String newPwd, String oldPwd, HttpServletRequest request) {
		Message message = autoLoginTo();
		if (message.getType()== Message.Type.success) {
			HttpClient httpClient = HttpClientHelper.getUCHttpClient(request);
			try {
				// crm调度，修改视频账号密码
				String url = UICConfig.UC.updatePassUrl;
				Map<String, String> map = new HashMap<String, String>();
				map.put("enType","BASE64");
				map.put("oldPass",Base64Util.encode(oldPwd));
				map.put("newPass",Base64Util.encode(newPwd));
				String json_result = HttpClientHelper.post(httpClient, url, map);

				JSONObject jsonObject = JSONObject.fromObject(json_result);
				String result = jsonObject.getString("type");
				String desc = jsonObject.getString("content");
				if ("success".equals(result)) {
					return Message.success("修改成功");
				} else {
					return Message.error(desc);
				}
			} catch (Exception e) {
				e.printStackTrace();
				return Message.error("修改失败");
			} 
		} else {
	    	return message;
		}
	}
	
	public Message bindUpdate(String captcha, HttpServletRequest request) {
		Message message = autoLoginTo();
		if (message.getType()== Message.Type.success) {
			HttpClient httpClient = HttpClientHelper.getUCHttpClient(request);
			try {
				// crm调度，修改视频账号密码
				String url = UICConfig.UC.bindUpdateUrl;
				Map<String, String> map = new HashMap<String, String>();
				map.put("enType","BASE64");
				map.put("captcha",captcha);
				String json_result = HttpClientHelper.post(httpClient, url, map);

				JSONObject jsonObject = JSONObject.fromObject(json_result);
				String result = jsonObject.getString("type");
				String desc = jsonObject.getString("content");
				if ("success".equals(result)) {
					return Message.success(desc);
				} else {
					return Message.error(desc);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
			}
		}
		return Message.error("绑定失败");
	}

	public Message updatePaymentPass(String newPwd, String oldPwd, HttpServletRequest request) {
		Message message = autoLoginTo();
		if (message.getType()== Message.Type.success) {
			HttpClient httpClient = HttpClientHelper.getUCHttpClient(request);
			try {
				// crm调度，修改视频账号密码
				String url = UICConfig.UC.updatePaymentPassUrl;
				Map<String, String> map = new HashMap<String, String>();
				map.put("enType","BASE64");
				map.put("oldPass",Base64Util.encode(oldPwd));
				map.put("newPass",Base64Util.encode(newPwd));
				String json_result = HttpClientHelper.post(httpClient, url, map);

				JSONObject jsonObject = JSONObject.fromObject(json_result);
				String result = jsonObject.getString("type");
				String desc = jsonObject.getString("content");
				if ("success".equals(result)) {
					return Message.success("修改成功");
				} else {
					return Message.error(desc);
				}
			} catch (Exception e) {
				e.printStackTrace();
				return Message.error("修改失败");
			}
		} else {
	      return message;
		}
		
	}


	public Message retrievePass(String username,String newPwd,String captcha, HttpServletRequest request) {
		HttpClient httpClient = HttpClientHelper.getUCHttpClient(request);
		try {
			// 调用uc找回密码
			String url = UICConfig.UC.backPassUrl;
			Map<String, String> map = new HashMap<String, String>();
			map.put("username", username);
			map.put("captcha", captcha);
			map.put("enType","BASE64");
			map.put("newPass",Base64Util.encode(newPwd));

			String json_result = HttpClientHelper.post(httpClient, url, map) ;
			JSONObject jsonObject = JSONObject.fromObject(json_result);
			String result = jsonObject.getString("type");
			String desc = jsonObject.getString("content");
			if ("success".equals(result)) {
				return Message.success("找回成功");
			} else {
				return Message.error(desc);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Message.error("找回失败");
	}
	
	public Message checkPaymentPassword(String password, HttpServletRequest request) {
		Message message = autoLoginTo();
		if (message.getType()== Message.Type.success) {
			HttpClient httpClient = HttpClientHelper.getUCHttpClient(request);
			try {
				// 调用uc找回密码
				String url = UICConfig.UC.checkPaymentPassword;
				Map<String, String> map = new HashMap<String, String>();
				map.put("enType","BASE64");
				map.put("enPassword",Base64Util.encode(password));

				
				String json_result = HttpClientHelper.get(httpClient, url, map) ;
				JSONObject jsonObject = JSONObject.fromObject(json_result);
				String result = jsonObject.getString("type");
				String desc = jsonObject.getString("content");
				if ("success".equals(result)) {
					return Message.success("密码正确");
				} else {
					return Message.error(desc);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		    return Message.error("会话无效-已登录");
		} else  {
		    return Message.error("会话无效");
		}
	}

	public Message retrievePaymentPass(String username,String newPwd,String captcha, HttpServletRequest request) {
		HttpClient httpClient = HttpClientHelper.getUCHttpClient(request);
		try {
			// 调用uc找回密码
			String url = UICConfig.UC.backPaymentPassUrl;
			Map<String, String> map = new HashMap<String, String>();
			map.put("username",username);
			map.put("captcha", captcha);
			map.put("enType","BASE64");
			map.put("newPass",Base64Util.encode(newPwd));

			String json_result = HttpClientHelper.post(httpClient, url, map) ;
			JSONObject jsonObject = JSONObject.fromObject(json_result);
			String result = jsonObject.getString("type");
			String desc = jsonObject.getString("content");
			if ("success".equals(result)) {
				return Message.success("找回成功");
			} else {
				return Message.error(desc);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return Message.error("找回失败");
	}
	
	public Message idcardSave(String name, String idCard, String pathFront, String pathBack,HttpServletRequest request) {
		Message message = autoLoginTo();
		if (message.getType()== Message.Type.success) {
			HttpClient httpClient = HttpClientHelper.getUCHttpClient(request);
			try {
				// crm调度，修改视频账号密码
				String url = UICConfig.UC.idcardSaveUrl;
				Map<String, String> map = new HashMap<String, String>();
				map.put("name",name);
				map.put("idCard",idCard);
				map.put("pathFront",pathFront);
				map.put("pathBack",pathBack);
				String json_result = HttpClientHelper.post(httpClient, url, map);

				JSONObject jsonObject = JSONObject.fromObject(json_result);
				String result = jsonObject.getString("type");
				String desc = jsonObject.getString("content");
				if ("success".equals(result)) {
					return Message.success("提交成功");
				} else {
					return Message.error(desc);
				}
			} catch (Exception e) {
				e.printStackTrace();
				return Message.error("提交失败");
			}
		} else {
	      return message;
		}
	}
	
}
