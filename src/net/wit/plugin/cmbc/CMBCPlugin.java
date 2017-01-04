/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.plugin.cmbc;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.springframework.stereotype.Component;

import net.wit.Setting;
import net.wit.entity.Payment;
import net.wit.entity.PluginConfig;
import net.wit.plugin.PaymentPlugin;
import net.wit.util.SettingUtils;

/**
 * Plugin - 民生E支付
 * 
 * @author rsico Team
 * @version 3.0
 */
@Component("cmbcPlugin")
public class CMBCPlugin extends PaymentPlugin {

	/** 货币 */
	private static final String CURRENCY = "156";

	@Override
	public String getName() {
		return "民生E支付";
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
		return "http://www.cmbc.com";
	}

	@Override
	public String getInstallUrl() {
		return "cmbc/install.jhtml";
	}

	@Override
	public String getUninstallUrl() {
		return "cmbc/uninstall.jhtml";
	}

	@Override
	public String getSettingUrl() {
		return "cmbc/setting.jhtml";
	}

	@Override
	public String getRequestUrl() {
		return "https://epay.cmbc.com.cn/ipad/service.html";
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
		Setting setting = SettingUtils.get();
		PluginConfig pluginConfig = getPluginConfig();
		Payment payment = getPayment(sn);
		Map<String, Object> map = new HashMap<String, Object>();
				
		//1. 参数准备
		String service = "create_direct_pay_by_user";
		String partner_id = pluginConfig.getAttribute("partner");
		String input_charset = "utf-8";	//固定值
		String sign_type = "MD5";	//固定值
		String out_trade_no = sn;	//保证每次生成订单号不一样
	    DecimalFormat df=(DecimalFormat)NumberFormat.getInstance(); 
	    df.setGroupingUsed(false);
  	    df.setMaximumFractionDigits(2); 
		String amount = df.format(payment.getAmount());	//单位为元
		String payMethod = "";//"bankPay";	//固定值，注意大小写
		String seller_email = pluginConfig.getAttribute("email");
		String notify_url = this.getNotifyUrl(sn,NotifyMethod.async, root);	//商户自定义url,不能为空
		String subject = description;	//不能为空，否则报“商品名称格式不正确 ”
		String buyer_email = "";//不填
		String body = description;	//不能为空，否则报“商品名称格式不正确 ”
		String show_url = "";//不填
		String default_bank = "";//填写为“BANK_CODE”和“PRO_CODE”拼接字符串（字典参见接口文档8附录）。如个人网银产品的工商银行，则此字段输入：200304。如果为空则默认在e支付平台选择支付银行
		String return_url = this.getNotifyUrl(sn,NotifyMethod.sync, root);//不能为空，否则报“返回地址url格式不正确”
		String royalty_parameters = "";//不填
		
		//请联系客户经理索要商户签约密钥key
		String key = pluginConfig.getAttribute("key");
		String sign = "";

		map.put("service", service);
		map.put("partner_id", partner_id);
		map.put("input_charset", input_charset);
		map.put("sign_type", sign_type);
		map.put("out_trade_no", out_trade_no);
		map.put("amount", amount);
		map.put("payMethod", payMethod);
		map.put("seller_email", seller_email);
		map.put("notify_url", notify_url);
		map.put("subject", subject);
		map.put("buyer_email", buyer_email);
		map.put("body", body);
		map.put("show_url", show_url);
		map.put("default_bank", default_bank);
		map.put("return_url", return_url);
		map.put("royalty_parameters", royalty_parameters);
		
		//2. 获得签名字符串
		if (StringUtils.isNotEmpty(key)) {
			sign = sign(map, key);
			map.put("sign", sign);
		}
		
		// 交易请求url 从配置文件读取
		return map;
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
				System.out.println(en+"="+value);
			}
		}
		return res;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean verifyNotify(String sn, NotifyMethod notifyMethod, HttpServletRequest request) {
		PluginConfig pluginConfig = getPluginConfig();
		Payment payment = getPayment(sn);	
		System.out.println("notify="+notifyMethod);
		Map<String, Object> resultMap = getAllRequestParam(request);
		boolean isValid = false;
		if (resultMap.containsKey("sign")) {
	    	String sign = resultMap.get("sign").toString();
		    resultMap.remove("sign");
		    isValid = checkSign(resultMap, pluginConfig.getAttribute("key"), sign);
		} else {
	    	String md5_sign = resultMap.get("md5_sign").toString();
		    resultMap.remove("md5_sign");
		    isValid = checkSign(resultMap, pluginConfig.getAttribute("key"), md5_sign);
		}
		if  (isValid && sn.equals(request.getParameter("out_trade_no")) && 
			("TRADE_PAYED".equals(request.getParameter("trade_status")) || "TRADE_FINISHED".equals(request.getParameter("trade_status")) ) 
			&& payment.getAmount().compareTo(new BigDecimal(request.getParameter("total_fee"))) == 0) {
			return true;
		}
		return false;
	}

	@Override
	public String getNotifyMessage(String sn, NotifyMethod notifyMethod, HttpServletRequest request) {
	    if (notifyMethod.equals(NotifyMethod.async)) {
		    return "true";
	    } else 
	    {
	    	return null;
	    }
	}

	@Override
	public Integer getTimeout() {
		return 21600;
	}
	
    public static String mapToUrl(Map<String, String> map) {
        Iterator<Entry<String, String>> it = map.entrySet().iterator();
        StringBuffer sb = new StringBuffer();
        while (it != null && it.hasNext()) {
            Entry<String, String> entry = it.next();
            if (StringUtils.isNotBlank(entry.getKey())) {
                sb.append(entry.getKey());
                sb.append("=");
                if (entry.getKey().equals("out_trade_no")) {
                	try {
						sb.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
						sb.append("&");
						continue;
					} catch (UnsupportedEncodingException e) {
					}
				}
                sb.append(entry.getValue());
                sb.append("&");
            }
        }
        String urlTemp = sb.toString();
        if (StringUtils.isNotBlank(urlTemp)) {
            return StringUtils.substring(urlTemp, 0, urlTemp.length() - 1);
        }

        return null;
    }

	
    /**
     * Used building output as Hex
     */
    private static final char[] DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a',
            'b', 'c', 'd', 'e', 'f'   };

    public static String md5(String text) {
        MessageDigest msgDigest = null;

        try {
            msgDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("System doesn't support MD5 algorithm.");
        }

        try {
            msgDigest.update(text.getBytes("utf-8"));

        } catch (UnsupportedEncodingException e) {

            throw new IllegalStateException("System doesn't support your  EncodingException.");

        }

        byte[] bytes = msgDigest.digest();

        String md5Str = new String(encodeHex(bytes));

        return md5Str;
    }

    public static char[] encodeHex(byte[] data) {

        int l = data.length;

        char[] out = new char[l << 1];

        // two characters form the hex value.
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = DIGITS[(0xF0 & data[i]) >>> 4];
            out[j++] = DIGITS[0x0F & data[i]];
        }
        return out;
    }

    @SuppressWarnings("unchecked")
	private static String getSignatureContent(Properties properties) {
        StringBuffer content = new StringBuffer();
        List keys = new ArrayList(properties.keySet());
        Collections.sort(keys);
        for (int i = 0; i < keys.size(); i++) {
            String key = (String) keys.get(i);
            String value = properties.getProperty(key);
            content.append((i == 0 ? "" : "&") + key + "=" + value);
        }
        return content.toString();
    }

    public static String sign(String content, String privateKey) {
        if (StringUtils.isBlank(privateKey)) {
            return null;
        }
        String signBefore = content + privateKey;
         return md5(signBefore);
    }
	
    public static String sign(Map<String, Object> params, String key) {
        if (null != params && StringUtils.isNotBlank(key)) {
            Properties properties = new Properties();
            for (Iterator<String> iter = params.keySet().iterator(); iter.hasNext();) {
                String name = (String) iter.next();
                String value = params.get(name).toString();
				System.out.println(name + " = " + value);
               
                if (StringUtils.isBlank(name) || StringUtils.isBlank(value)
                    || StringUtils.equalsIgnoreCase("sign", name) || StringUtils.equalsIgnoreCase("md5_sign", name)) {
                    continue;
                }
                properties.setProperty(name, StringUtils.trim(value));
            }
            String content = getSignatureContent(properties);
             return sign(content, key);
        }
        return null;
    }

    public static boolean checkSign(Map<String, Object> params, String key, String sign) {
        if (null != params && StringUtils.isNotBlank(key) && StringUtils.isNotBlank(sign)) {

            String signed = sign(params, key);
            if (StringUtils.equalsIgnoreCase(signed, sign)) {
                return true;
            }
        }
        return false;
    }

    public static Map<String, Object> getParamMapFromReturnUrl(String returlUrl) throws UnsupportedEncodingException{
    	Map<String, Object> paramMap = new HashMap<String, Object>();
    	int splitIndex = returlUrl.indexOf("?");
    	if(splitIndex == -1){
    		System.out.println("returnUrl格式不正确:" + returlUrl);
    		return null;
    	}
    	returlUrl = returlUrl.substring(splitIndex + 1);		
		String[] tokens = returlUrl.split("&");
		for(String token : tokens){
			String key = token.substring(0, token.indexOf("="));
			String value = token.substring(token.indexOf("=") + 1);
			
			if(key.equals("md5_sign") || key.equals("sign")){	//签名字段无需做url_decode
				paramMap.put(key, value);
			}else{	//其他字段需做url_decode
				value = URLDecoder.decode(value, "UTF-8");
				paramMap.put(key, value);
			}
			System.out.println(key + " == " + value);
		}
		paramMap.remove("payMethod");
		return paramMap;
    }
	/**
	 * 查询订单的支付结果  0000成功  9999处理中  其他的失败 
	 */
	@Override
    public String queryOrder(Payment payment) {
		PluginConfig pluginConfig = getPluginConfig();
		
		//1. 参数准备
		String service = "query_pay_by_platform";
		String partner_id = pluginConfig.getAttribute("partner");
		String input_charset = "utf-8";
		String sign_type = "MD5";
		String out_trade_no = payment.getSn();	//原支付订单号
		String return_url = this.getNotifyUrl(payment.getSn(),NotifyMethod.async, "/b2b");//不能为空，否则报“返回地址url格式不正确”
		
		//请联系客户经理索要商户签约密钥key
		String key =  pluginConfig.getAttribute("key");
		String sign = "";

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("service", service);
		map.put("partner_id", partner_id);
		map.put("input_charset", input_charset);
		map.put("sign_type", sign_type);
		map.put("out_trade_no", out_trade_no);
		map.put("return_url", return_url);
		
		//2. 获得签名字符串
		if (StringUtils.isNotEmpty(key)) {
			sign = sign(map, key);
			map.put("sign", sign);
			System.out.println("sign == " + sign);
		}else{
			return "9999";
		}
		
		Set entrySet = map.entrySet();
		Iterator it = entrySet.iterator();
		StringBuffer tailString = new StringBuffer();
		while(it.hasNext()){
			Entry<String, String> entry = (Entry<String, String>)it.next();
			tailString.append(entry.getKey());
			tailString.append("=");
			try {
				tailString.append(URLEncoder.encode(entry.getValue(), "utf-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "9999";
			}
			tailString.append("&");
		}
		
		tailString.deleteCharAt(tailString.length() - 1);//去掉最后一个&符号	
		
		String serviceUrl = getRequestUrl() + "?" + tailString.toString();
		
		String retMsg=null;
		try {
			retMsg = sendPostRequest(getRequestUrl(), map);
		} catch (ClientProtocolException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return "9999";
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return "9999";
		}

		if(retMsg == null || retMsg.equals("")){
			System.out.println("用户签名失败或订单不存在");
			return "9999";
		}else{
			System.out.println("查询订单信息：" + retMsg);
		}
		
		//5. 验证返回签名
		Map<String, Object> queryResultMap;
		try {
			queryResultMap = getParamMapFromReturnUrl(retMsg);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "9999";
		}
		String md5_sign = queryResultMap.get("sign").toString();
		queryResultMap.remove("sign");
		boolean isValid = checkSign(queryResultMap, key, md5_sign);
		if(isValid){
			System.out.println("验证成功");
		}else{
			System.out.println("验证失败");
			return "9999";
		}
		
		String trade_status = queryResultMap.get("trade_status").toString();
		if  (isValid  && 
				("TRADE_PAYED".equals(trade_status) || "TRADE_FINISHED".equals(trade_status) ) 
			) {
				return "0000";
			}
		else {
		     return "0001";
		}
	}
	
	
	public static String sendPostRequest(String  reqURL, Map<String, Object> params) throws ClientProtocolException, IOException{
		DefaultHttpClient httpClient = new DefaultHttpClient();
	
		
		HttpPost post = setPostParam(reqURL, params,"application/x-www-form-urlencoded; text/html; charset=utf-8");
		post.getParams().setBooleanParameter(
				CoreProtocolPNames.USE_EXPECT_CONTINUE, false);
		HttpResponse response = httpClient.execute(post);
		HttpEntity entity = response.getEntity();             //获取响应实体 
        
        long responseLength = 0;
		if (null != entity) { 
            responseLength = entity.getContentLength();
        } 
        if(responseLength == -1){
        	return null;
        }
        Header[] location = response.getHeaders("Location");
        return location[0].getValue();
	}
	

	
	public static HttpPost setPostParam(String queryUrl,Map<String, Object> queryMap, String contentType){
		String url = queryUrl;
		HttpPost post = new HttpPost(url);
		post.setHeader("Content-Type", contentType);	
		// 设置参数的集合
		List<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();
		// 设置参数，格式是name:value
		if (null != queryMap && 0 != queryMap.size()) {
			// 填入各个表单域的值
			for (String key : queryMap.keySet()) {
				String val = queryMap.get(key).toString();
				if (StringUtils.isNotBlank(key) && StringUtils.isNotBlank(val)) {
					nvps.add(new BasicNameValuePair(key, val));
				}
			}
		}
		
		// 对参数实体进行格式转换
		UrlEncodedFormEntity urlEntity = null;
		try {
			urlEntity = new UrlEncodedFormEntity(nvps,
					"utf-8");
//			urlEntity.setContentType("application/x-www-form-urlencoded");
		} catch (UnsupportedEncodingException e1) {
			//log.error("系统不支持该编码集:");
		}
		// 设置实体
		post.setEntity(urlEntity);
		return post;
		
	}
	
}