package net.wit.weixin.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.wit.util.DateUtil;
import net.wit.weixin.pojo.AccessToken;
import net.wit.weixin.pojo.Menu;
import net.wit.weixin.pojo.Ticket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 微信公众平台通用接口工具类
 * @author Administrator
 */
public class WeixinUtil {

	private static Logger log = LoggerFactory.getLogger(WeixinUtil.class);

	private static AccessToken accessToken = null;

	private static Ticket jsapi_ticket = null;

	// 获取access_token的接口地址（GET） 限200（次/天） 
	private static String access_token_url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";

	// 获取oauth2 access_token的接口地址（GET） 限200（次/天） 
	private static String oauth2_access_token_url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";

	// 菜单创建（POST） 限100（次/天）
	private static String menu_create_url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";

	// 菜单查询（GET） 限1000（次/天）
	private static String menu_get_url = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=ACCESS_TOKEN";

	// 菜单删除（GET） 限100（次/天）
	private static String menu_delete_url = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=ACCESS_TOKEN";

	// 获取关注者列表
	private static String user_getlist_url = "https://api.weixin.qq.com/cgi-bin/user/get?access_token=ACCESS_TOKEN&next_openid=NEXT_OPENID";

	// 使用code换取access_token
	private static String code_at_url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";

	// 使用refresh_token换取access_token
	private static String refresh_at_url = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=APPID&grant_type=refresh_token&refresh_token=REFRESH_TOKEN";

	// 使用access_token获取用户信息
	private static String user_getinfo_url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";

	// 创建永久二维码
	private static String create_qrcode = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=ACCESS_TOKEN";

	// 根据OpenID列表群发
	private static String massSendUrl = "https://api.weixin.qq.com/cgi-bin/message/mass/send?access_token=ACCESS_TOKEN";

	// 用户同意授权，获取code
	private static String oauth2_get_code = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=123#wechat_redirect";

	private static String GETTICKET = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";
 
	private static String send_message = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";
	
	/**
	 * 发起https请求并获取结果
	 * @param requestUrl 请求地址
	 * @param requestMethod 请求方式（GET、POST）
	 * @param outputStr 提交的数据
	 * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
	 */
	public static JSONObject httpRequest(String requestUrl, String requestMethod, String outputStr) {
		JSONObject jsonObject = null;
		StringBuffer buffer = new StringBuffer();
		try {
			// 创建SSLContext对象，并使用我们指定的信任管理器初始化
			TrustManager[] tm = { new MyX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();

			URL url = new URL(requestUrl);
			HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
			httpUrlConn.setSSLSocketFactory(ssf);

			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			// 设置请求方式（GET/POST）
			httpUrlConn.setRequestMethod(requestMethod);

			if ("GET".equalsIgnoreCase(requestMethod))
				httpUrlConn.connect();

			// 当有数据需要提交时
			if (null != outputStr) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				// 注意编码格式，防止中文乱码
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}

			// 将返回的输入流转换成字符串
			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();
			// 释放资源
			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();
			jsonObject = JSONObject.fromObject(buffer.toString());
		} catch (ConnectException ce) {
			log.error("Weixin server connection timed out.");
		} catch (Exception e) {
			log.error("https request error:{}", e);
		}
		return jsonObject;
	}

