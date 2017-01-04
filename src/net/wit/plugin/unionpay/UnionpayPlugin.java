/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.plugin.unionpay;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import net.wit.Setting;
import net.wit.entity.Payment;
import net.wit.entity.PluginConfig;
import net.wit.plugin.PaymentPlugin;
import net.wit.util.SettingUtils;
import net.wit.weixin.util.WeiXinUtils;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.unionpay.acp.sdk.HttpClient;
import com.unionpay.acp.sdk.SDKConfig;
import com.unionpay.acp.sdk.SDKConstants;
import com.unionpay.acp.sdk.SDKUtil;

/**
 * Plugin - 银联在线支付
 * 
 * @author rsico Team
 * @version 3.0
 */
@Component("unionpayPlugin")
public class UnionpayPlugin extends PaymentPlugin {

	/** 货币 */
	private static final String CURRENCY = "156";

	@Override
	public String getName() {
		return "银联在线支付";
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
		return "unionpay/install.jhtml";
	}

	@Override
	public String getUninstallUrl() {
		return "unionpay/uninstall.jhtml";
	}

	@Override
	public String getSettingUrl() {
		return "unionpay/setting.jhtml";
	}

	@Override
	public String getRequestUrl() {
		SDKConfig.getConfig().loadPropertiesFromSrc();// 从classpath加载acp_sdk.properties文件
		return SDKConfig.getConfig().getFrontRequestUrl();
		//return "https://unionpaysecure.com/api/Pay.action";
	}

	@Override
	public RequestMethod getRequestMethod() {
		return RequestMethod.post;
	}

	@Override
	public String getRequestCharset() {
		return "UTF-8";
	}

