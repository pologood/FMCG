package net.wit.weixin.main;

import java.util.Calendar;
import java.util.Date;

import net.sf.json.JSONObject;
import net.wit.weixin.pojo.AccessToken;
import net.wit.weixin.util.WeixinUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**用户信息管理类
 * 
 * @author Administrator
 *
 */
public class UserInfoManager {
	private static Logger log = LoggerFactory.getLogger(MenuManager.class);
	
	public static void main(String[] args) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		Date date = calendar.getTime();
	}
	
}
