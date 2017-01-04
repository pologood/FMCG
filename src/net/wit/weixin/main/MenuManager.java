package net.wit.weixin.main;

import java.net.URLEncoder;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import net.wit.Setting;
import net.wit.util.SettingUtils;
import net.wit.weixin.pojo.AccessToken;
import net.wit.weixin.pojo.Button;
import net.wit.weixin.pojo.CommonButton;
import net.wit.weixin.pojo.ComplexButton;
import net.wit.weixin.pojo.Menu;
import net.wit.weixin.util.WeixinUtil;

import org.apache.axis2.transport.http.util.URIEncoderDecoder;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 菜单管理器类
 * @author Administrator
 */
public class MenuManager {
	public static void createMenu() {
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
		    int del = WeixinUtil.deleteMenu(at.getToken());
			if (0 == del) {
				System.out.println("菜单创建成功！");
			} else {
				System.out.println("菜单创建失败！" + del);
			}
			int result = WeixinUtil.createMenu(getMenu(), at.getToken());
			// JSONObject result = WeixinUtil.getMenu(at.getToken());
			// System.out.println(result.toString());

			// 判断菜单创建结果
			if (0 == result) {
				System.out.println("菜单创建成功！");
			} else {
				System.out.println("菜单创建失败！" + result);
			}
		}
	}

	/**
	 * 组装菜单数据
	 * @return
	 */
	public static Menu getMenu() {
		ResourceBundle bundle = PropertyResourceBundle.getBundle("config");

		Menu menu = new Menu();
		String wxMenu = bundle.getString("wxMenu");

		CommonButton btn11 = new CommonButton();
		Setting setting = SettingUtils.get();
		btn11.setName("商城");
		btn11.setType("view");
		btn11.setUrl(codeUrlO2(URLEncoder.encode(bundle.getString("WeiXinSiteUrl") + "/weixin/index.jhtml")));

		CommonButton btn12 = new CommonButton();
		btn12.setName("附近");
		btn12.setType("view");
		btn12.setUrl(codeUrlO2(URLEncoder.encode(bundle.getString("WeiXinSiteUrl") + "/weixin/tenant/nearby.jhtml")));

		String url = bundle.getString("WeiXinSiteUrl") + "/www/invite/index.html";

		if (wxMenu.equals("101")) {
			url = bundle.getString("WeiXinSiteUrl") + "/www/invite/index_ztb.html";
		}

		CommonButton btn32 = new CommonButton();
		btn32.setName("会员中心");
		btn32.setType("view");
		btn32.setUrl(codeUrlO2(URLEncoder.encode(bundle.getString("WeiXinSiteUrl") + "/weixin/member/index.jhtml")));

		CommonButton btn33 = new CommonButton();
		btn33.setName("我的订单");
		btn33.setType("view");
		btn33.setUrl(codeUrlO2(URLEncoder.encode(bundle.getString("WeiXinSiteUrl") + "/weixin/member/order.jhtml")));

		CommonButton btn34 = new CommonButton();
		btn34.setName("绑定账号");
		btn34.setType("view");
		btn34.setUrl(bundle.getString("WeiXinSiteUrl") + "/weixin/member/bindmobile.jhtml");
		
		CommonButton btn31 = new CommonButton();
		btn31.setName("免费开店");
		btn31.setType("view");
		btn31.setUrl(url);

		ComplexButton mainBtn4 = new ComplexButton();
		mainBtn4.setName("我的");
		mainBtn4.setSub_button(new CommonButton[] { btn32,btn33,btn31 });

		if (wxMenu==null) {
		   menu.setButton(new Button[] { btn11, btn12, mainBtn4 });
		} else {
		   menu.setButton(new Button[] { btn11, btn12, mainBtn4 });
		}
		return menu;
	}

	public static String codeUrlO2(String url) {
		ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
		String appId = bundle.getString("APPID");// 睿商圈
		// 第三方用户唯一凭证密钥
		try {
			url = WeixinUtil.getOauth2Code(appId,url, "snsapi_base");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return url;
	}

}
