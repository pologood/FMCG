/**
 * @Title：MutualController.java 
 * @Package：net.wit.controller.wap.member 
 * @Description：
 * @author：Chenlf
 * @date：2015年3月15日 下午9:03:54 
 * @version：V1.0   
 */

package net.wit.controller.wap;

import java.util.HashMap;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import net.wit.Setting;
import net.wit.util.JsonUtils;
import net.wit.util.SettingUtils;
import net.wit.util.Sha1Util;
import net.wit.weixin.main.MenuManager;
import net.wit.weixin.pojo.Ticket;
import net.wit.weixin.util.WeiXinUtils;
import net.wit.weixin.util.WeixinUtil;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @ClassName：MutualController
 * @Description：
 * @author：Chenlf
 * @date：2015年3月15日 下午9:03:54
 */

@Controller("wapMutualController")
@RequestMapping("/wap/mutual")
public class MutualController extends BaseController {

	public String getSha1Sign(HashMap<String, Object> params) {
		try {
			String str1 = WeiXinUtils.FormatBizQueryParaMap(params, false);
			return Sha1Util.encode(str1);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	@RequestMapping(value = "/get_config", method = RequestMethod.GET)
	@ResponseBody
	public net.wit.Message getConfig(String url) {
		String noncestr = WeiXinUtils.CreateNoncestr();
		//System.out.println("noncestr==" + noncestr);
		String timeStamp = WeiXinUtils.getTimeStamp();
		//System.out.println("timeStamp==" + timeStamp);

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("noncestr", noncestr);
		Ticket ticket = WeixinUtil.getTicket();
		if (ticket == null) {
			return net.wit.Message.error("ticket 获取失败");
		}
		map.put("jsapi_ticket", ticket.getTicket());
		//System.out.println("jsapi_ticket==" + ticket.getTicket());
		map.put("timestamp", timeStamp);
		map.put("url", url);
		//System.out.println("url==" + url);
		String sha1Sign = getSha1Sign(map);
		//System.out.println("signature==" + sha1Sign);

		HashMap<String, Object> config = new HashMap<String, Object>();
		
	    ResourceBundle bundle=PropertyResourceBundle.getBundle("config");
		Setting setting = SettingUtils.get();
				
		config.put("appId", bundle.getString("APPID"));
		config.put("timestamp", timeStamp);
		config.put("nonceStr", noncestr);
		config.put("signature", sha1Sign);
		config.put("sharetitle", "欢迎关注"+setting.getSiteName());
		config.put("sharedescr", "聚焦同城好店，这座城市，你想要的，我来实现。");
		config.put("sharelink", MenuManager.codeUrlO2( bundle.getString("WeiXinSiteUrl")+"/wap/index.jhtml"));
		config.put("shareimage", bundle.getString("WeiXinSiteUrl")+"/upload/welcome.png");
		return net.wit.Message.success(JsonUtils.toJson(config));
	}
}
