/**
 *====================================================
 * 文件名称: ToholaAuthenticodeStrategy.java
 * 修订记录：
 * No    日期				作者(操作:具体内容)
 * 1.    2014年8月19日			Administrator(创建:创建文件)
 *====================================================
 * 类描述：(说明未实现或其它不应生成javadoc的内容)
 * 
 */
package net.wit.domain.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import javax.annotation.Resource;

import net.wit.Setting;
import net.wit.domain.AuthenticodeStrategy;
import net.wit.entity.Order;
import net.wit.entity.Trade;
import net.wit.service.SmsSendService;
import net.wit.util.SettingUtils;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @ClassName: ToholaAuthenticodeStrategy
 * @Description: 验证码发送方式
 * @author Administrator
 * @date 2014年8月19日 上午10:04:32
 */
@Service("authenticodeStrategy")
public class ToholaAuthenticodeStrategy implements AuthenticodeStrategy {

	@Resource(name = "smsSendServiceImpl")
	private SmsSendService smsSendService;

	public void createAuthentiCode(Order order) {}

	public void sendNotify(Order order) {
		Setting setting = SettingUtils.get();
		ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
		if (StringUtils.isNotBlank(order.getMember().getMobile())) {
			Map<String, String> model = new HashMap<String, String>();
			for (Trade t : order.getTrades()) {
				model.put("siteName", setting.getSiteName());
				model.put("validatecode", t.getSn());
				model.put("signature", bundle.getString("signature"));
				if(order.getPhone()!=null&&StringUtils.isNotEmpty(order.getPhone())){
					smsSendService.sendTemplateNoticePool(order.getPhone(), "memberTPLNotice", model);
				}else{
					smsSendService.sendTemplateNoticePool(order.getMember().getMobile(), "memberTPLNotice", model);
				}
				model.clear();
			}
		}
	}
	public void sendNotify(Trade trade) {
		Setting setting = SettingUtils.get();
		ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
		if (StringUtils.isNotBlank(trade.getOrder().getMember().getMobile())) {
			Map<String, String> model = new HashMap<String, String>();
				model.put("siteName", setting.getSiteName());
				model.put("validatecode", trade.getSn());
				model.put("signature", bundle.getString("signature"));
				if(trade.getOrder().getPhone()!=null&&StringUtils.isNotEmpty(trade.getOrder().getPhone())){
					smsSendService.sendTemplateNoticePool(trade.getOrder().getPhone(), "memberTPLNotice", model);
				}else{
					smsSendService.sendTemplateNoticePool(trade.getOrder().getMember().getMobile(), "memberTPLNotice", model);
				}
				model.clear();
		}
	}

}
