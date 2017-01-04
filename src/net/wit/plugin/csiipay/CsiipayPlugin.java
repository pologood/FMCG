/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.plugin.csiipay;

import java.io.*;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

import javax.net.ssl.*;
import javax.servlet.http.HttpServletRequest;

import net.wit.Setting;
import net.wit.entity.Payment;
import net.wit.entity.PluginConfig;
import net.wit.plugin.PaymentPlugin;
import net.wit.util.SettingUtils;
import net.wit.weixin.util.WeiXinUtils;

import org.apache.commons.lang.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.stereotype.Component;

import com.unionpay.acp.sdk.HttpClient;
import com.unionpay.acp.sdk.SDKConfig;
import com.unionpay.acp.sdk.SDKConstants;
import com.unionpay.acp.sdk.SDKUtil;

/**
 * Plugin - 农商银行支付
 * 
 * @author rsico Team
 * @version 3.0
 */
@Component("csiipayPlugin")
public class CsiipayPlugin extends PaymentPlugin {

	/** 货币 */
	private static final String CURRENCY = "156";

	@Override
	public String getName() {
		return "农商银行支付";
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
		return "csiipay/install.jhtml";
	}

	@Override
	public String getUninstallUrl() {
		return "csiipay/uninstall.jhtml";
	}

	@Override
	public String getSettingUrl() {
		return "csiipay/setting.jhtml";
	}

	@Override
	public String getRequestUrl() {
	return "https://ebank.ahrcu.com:8443/pweb/prePlainPayGateAccess.do";
	}

	@Override
	public RequestMethod getRequestMethod() {
		return RequestMethod.post;
	}

	@Override
	public String getRequestCharset() {
		return "GBK";
	}

	@Override
	public Map<String, Object> getParameterMap(String sn, String description, HttpServletRequest request, String root) {
		PluginConfig pluginConfig = getPluginConfig();
		Payment payment = getPayment(sn);
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		Map<String, Object> parameterMap2 = new HashMap<String, Object>();
		StringBuffer plain=new StringBuffer();
        // 版本号你
		parameterMap.put("transId", "ZF01");
		// 商户号码，请改成自己的商户号,
		parameterMap.put("merchantId",pluginConfig.getAttribute("partner"));
		// 商户订单号，8-40位数字字母
		parameterMap.put("orderId", sn);
		// 交易金额
		parameterMap.put("transAmt", payment.getAmount().multiply(new BigDecimal(100)).intValue());
		// 订单发送时间，取系统时间
		parameterMap.put("transDateTime", new SimpleDateFormat("yyyyMMddHHmmss").format(payment.getCreateDate()));
		// 交易币种
		parameterMap.put("currencyType",CURRENCY);
		// 订货人姓名
		parameterMap.put("customerName", "member");
		// 产品信息
		parameterMap.put("productInfo", "jdh_product");
		// 订货人EMAIL
		parameterMap.put("customerEMail", "30355701@qq.com");
		//附加信息
		parameterMap.put("msgExt", "vsstoo");
		// 后台通知地址
		parameterMap.put("merURL", this.getNotifyUrl(sn,NotifyMethod.sync, root));

		String plains= generatePlain(parameterMap);
		parameterMap2.put("Plain", plains);
		parameterMap2.put("Signature",generateSign(plains));
		System.out.println(plains);
		System.out.println(generateSign(plains));
		// 交易请求url 从配置文件读取
		return parameterMap2;
	}

	public static Map<String, Object> getAllRequestParam(final HttpServletRequest request) {
		Map<String, Object> res = new HashMap<String, Object>();
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
		Map<String, Object> valideData = new HashMap<String, Object>();
		String plain=request.getParameter("Plain");
		String signature=request.getParameter("Signature");
		if(plain==null||signature==null){
			return false;
		}
		if (!checkSign(plain,signature)) {
			return false;
		}

		String[] array=plain.split("~\\|~");
		for (String s:array){
			String[] array2=s.split("=");
			if(s.endsWith("=")){
				valideData.put(array2[0],"");
				continue;
			}
			valideData.put(array2[0],array2[1]);
		}
		if (pluginConfig.getAttribute("partner").equals(valideData.get("merchantId")) && sn.equals(valideData.get("orderId")) &&"AAAAAAA".equals(valideData.get("respCode"))
				&&payment.getAmount().compareTo(new BigDecimal(valideData.get("transAmt").toString()))==0) {
			return true;
		}
		return false;
	}

	@Override
	public String getNotifyMessage(String sn, NotifyMethod notifyMethod, HttpServletRequest request) {
		return "handler over";
	}

	@Override
	public Integer getTimeout() {
		return 21600;
	}

