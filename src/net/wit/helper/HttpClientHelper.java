/**
 *====================================================
 * 文件名称: HttpClientHelper.java
 * 修订记录：
 * No    日期				作者(操作:具体内容)
 * 1.    2014年6月18日			Administrator(创建:创建文件)
 *====================================================
 * 类描述：(说明未实现或其它不应生成javadoc的内容)
 * 
 */
package net.wit.helper;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;
import net.wit.Setting;
import net.wit.constant.Constant;
import net.wit.util.SettingUtils;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @ClassName: HttpClientHelper
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author Administrator
 * @date 2014年6月18日 下午3:34:28
 */
public class HttpClientHelper {

	/**
	 * 组装请求头
	 * @param params
	 * @return
	 */
	private static Header[] buildHeader(Map<String, String> params) {
		Header[] headers = new BasicHeader[params.size()];
		int i = 0;
		for (Map.Entry<String, String> entry : params.entrySet()) {
			headers[i] = new BasicHeader(entry.getKey(), entry.getValue());
			i++;
		}
		return headers;
	}

	public static HttpClient getUCHttpClient(HttpServletRequest request) {
		if (request==null) {
			RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
			if (requestAttributes != null) {
				request = ((ServletRequestAttributes) requestAttributes).getRequest();
			}
		}
		HttpSession session = request.getSession();
		if (session == null) {
			return null;
		}
		//System.out.println("init==>" + session.getId());
		Object attribute = session.getAttribute(Constant.UC_HTTP_CLIENT);
		if (attribute == null) {
			Setting setting = SettingUtils.get();
			BasicCookieStore cookieStore = new BasicCookieStore();
			BasicClientCookie cookie = new BasicClientCookie("JSESSIONID", session.getId());
			cookie.setVersion(0);
			//cookie.setDomain(setting.getCookieDomain());
			cookie.setPath("/");
			cookieStore.addCookie(cookie);
			CloseableHttpClient httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
			request.getSession().setAttribute(Constant.UC_HTTP_CLIENT, httpClient);
			
			return httpClient;
		} else {
			return (HttpClient) attribute;
		}
	}

	public static String get(final HttpClient client, String url, Map<String, String> params) {
		return get(client, url, params, null);
	}

	public static String getJsessionid(HttpResponse httpResponse) {
		String JSESSIONID = "";
		Header[] heard = httpResponse.getHeaders("Set-Cookie");
		for (int i = 0; i < heard.length; i++) {
			if (heard[i].getValue().contains("JSESSIONID")) {
				JSESSIONID = heard[i].getValue();
				break;
			}
		}
		return JSESSIONID;
	}
	
	@SuppressWarnings("deprecation")
	public static String get(final HttpClient client, String url, Map<String, String> params, Map<String, String> headers) {
		HttpGet get = null;
		try {
			if (params != null) {
				StringBuffer sb = new StringBuffer();
				for (Entry<String, String> entry : params.entrySet()) {
					if (entry.getValue() == null) {
						continue;
					}
					sb.append(entry.getKey());
					sb.append("=");
					sb.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
					sb.append("&");
				}
				if (sb.length() > 0) {
					sb = sb.deleteCharAt(sb.length() - 1);
				}
				url = url + (url.contains("?") ? "&" : "?") + sb;
			}
			get = new HttpGet(url);
			if (headers != null && !headers.isEmpty()) { // 设置 header
				get.setHeaders(buildHeader(headers));
			}
			
			get.addHeader("accept-encoding", "gzip,deflate");  
			get.addHeader("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0; Alexa Toolbar; Maxthon 2.0)"); 

			HttpResponse response = client.execute(get);
			
			//System.out.println("get==>" + getJsessionid(response));
			HttpEntity entity = (HttpEntity) response.getEntity();
			String responseText = EntityUtils.toString(entity, "UTF-8");
			EntityUtils.consume(entity);
			return responseText;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (get != null) {
				get.abort();
			}
			if (client != null) {
				client.getConnectionManager().closeExpiredConnections();
			}
		}
	}

	public static String post(final HttpClient client, String url, Map<String, String> params) {
		return post(client, url, params, null);
	}

	@SuppressWarnings("deprecation")
	public static String post(final HttpClient client, String url, Map<String, String> params, Map<String, String> headers) {
		HttpPost post = null;
		try {
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			if (params != null) {
				for (Entry<String, String> entry : params.entrySet()) {
					nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
				}
			}
			post = new HttpPost(url);
			if (headers != null && !headers.isEmpty()) { // 设置 header
				post.setHeaders(buildHeader(headers));
			}
			post.setEntity(new UrlEncodedFormEntity(nvps,HTTP.UTF_8));
			HttpResponse response = client.execute(post);
			//System.out.println("post==>" + getJsessionid(response));
			HttpEntity entity = (HttpEntity) response.getEntity();
			String responseText = EntityUtils.toString(entity, "UTF-8");
			EntityUtils.consume(entity);
			return responseText;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (post != null) {
				post.abort();
			}
			if (client != null) {
				client.getConnectionManager().closeExpiredConnections();
			}
		}
	}

	public static void main(String[] args) {
		BasicCookieStore cookieStore = new BasicCookieStore();
		BasicClientCookie cookie = new BasicClientCookie("JSESSIONID", "559B6AA10AFDC1DBCF7D4B70144E68B9");
		cookie.setVersion(0);
		cookie.setDomain("");
		cookie.setPath("/");
		cookieStore.addCookie(cookie);
		CloseableHttpClient httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
		String url = "http://localhost:8080/tiaohuo/register/send_mobile.jhtml";
		Map<String, String> params = new HashMap<String, String>();
		params.put("mobile", "15280091258");
		String responseText = HttpClientHelper.post(httpClient, url, params);
		JSONObject jsonObject = JSONObject.fromObject(responseText);
		String securityCode = jsonObject.getString("content");
		url = "http://localhost:8080/tiaohuo/register/submit.jhtml";
		params.clear();
		params.put("mobile", "11280091258");
		params.put("password", "123456");
		params.put("securityCode", securityCode);
		responseText = HttpClientHelper.post(httpClient, url, params);
		//System.out.println(responseText);
	}

}
