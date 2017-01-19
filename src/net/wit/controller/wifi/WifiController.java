/**
 * @Title：CoreController.java 
 * @Package：net.wit.controller.weixin 
 * @Description：
 * @author：Chenlf
 * @date：2015年2月17日 上午9:34:09 
 * @version：V1.0   
 */

package net.wit.controller.wifi;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.wit.entity.WifiRecord;
import net.wit.service.WifiRecordService;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import net.sf.json.JSONObject;
import net.wit.Message;

/**
 * @ClassName：WifiController
 * @Description：
 * @author：Chenlf
 * @date：2015年2月17日 上午9:34:09
 */
@Controller("wifiController")
@RequestMapping("/wifi")
public class WifiController extends BaseController {

	@Resource(name = "wifiRecordServiceImpl")
	private WifiRecordService wifiRecordService;

	/**
	 * 探针服务器发来的消息  mac 商家的地址 ,rf 信号  umac 手机的地址
	 */
	@RequestMapping(value = "/notify")
	public @ResponseBody Message notify(String[] mac,Long [] rf,String [] umac) {	 
		for (int i=0;i<mac.length;i++) {
			WifiRecord wifi = new WifiRecord();
			wifi.setUuidd(umac[i]);
			wifi.setWuidd(mac[i]);
			wifi.setWifiType(WifiRecord.WifiType.member);
            wifi.setSignall(rf[i]);
			wifiRecordService.save(wifi);
		}
		return Message.success("success");
	}

	/**
	 * 考勤服务器发来的消息  Mac:手机的 mac 地址
	 * Type:1 上班   2 下班  3 中途离开

	 */
	@RequestMapping(value = "/clock")
	public @ResponseBody Message clock(String[] mac,Long [] type) {	 	
		for (int i=0;i<mac.length;i++) {
			System.out.println("mac="+mac[i]);
			System.out.println("type="+type[i]);
		}
       
		return Message.success("success");
	}

}