	/**
	 *  * 获取access_token  *   * @param appid 凭证  * @param appsecret 密钥  * @return access_token  
	 */
	public static AccessToken getAccessToken(String appid, String appsecret) {
		if (accessToken == null || accessToken.getExpire().getTime() <= (new Date()).getTime()) {
			String requestUrl = access_token_url.replace("APPID", appid).replace("APPSECRET", appsecret);
			JSONObject jsonObject = httpRequest(requestUrl, "GET", null);
			// 如果请求成功 
			if (null != jsonObject) {
				try {
					accessToken = new AccessToken();
					accessToken.setToken(jsonObject.getString("access_token"));
					accessToken.setExpiresIn(jsonObject.getInt("expires_in"));
					accessToken.setExpire(DateUtil.transpositionDate(new Date(), Calendar.SECOND, new Integer(jsonObject.getInt("expires_in"))));
				} catch (JSONException e) {
					accessToken = null;
					// 获取token失败
					log.error("获取token失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));
				}
			}
		}
		return accessToken;
	}

	/**
	 *  * 获取access_token  *   * @param appid 凭证  * @param appsecret 密钥  * @return access_token  
	 */
	public static AccessToken getOauth2AccessToken(String appid, String appsecret, String code) {
		AccessToken accessToken = null;

		String requestUrl = oauth2_access_token_url.replace("APPID", appid).replace("SECRET", appsecret).replace("CODE", code);
		JSONObject jsonObject = httpRequest(requestUrl, "GET", null);
		// 如果请求成功 
		if (null != jsonObject) {
			try {
				accessToken = new AccessToken();
				accessToken.setToken(jsonObject.getString("access_token"));
				accessToken.setExpiresIn(jsonObject.getInt("expires_in"));
				accessToken.setOpenid(jsonObject.getString("openid"));
				accessToken.setRefreshToken(jsonObject.getString("refresh_token"));
				accessToken.setScope(jsonObject.getString("scope"));
			} catch (JSONException e) {
				accessToken = null;
				log.error("获取token失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));
			}
		}
		return accessToken;
	}

	/**
	 * 创建菜单
	 * @param menu 菜单实例
	 * @param accessToken 有效的access_token
	 * @return 0表示成功，其他值表示失败
	 */
	public static int createMenu(Menu menu, String accessToken) {
		int result = 0;

		// 拼装创建菜单的url
		String url = menu_create_url.replace("ACCESS_TOKEN", accessToken);
		// 将菜单对象转换成json字符串
		String jsonMenu = JSONObject.fromObject(menu).toString();
		System.out.println(jsonMenu);
		// 调用接口创建菜单
		JSONObject jsonObject = httpRequest(url, "POST", jsonMenu);

		if (null != jsonObject) {
			if (0 != jsonObject.getInt("errcode")) {
				result = jsonObject.getInt("errcode");
				log.error("创建菜单失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));
			}
		}

		return result;
	}

	/**
	 * 查询菜单
	 * @param accessToken 有效的access_token
	 * @return JSONObject
	 */
	public static JSONObject createQrcode(String accessToken,String data) {
		// 拼装查询菜单的url
		String url = create_qrcode.replace("ACCESS_TOKEN", accessToken);
		// 调用接口查询菜单
		JSONObject jsonObject = httpRequest(url, "POST", data);
		
		if (null != jsonObject) {
			if (jsonObject.containsKey("errcode")) {
				if (0 != jsonObject.getInt("errcode")) {
					System.out.println("创建二维码失败，err:"+jsonObject.getInt("errcode")+" info:"+jsonObject.getString("errmsg"));
				}
			}
		}

		return jsonObject;
	}


	/**
	 * 查询菜单
	 * @param accessToken 有效的access_token
	 * @return JSONObject
	 */
	public static JSONObject getMenu(String accessToken) {
		// 拼装查询菜单的url
		String url = menu_get_url.replace("ACCESS_TOKEN", accessToken);
		// 调用接口查询菜单
		JSONObject jsonObject = httpRequest(url, "GET", null);

		return jsonObject;
	}

	/**
	 * 删除菜单
	 * @param accessToken 有效的access_token
	 * @return 0表示成功，其他值表示失败
	 */
	public static int deleteMenu(String accessToken) {
		int result = 0;

		// 拼装删除菜单的url
		String url = menu_delete_url.replace("ACCESS_TOKEN", accessToken);
		// 调用接口删除菜单
		JSONObject jsonObject = httpRequest(url, "GET", null);

		if (null != jsonObject) {
			if (0 != jsonObject.getInt("errcode")) {
				result = jsonObject.getInt("errcode");
				log.error("删除菜单失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));
			}
		}

		return result;
	}

	/**
	 * 查询菜单
	 * @param accessToken 有效的access_token
	 * @return JSONObject
	 */
	public static JSONObject getMenuObject(String accessToken) {
		// 拼装查询菜单的url
		String url = menu_get_url.replace("ACCESS_TOKEN", accessToken);
		// 调用接口查询菜单
		JSONObject jsonObject = httpRequest(url, "GET", null);

		return jsonObject;
	}

	/**
	 * 查询关注者列表
	 * @param accessToken 有效的access_token
	 * @return JSONObject
	 */
	public static JSONObject getUserList(String accessToken, String nextOpenid) {
		// 拼装查询菜单的url
		String url = user_getlist_url.replace("ACCESS_TOKEN", accessToken).replace("NEXT_OPENID", nextOpenid);
		// 调用接口查询菜单
		JSONObject jsonObject = httpRequest(url, "GET", null);

		return jsonObject;
	}

	/**
	 * 通过code获取acces_token
	 * @param appid 凭证  * @param appsecret 密钥
	 * @param code 票据
	 * @return JSONObject
	 */
	public static JSONObject getAccessToken(String appid, String secret, String code) {
		// 拼装查询菜单的url
		String url = code_at_url.replace("APPID", appid).replace("SECRET", secret).replace("CODE", code);
		// 调用接口查询菜单
		JSONObject jsonObject = httpRequest(url, "GET", null);

		return jsonObject;
	}

	/**
	 * 通过refresh_token换取access_token
	 * @param refresh_token
	 * @param appid
	 */
	public static JSONObject refreshAccessToken(String appid, String refresh_token) {
		// 拼装查询菜单的url
		String url = refresh_at_url.replace("APPID", appid).replace("SECRET", refresh_token);
		// 调用接口查询菜单
		JSONObject jsonObject = httpRequest(url, "GET", null);

		return jsonObject;
	}

	/**
	 * 使用access_token获取用户信息  * @param access_token
	 * @param openid
	 * @return JSONObject
	 */
	public static JSONObject getUserInfo(String access_token, String openid) {
		// 拼装查询菜单的url
		String url = user_getinfo_url.replace("ACCESS_TOKEN", access_token).replace("OPENID", openid);
		// 调用接口查询菜单
		JSONObject jsonObject = httpRequest(url, "GET", null);
		return jsonObject;
	}

	/**
	 * 判断是否是QQ表情
	 * @param content
	 * @return
	 */
	public static boolean isFace(String content) {
		boolean result = false;
		String faceRegex = "/::\\)|/::~|/::B|/::\\||/:8-\\)|/::<|/::$|/::X|/::Z" + "|/::'\\(|/::-\\||/::@|/::P|/::D|/::O|/::\\(|/::\\+|/:--b|/::Q" + "|/::T|/:,@P|/:,@-D|/::d|/:,@o|/::g|/:\\|-\\)|/::!|/::L|/::>|/::,@"
				+ "|/:,@f|/::-S|/:\\?|/:,@x|/:,@@|/::8|/:,@!|/:!!!|/:xx|/:bye|/:wipe" + "|/:dig|/:handclap|/:&-\\(|/:B-\\)|/:<@|/:@>|/::-O|/:>-\\||/:P-\\(" + "|/::'\\||/:X-\\)|/::\\*|/:@x|/:8\\*|/:pd|/:<W>|/:beer|/:basketb|/:oo"
				+ "|/:coffee|/:eat|/:pig|/:rose|/:fade|/:showlove|/:heart|/:break|/:cake" + "|/:li|/:bome|/:kn|/:footb|/:ladybug|/:shit|/:moon|/:sun|/:gift|/:hug" + "|/:strong|/:weak|/:share|/:v|/:@\\)|/:jj|/:@@|/:bad|/:lvu|/:no|/:ok"
				+ "|/:love|/:<L>|/:jump|/:shake|/:<O>|/:circle|/:kotow|/:turn|/:skip|/:oY" + "|/:#-0|/:hiphot|/:kiss|/:<&|/:&>";
		Pattern p = Pattern.compile(faceRegex);
		if (p.matcher(content).matches())
			result = true;

		return result;
	}

	/**
	 *  * 根据OpenID列表群发 
	 */
	public static int massSend(String appid, String appsecret, String message) {
		int result = 0;
		AccessToken token = WeixinUtil.getAccessToken(appid, appsecret);
		String requestUrl = massSendUrl.replace("ACCESS_TOKEN", token.getToken());
		JSONObject jsonObject = httpRequest(requestUrl, "POST", message);
		if (null != jsonObject) {
			if (0 != jsonObject.getInt("errcode")) {
				result = jsonObject.getInt("errcode");
				log.error("群发失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));
			}
		}
		return result;
	}


	/**
	 *  * 根据OpenID列表群发 
	 */
	public static int sendTemplete(String appid, String appsecret, String message) {
		int result = 0;
		AccessToken token = WeixinUtil.getAccessToken(appid, appsecret);
		System.out.println(token.getToken());
		String requestUrl = send_message.replace("ACCESS_TOKEN", token.getToken());
		System.out.println(requestUrl);
		JSONObject jsonObject = httpRequest(requestUrl, "POST", message);
		if (null != jsonObject) {
			if (0 != jsonObject.getInt("errcode")) {
				result = jsonObject.getInt("errcode");
				log.error("模板消息 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));
			}
		}
		return result;
	}
	
	/**
	 *  * 根据OpenID列表群发 
	 */
	public static String getOauth2Code(String appid, String redirectUri, String scope) {
		try {
			return WeixinUtil.oauth2_get_code.replace("APPID", appid).replace("REDIRECT_URI", redirectUri).replace("SCOPE", scope);
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}

	public static Ticket getTicket() {
		if (jsapi_ticket == null || jsapi_ticket.getExpire().getTime() <= (new Date()).getTime()) {
		    ResourceBundle bundle=PropertyResourceBundle.getBundle("config");
			AccessToken token = getAccessToken(bundle.getString("APPID"), bundle.getString("APPSECRET"));
			if (token != null) {
				String requestUrl = GETTICKET.replace("ACCESS_TOKEN", token.getToken());
				JSONObject jsonObject = httpRequest(requestUrl, "GET", null);
				if (jsonObject != null) {
					jsapi_ticket = new Ticket();
					jsapi_ticket.setTicket(jsonObject.getString("ticket"));
					jsapi_ticket.setExpires_in(jsonObject.getInt("expires_in"));
					jsapi_ticket.setExpire(DateUtil.transpositionDate(new Date(), Calendar.SECOND, new Integer(jsonObject.getInt("expires_in"))));
					return jsapi_ticket;
				}
			}
		}
		return jsapi_ticket;
	}

}