	@Override
	public Map<String, Object> getParameterMap(String sn, String description, HttpServletRequest request, String root) {
		SDKConfig.getConfig().loadPropertiesFromSrc();// 从classpath加载acp_sdk.properties文件
		Setting setting = SettingUtils.get();
		PluginConfig pluginConfig = getPluginConfig();
		Payment payment = getPayment(sn);
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		Map<String, String> data = new HashMap<String, String>();
		// 版本号
		parameterMap.put("version", "5.0.0");
		// 字符集编码 默认"UTF-8"
		parameterMap.put("encoding", "UTF-8");
		// 签名方法 01 RSA
		parameterMap.put("signMethod", "01");
		// 交易类型 01-消费
		parameterMap.put("txnType", "01");
		// 交易子类型 01:自助消费 02:订购 03:分期付款
		parameterMap.put("txnSubType", "01");
		// 业务类型
		parameterMap.put("bizType", "000201");
		// 渠道类型，07-PC，08-手机
		parameterMap.put("channelType", "07");
		// 前台通知地址 ，控件接入方式无作用
		parameterMap.put("frontUrl",this.getNotifyUrl(sn,NotifyMethod.sync, root));
		// 后台通知地址
		parameterMap.put("backUrl", this.getNotifyUrl(sn,NotifyMethod.async, root));
		// 接入类型，商户接入填0 0- 商户 ， 1： 收单， 2：平台商户
		parameterMap.put("accessType", "0");
		// 商户号码，请改成自己的商户号
		parameterMap.put("merId",pluginConfig.getAttribute("partner"));
		// 商户订单号，8-40位数字字母
		parameterMap.put("orderId", sn);
		// 订单发送时间，取系统时间
		parameterMap.put("txnTime", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		// 交易金额，单位分
		parameterMap.put("txnAmt", payment.getAmount().multiply(new BigDecimal(100)).setScale(0).toString());
		// 交易币种
		parameterMap.put("currencyCode",CURRENCY);
		// 请求方保留域，透传字段，查询、通知、对账文件中均会原样出现
		// data.put("reqReserved", "透传信息");
		// 订单描述，可不上送，上送时控件中会显示该信息
		// data.put("orderDesc", "订单描述");

		Map<String, String> submitFromData = generateSign(parameterMap);
		parameterMap.put("signature", submitFromData.get("signature"));
		parameterMap.put("certId", submitFromData.get("certId"));
		// 交易请求url 从配置文件读取
		return parameterMap;
	}

	public static Map<String, String> getAllRequestParam(final HttpServletRequest request) {
		Map<String, String> res = new HashMap<String, String>();
		Enumeration<?> temp = request.getParameterNames();
		if (null != temp) {
			while (temp.hasMoreElements()) {
				String en = (String) temp.nextElement();
				String value = request.getParameter(en);
				res.put(en, value);
				//在报文上送时，如果字段的值为空，则不上送<下面的处理为在获取所有参数数据时，判断若值为空，则删除这个字段>
				//System.out.println("ServletUtil类247行  temp数据的键=="+en+"     值==="+value);
				if (null == res.get(en) || "".equals(res.get(en))) {
					res.remove(en);
				}
			}
		}
		return res;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean verifyNotify(String sn, NotifyMethod notifyMethod, HttpServletRequest request) {
		PluginConfig pluginConfig = getPluginConfig();
		Payment payment = getPayment(sn);
		SDKConfig.getConfig().loadPropertiesFromSrc();
		try {
			request.setCharacterEncoding("ISO-8859-1");
			String encoding = request.getParameter(SDKConstants.param_encoding);
			// 获取请求参数中所有的信息
			Map<String, String> reqParam = getAllRequestParam(request);

			Map<String, String> valideData = null;
			if (null != reqParam && !reqParam.isEmpty()) {
				Iterator<Entry<String, String>> it = reqParam.entrySet().iterator();
				valideData = new HashMap<String, String>(reqParam.size());
				while (it.hasNext()) {
					Entry<String, String> e = it.next();
					String key = (String) e.getKey();
					String value = (String) e.getValue();
					value = new String(value.getBytes("ISO-8859-1"), encoding);
					valideData.put(key, value);
				}
			}
			
			// 验证签名
			if (!SDKUtil.validate(valideData, encoding)) {
				return false;
			}
			
			
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return false;
		}

		if (pluginConfig.getAttribute("partner").equals(request.getParameter("merId")) && sn.equals(request.getParameter("orderId")) && "00".equals(request.getParameter("respCode"))
				&& payment.getAmount().multiply(new BigDecimal(100)).compareTo(new BigDecimal(request.getParameter("txnAmt"))) == 0) {
			return true;
		}
		return false;
	}

	@Override
	public String getNotifyMessage(String sn, NotifyMethod notifyMethod, HttpServletRequest request) {
		return null;
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
	private Map<String, String> generateSign(Map<String, ?> parameterMap) {
		Entry<String, String> obj = null;
		Map<String, String> submitFromData = new HashMap<String, String>();
		for (Iterator<?> it = parameterMap.entrySet().iterator(); it.hasNext();) {
			obj = (Entry<String, String>) it.next();
			String value = obj.getValue();
			if (StringUtils.isNotBlank(value)) {
				// 对value值进行去除前后空处理
				submitFromData.put(obj.getKey(), value.trim());
			}
		}
		/**
		 * 签名
		 */
		
		SDKUtil.sign(submitFromData, getRequestCharset());

		return submitFromData;
	}

	public Map<String, String> submitUrl(Map<String, String> submitFromData,String requestUrl) {
		String resultString = "";
		//System.out.println("requestUrl====" + requestUrl);
		//System.out.println("submitFromData====" + submitFromData.toString());
		/**
		 * 发送
		 */
		HttpClient hc = new HttpClient(requestUrl, 30000, 30000);
		try {
			int status = hc.send(submitFromData, getRequestCharset());
			if (200 == status) {
				resultString = hc.getResult();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Map<String, String> resData = new HashMap<String, String>();
		/**
		 * 验证签名
		 */
		if (null != resultString && !"".equals(resultString)) {
			// 将返回结果转换为map
			resData = SDKUtil.convertResultStringToMap(resultString);
			if (SDKUtil.validate(resData, getRequestCharset())) {
				//System.out.println("验证签名成功");
			} else {
				System.out.println("验证签名失败");
			}
			// 打印返回报文
			System.out.println("打印返回报文：" + resultString);
		}
		return resData;
	}
	/**
	 * 查询订单的支付结果  0000成功  9999处理中  其他的失败 
	 */
	@Override
    public String queryOrder(Payment payment) {
		PluginConfig pluginConfig = getPluginConfig();
		SDKConfig.getConfig().loadPropertiesFromSrc();// 从classpath加载acp_sdk.properties文件

		/**
		 * 组装请求报文
		 */
		Map<String, String> data = new HashMap<String, String>();
		// 版本号
		data.put("version", "5.0.0");
		// 字符集编码 默认"UTF-8"
		data.put("encoding", "UTF-8");
		// 签名方法 01 RSA
		data.put("signMethod", "01");
		// 交易类型 
		data.put("txnType", "00");
		// 交易子类型 
		data.put("txnSubType", "00");
		// 业务类型
		data.put("bizType", "000000");
		// 渠道类型，07-PC，08-手机
		data.put("channelType", "07");
		// 接入类型，商户接入填0 0- 商户 ， 1： 收单， 2：平台商户
		data.put("accessType", "0");
		// 商户号码，请改成自己的商户号
		data.put("merId",pluginConfig.getAttribute("partner"));
		// 商户订单号，请修改被查询的交易的订单号
		data.put("orderId", payment.getSn());
		// 订单发送时间，请修改被查询的交易的订单发送时间
		data.put("txnTime",  new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));

		data = generateSign(data);

		// 交易请求url 从配置文件读取
		String url = SDKConfig.getConfig().getSingleQueryUrl();

		Map<String, String> resmap = submitUrl(data, url);

		//System.out.println("请求报文=["+data.toString()+"]");
		//System.out.println("应答报文=["+resmap.toString()+"]");
		
			String return_code = resmap.get("respCode");
			if (return_code!=null && return_code.equals("00")) {
				String status = resmap.get("origRespCod");
				if (status.equals("00")) {
					return "0000";
				} else {
					return "0001";
				}
			} else {
				return "9999";
			}
		
	}
}