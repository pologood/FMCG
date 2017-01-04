/**
 *====================================================
 * 文件名称: RegisterServiceImpl.java
 * 修订记录：
 * No    日期				作者(操作:具体内容)
 * 1.    2014年7月10日			Administrator(创建:创建文件)
 *====================================================
 * 类描述：(说明未实现或其它不应生成javadoc的内容)
 * 
 */
package net.wit.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.wit.constant.Constant;
import net.wit.helper.HttpClientHelper;
import net.wit.service.RegisterService;

import org.apache.http.client.HttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @ClassName: RegisterServiceImpl
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author Administrator
 * @date 2014年7月10日 上午9:21:27
 */
@Service("registerService")
public class RegisterServiceImpl implements RegisterService {

	@Value("${vbox.ucServer}")
	private String memberCenter;

	public Map<String, Object> index(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> params = new HashMap<String, Object>();
		HttpClient httpClient = HttpClientHelper.getUCHttpClient(request);
		String url = memberCenter + Constant.UC_SUBMIT_GET;
		String responseText = HttpClientHelper.get(httpClient, url, null);
		JSONObject jsonObject = JSONObject.fromObject(responseText);
		String captchaId = jsonObject.getString("captchaId");
		params.put("captchaId", captchaId);
		params.put("uc_center", true);
		params.put("uc_captcha", memberCenter + Constant.UC_CAPTCHA);
		return params;
	}

	public boolean checkUsername(String username, HttpServletRequest request) {
		HttpClient httpClient = HttpClientHelper.getUCHttpClient(request);
		String url = memberCenter + Constant.UC_CHECK_USERNAME;
		Map<String, String> params = new HashMap<String, String>();
		params.put("username", username);
		String responseText = HttpClientHelper.get(httpClient, url, params);
		if ("true".equals(responseText)) {
			return true;
		}
		return false;
	}

	public boolean checkEmail(String email, HttpServletRequest request) {
		HttpClient httpClient = HttpClientHelper.getUCHttpClient(request);
		String url = memberCenter + Constant.UC_CHECK_EMAIL;
		Map<String, String> params = new HashMap<String, String>();
		params.put("email", email);
		String responseText = HttpClientHelper.get(httpClient, url, params);
		if ("true".equals(responseText)) {
			return true;
		}
		return false;
	}

	public boolean checkMobile(String mobile, HttpServletRequest request) {
		HttpClient httpClient = HttpClientHelper.getUCHttpClient(request);
		String url = memberCenter + Constant.UC_CHECK_MOBILE;
		Map<String, String> params = new HashMap<String, String>();
		params.put("mobile", mobile);
		String responseText = HttpClientHelper.get(httpClient, url, params);
		if ("true".equals(responseText)) {
			return true;
		}
		return false;
	}

	public String getCheckCode(String mobile, HttpServletRequest request) {
		HttpClient httpClient = HttpClientHelper.getUCHttpClient(request);
		String url = memberCenter + Constant.UC_SEND_MOBILE;
		Map<String, String> params = new HashMap<String, String>();
		params.put("mobile", mobile);
		String responseText = HttpClientHelper.post(httpClient, url, params);
		JSONObject jsonObject = JSONObject.fromObject(responseText);
		if ("success".equals(jsonObject.getString("type"))) {
			String content = jsonObject.getString("content");
			return content;
		}
		return "";
	}

	public String sendEmail(String email, HttpServletRequest request) {
		HttpClient httpClient = HttpClientHelper.getUCHttpClient(request);
		String url = memberCenter + Constant.UC_SEND_EMAIL;
		Map<String, String> params = new HashMap<String, String>();
		params.put("email", email);
		String responseText = HttpClientHelper.post(httpClient, url, params);
		JSONObject jsonObject = JSONObject.fromObject(responseText);
		if ("success".equals(jsonObject.getString("type"))) {
			String content = jsonObject.getString("content");
			return content;
		}
		return "";
	}

	public String submit(String captchaId, String captcha, String username, String mobile, String email, String checkCode, Long areaId, String password, HttpServletRequest request, HttpServletResponse response) {
		HttpClient httpClient = HttpClientHelper.getUCHttpClient(request);
		String url = memberCenter + Constant.UC_REGISTER_SUBMIT;
		Map<String, String> params = new HashMap<String, String>();
		params.put("captchaId", captchaId);
		params.put("captcha", captcha);
		params.put("username", username);
		params.put("mobile", mobile);
		params.put("email", email);
		params.put("securityCode", checkCode);
		params.put("areaId", areaId + "");
		params.put("password", password);
		String responseText = HttpClientHelper.post(httpClient, url, params);

		JSONObject jsonObject = JSONObject.fromObject(responseText);
		String type = jsonObject.getString("type");
		String content = jsonObject.getString("content");
		if (!"success".equals(type)) {
			return content;
		}
		return "success";
	}
}
