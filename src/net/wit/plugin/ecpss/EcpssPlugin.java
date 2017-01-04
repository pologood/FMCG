/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.plugin.ecpss;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import net.wit.entity.Payment;
import net.wit.entity.PluginConfig;
import net.wit.plugin.PaymentPlugin;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

/**
 * Plugin - 汇潮支付
 * 
 * @author rsico Team
 * @version 3.0
 */
@Component("ecpssPlugin")
public class EcpssPlugin extends PaymentPlugin {

	/** 默认银行 */
	private static final String DEFAULT_BANK = "ICBC";

	/** 银行参数名称 */
	public static final String BANK_PARAMETER_NAME = "bank";

	@Override
	public String getName() {
		return "汇潮支付";
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
		return "ecpss/install.jhtml";
	}

	@Override
	public String getUninstallUrl() {
		return "ecpss/uninstall.jhtml";
	}

	@Override
	public String getSettingUrl() {
		return "ecpss/setting.jhtml";
	}

	@Override
	public String getRequestUrl() {
		return "https://pay.ecpss.com/sslpayment";
	}

	@Override
	public RequestMethod getRequestMethod() {
		return RequestMethod.get;
	}

	@Override
	public String getRequestCharset() {
		return "UTF-8";
	}

	/**
	 * 连接Map值
	 * 
	 * @param map
	 *            Map
	 * @param prefix
	 *            前缀
	 * @param suffix
	 *            后缀
	 * @param separator
	 *            连接符
	 * @param ignoreEmptyValue
	 *            忽略空值
	 * @param ignoreKeys
	 *            忽略Key
	 * @return 字符串
	 */
	@Override
	protected String joinValue(Map<String, Object> map, String prefix, String suffix, String separator, boolean ignoreEmptyValue, String... ignoreKeys) {
		List<String> list = new ArrayList<String>();
		if (map != null) {
			for (Entry<String, Object> entry : map.entrySet()) {
				String key = entry.getKey();
				String value = ConvertUtils.convert(entry.getValue());
				if (StringUtils.isNotEmpty(key) && !ArrayUtils.contains(ignoreKeys, key) && (!ignoreEmptyValue || StringUtils.isNotEmpty(value))) {
					list.add(value != null ? value : "");
				}
			}
		}
		return (prefix != null ? prefix : "") + StringUtils.join(list, separator) + (suffix != null ? suffix : "");
	}

	@Override
	public Map<String, Object> getParameterMap(String sn, String description, HttpServletRequest request, String root) {
		PluginConfig pluginConfig = getPluginConfig();
		Payment payment = getPayment(sn);
		Map<String, Object> parameterMap = new LinkedHashMap<String, Object>();
		parameterMap.put("MerNo", pluginConfig.getAttribute("partner"));
		parameterMap.put("BillNo", sn);
		parameterMap.put("Amount", payment.getAmount().setScale(2).toString());
		parameterMap.put("ReturnURL", getNotifyUrl(sn, NotifyMethod.sync,root));
		parameterMap.put("SignInfo", generateSign(parameterMap));
		
		parameterMap.put("Remark", "睿商圈交易款");
		parameterMap.put("products", "睿商圈交易款");
		parameterMap.put("AdviceURL", getNotifyUrl(sn, NotifyMethod.async,root));
		String bank = request.getParameter(BANK_PARAMETER_NAME);
		parameterMap.put("defaultBankNumber", StringUtils.isNotEmpty(bank) ? bank : DEFAULT_BANK);
		SimpleDateFormat orderTime =new SimpleDateFormat("yyyyMMddHHmmss");
		parameterMap.put("orderTime",orderTime.format(new Date()));
		return parameterMap;
	}

	@Override
	public boolean verifyNotify(String sn, NotifyMethod notifyMethod, HttpServletRequest request) {
		Payment payment = getPayment(sn);
		Map<String, Object> parameterMap = new LinkedHashMap<String, Object>();
		parameterMap.put("BillNo", request.getParameter("BillNo"));
		parameterMap.put("Amount", request.getParameter("Amount"));
		parameterMap.put("Succeed", request.getParameter("Succeed"));
		PluginConfig pluginConfig = getPluginConfig();
		String sign = DigestUtils.md5Hex(joinValue(parameterMap, null, pluginConfig.getAttribute("key"), "", true)).toUpperCase();
		if (sign.equals(request.getParameter("MD5info")) && 
			sn.equals(request.getParameter("BillNo")) && 
			"88".equals(request.getParameter("Succeed")) && 
			payment.getAmount().compareTo(new BigDecimal(request.getParameter("Amount"))) == 0
		   ) 
		{
			return true;
		}
		return false;
	}

	@Override
	public String getNotifyMessage(String sn, NotifyMethod notifyMethod, HttpServletRequest request) {
		if (notifyMethod == NotifyMethod.async) {
			return "ok";
		}
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
	private String generateSign(Map<String, Object> parameterMap) {
		PluginConfig pluginConfig = getPluginConfig();
		return DigestUtils.md5Hex(joinValue(parameterMap, null, "&"+pluginConfig.getAttribute("key"), "&", true)).toUpperCase();
	}

}