	/**
	 * 生成签名字符串plain
	 *
	 * @param parameterMap
	 *            参数
	 * @return 签名
	 */
	private String generatePlain(Map<String, ?> parameterMap) {
		String data ="";
		for (Iterator<?> it = parameterMap.entrySet().iterator(); it.hasNext();) {
			Entry<String, Object> obj = (Entry<String, Object>) it.next();
			if(obj.getValue()==null){
				continue;
			}
			String value = obj.getValue().toString();
			if (StringUtils.isNotBlank(value)) {
				if (!data.equals("") ) {
					data = data + "~|~";
				}
				data = data +obj.getKey()+"="+value.trim();
			}
		}
		/**
		 * 签名
		 */
		return data;
	}

	/**
	 * 生成签名
	 *
	 *            参数
	 * @return 签名
	 */
	private String generateSign(String plain) {
		/**
		 * 生成签名
		 */
		return com.csii.payment.client.core.ArcuMerchantSignVerify.merchantSignData_ABA(plain);
	}

	/**
	 * 验证签名
	 * @param plain
	 * @param signature
	 * @return
	 */
	private Boolean checkSign(String plain,String signature) {
		/**
		 * 验证签名
		 */
		return com.csii.payment.client.core.ArcuMerchantSignVerify.merchantVerifyPayGate(signature,plain);
	}
	
	/**
	 * 查询订单的支付结果  0000成功  9999处理中  其他的失败 
	 */
	@Override
    public String queryOrder(Payment payment) {
//		PluginConfig pluginConfig = getPluginConfig();
//		SDKConfig.getConfig().loadPropertiesFromSrc();// 从classpath加载acp_sdk.properties文件
//
//		Map<String, Object> data = new HashMap<String, Object>();
//		Map<String, Object> data2 = new HashMap<String, Object>();
//
//		// 版本号
//		data.put("transId", "ZF15");
//		// 商户号码，请改成自己的商户号
//		data.put("merchantId",pluginConfig.getAttribute("partner"));
//		// 商户订单号，8-40位数字字母
//		data.put("originalorderId", payment.getSn());
//		// 订单发送时间，取系统时间
//		data.put("originalTransDateTime", new SimpleDateFormat("yyyyMMddHHmmss").format(payment.getCreateDate()));
//		// 交易金额
//		data.put("originalTransAmt", payment.getAmount().multiply(new BigDecimal(100)));
//		String plain=generatePlain(data);
//		data2.put("Plain",plain.toString());
//		data2.put("Signature",generateSign(plain).toString());
//
//		String result = null;
//		try {
//			result = httpsPost("https://ebank.ahrcu.com:8443/eweb/backOnline.do", data2);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		System.out.println(result);
//		String[] array=result.split("~\\|~");
//		Map<String,String> map=new HashMap<String,String>();
//		for (String s:array){
//			if(s.startsWith("transStatus")){
//				String array2[]=s.split("=");
//				map.put(array2[0].toString(),array2[1].toString());
//				if(array2[0].toString().equals("transStatus")&&array2[1].toString().equals("00")){
//					return "0000";
//				}else{
//					return "9999";
//				}
//			};
//		}
		return "9999";
		
	}
	private static class TrustAnyTrustManager implements X509TrustManager {
		public void checkClientTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
		}
		public void checkServerTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
		}
		public X509Certificate[] getAcceptedIssuers() {
			return new X509Certificate[] {};
		}
	}

	private static class TrustAnyHostnameVerifier implements HostnameVerifier {
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	}
	/**
	 * 参数编码
	 */
	public static String httpBuildQuery(Map<String, String> data) {
		String ret = "";
		String k, v;
		Iterator<String> iterator = data.keySet().iterator();
		while (iterator.hasNext()) {
			k = iterator.next();
			v = data.get(k);
			try {
				ret += URLEncoder.encode(k, "utf8") + "=" + URLEncoder.encode(v, "utf8");
			} catch (UnsupportedEncodingException e) {
			}
			ret += "&";
		}
		return ret.substring(0, ret.length() - 1);
	}
	public static String httpsPost(String url0,Map data) throws Exception {

		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, new TrustManager[] { new TrustAnyTrustManager() },
				new java.security.SecureRandom());
		URL console = new URL(url0);
		HttpsURLConnection conn = (HttpsURLConnection) console.openConnection();
		conn.setSSLSocketFactory(sc.getSocketFactory());
		conn.setHostnameVerifier(new TrustAnyHostnameVerifier());
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

		conn.setDoInput(true);
		conn.setDoOutput(true);

		PrintWriter writer = new PrintWriter(conn.getOutputStream());
		writer.print(httpBuildQuery(data));
		writer.flush();
		writer.close();
		String line;
		BufferedReader bufferedReader;
		StringBuilder sb = new StringBuilder();
		InputStreamReader streamReader = null;
		try {
			streamReader = new InputStreamReader(conn.getInputStream(), "UTF-8");
		} catch (IOException e) {
			streamReader = new InputStreamReader(conn.getErrorStream(), "UTF-8");
		} finally {
			if (streamReader != null) {
				bufferedReader = new BufferedReader(streamReader);
				sb = new StringBuilder();
				while ((line = bufferedReader.readLine()) != null) {
					sb.append(line);
				}
			}
		}
		return sb.toString();
	}
}