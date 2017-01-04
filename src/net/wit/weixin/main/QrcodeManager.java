package net.wit.weixin.main;

import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import net.sf.json.JSONObject;
import net.wit.weixin.pojo.AccessToken;
import net.wit.weixin.util.WeixinUtil;

/**
 * 菜单管理器类
 * @author Administrator
 */
public class QrcodeManager {
	public static JSONObject createQrcode(String data) {
		ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
		// 第三方用户唯一凭证
		String appId = bundle.getString("APPID");// 睿商圈
		// String appId = "wxd9cfce3d40f0caf7";//测试号
		// 第三方用户唯一凭证密钥
		String appSecret = bundle.getString("APPSECRET");
		// 调用接口获取access_token
		AccessToken at = WeixinUtil.getAccessToken(appId, appSecret);

		if (null != at) {
			// 调用接口创建菜单
			JSONObject json = WeixinUtil.createQrcode(at.getToken(),data);
			return json;
		} else {
			return null;
		}
		
	}
}
