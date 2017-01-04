/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.plugin.chinapay.mobile;

import java.io.StringReader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import net.wit.entity.Payment;
import net.wit.entity.PluginConfig;
import net.wit.plugin.PaymentPlugin;

import org.apache.commons.codec.digest.DigestUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.springframework.stereotype.Component;
import org.xml.sax.InputSource;

/**
 * Plugin - 银联手机支付
 * 
 * @author rsico Team
 * @version 3.0
 */
@Component("chinapayMobilePlugin")
public class ChinapayMobilePlugin extends PaymentPlugin {

	/** 货币 */
	private static final String CURRENCY = "156";

	@Override
	public String getName() {
		return "ChinaPayMobile";
	}

	@Override
	public String getVersion() {
		return "1.0";
	}

	@Override
	public String getAuthor() {
		return "rsico";
	}

	@Override
	public String getSiteUrl() {
		return "http://www.rsico.cn";
	}

	@Override
	public String getInstallUrl() {
		return "chinapay/mobile/install.jhtml";
	}

	@Override
	public String getUninstallUrl() {
		return "chinapay/mobile/uninstall.jhtml";
	}

	@Override
	public String getSettingUrl() {
		return "chinapay/mobile/setting.jhtml";
	}

	@Override
	public String getRequestUrl() {
//		return "http://www.sinoqy.com:8080/qyapi/trans/getTn";
		return "http://mpay.sinoqy.com:6283/qyapi/trans/getTn";
	}

	@Override
	public RequestMethod getRequestMethod() {
		return RequestMethod.get;
	}

	@Override
	public String getRequestCharset() {
		return "UTF-8";
	}

	@Override
	public Map<String, Object> getParameterMap(String sn, String description, HttpServletRequest request, String root) {
		PluginConfig pluginConfig = getPluginConfig();
		Payment payment = getPayment(sn);
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put("merchantNO", pluginConfig.getAttribute("partner"));
		parameterMap.put("orderNO", sn);
 		parameterMap.put("orderAmount", payment.getAmount().multiply(new BigDecimal(100)).setScale(0).toString());
		parameterMap.put("backUrl", getNotifyUrl(sn, NotifyMethod.async,root));
		parameterMap.put("sign",DigestUtils.md5Hex(pluginConfig.getAttribute("partner")+"&"+sn+"&"+payment.getAmount().multiply(new BigDecimal(100)).setScale(0).toString()+"&"+getNotifyUrl(sn, NotifyMethod.async,root)+"&"+pluginConfig.getAttribute("key")));
		return parameterMap;
	}
	 
	@SuppressWarnings("unchecked")
	@Override
	public boolean verifyNotify(String sn, NotifyMethod notifyMethod, HttpServletRequest request) {
		PluginConfig pluginConfig = getPluginConfig();
		Payment payment = getPayment(sn);
		String resp = request.getParameter("resp");
		try {
			Map map = doXMLParse(resp);
		    String qyOrderNO = (String) map.get("qyOrderNO");
		    String orderAmount = (String) map.get("orderAmount");
		    String orderNO = (String) map.get("orderNO");
		    String qyResultCode = (String) map.get("qyResultCode");
		    String sign = (String) map.get("sign");
		    
		    String mysign = DigestUtils.md5Hex(pluginConfig.getAttribute("partner")+"&"+qyOrderNO+"&"+pluginConfig.getAttribute("key")+"&"+qyResultCode);
		    
			if (mysign.equals(sign) && sn.equals(orderNO) && "000000000".equals(qyResultCode) && payment.getAmount().multiply(new BigDecimal(100)).compareTo(new BigDecimal(orderAmount)) == 0) {
				return true;
			}

		} catch (Exception e) {
	    	return false;
        }
		return false;
	}

	public static String getChildrenText(List children) {
		StringBuffer sb = new StringBuffer();
		if (!children.isEmpty()) {
			Iterator it = children.iterator();
			while (it.hasNext()) {
				Element e = (Element) it.next();
				String name = e.getName();
				String value = e.getTextNormalize();
				List list = e.getChildren();
				sb.append("<" + name + ">");
				if (!list.isEmpty()) {
					sb.append(getChildrenText(list));
				}
				sb.append(value);
				sb.append("</" + name + ">");
			}
		}

		return sb.toString();
	}

	public HashMap<String,Object> doXMLParse(String strxml) throws Exception {
		if (null == strxml || "".equals(strxml)) {
			return null;
		}

		HashMap<String,Object> m = new HashMap<String,Object>();
		StringReader sr = new StringReader(strxml);
		InputSource is = new InputSource(sr);
		Document doc = (new SAXBuilder()).build(is);
		Element root = doc.getRootElement();
		List list = root.getChildren();
		Iterator it = list.iterator();
		while (it.hasNext()) {
			Element e = (Element) it.next();
			String k = e.getName();
			String v = "";
			List children = e.getChildren();
			if (children.isEmpty()) {
				v = e.getTextNormalize();
			} else {
				v = getChildrenText(children);
			}

			m.put(k, v);
		}

		// 关闭流

		return m;
	}
		
	/**
	 * 查询订单的支付结果  0000成功  9999处理中  其他的失败 
	 */
	@Override
    public String queryOrder(Payment payment) {
		if (payment.getPaySn()==null) {
			return "0001";
		}
		PluginConfig pluginConfig = getPluginConfig();
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put("qyOrderNO",payment.getPaySn());
		parameterMap.put("sign",DigestUtils.md5Hex(pluginConfig.getAttribute("partner")+"&"+payment.getPaySn()+"&"+pluginConfig.getAttribute("key")));
		String resp = "";
		try {
		    resp = get("http://mpay.sinoqy.com:6283/qyapi/query",parameterMap);
		} catch (Exception e) {
			e.printStackTrace();
			return "0001";
		}
		
		try {
			Map map = doXMLParse(resp);
			String resultCode = (String) map.get("qyResultCode");
			if (resultCode==null) {
				return "0001";
			}
			if (resultCode.equals("000000000")) {
				return "0000";
			} else if (resultCode.equals("200010000")) {
				return "9999";
			} else if (resultCode.equals("211000000")) {
				return "9999";
			} else {
				return "0001";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "0001";
		}
		
	}
	
	@Override
	public String getNotifyMessage(String sn, NotifyMethod notifyMethod, HttpServletRequest request) {
		return "0000";
	}

	@Override
	public Integer getTimeout() {
		return 21600;
	}

 	/**
	 * 生成签名
	 * 
	 * @param parameterMap
	 *            参数
	 * @return 签名
	 */
	public String generateSign(Map<String, ?> parameterMap) {
		PluginConfig pluginConfig = getPluginConfig();
        return DigestUtils.md5Hex(joinValue(new TreeMap<String, Object>(parameterMap), null,"&"+pluginConfig.getAttribute("key"), "&", true, "sign"));
	}

}