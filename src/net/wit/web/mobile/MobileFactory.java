package net.wit.web.mobile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import net.wit.dao.MobilePriceDao;
import net.wit.entity.MobilePrice;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service("mobileFactory")
public class MobileFactory {

	@Resource(name = "mobilePriceDaoImpl")
	private MobilePriceDao mobilePriceDao;

	public static final String key = "y8ktxfryyskdqv7jr83rm3e3ef9io2g0c385uvrmicy15ce8kecd8gu51m03pmd4wu8qlie6kbonsx6x13nqi3tolcuo35xjr9i8zo1xmyao7mjaej761qa1m7q5ba4w";
	//public static final String key = "111111";
	//public static final String agentId = "test_agent_id_1";
	public static final String agentId = "1386041130";
	
	public String post(String url, Map<String, Object> parameterMap) {
		Assert.hasText(url);
		String result = null;
		HttpClient httpClient = new DefaultHttpClient();
		try {
			HttpPost httpPost = new HttpPost(url);
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			if (parameterMap != null) {
				for (Entry<String, Object> entry : parameterMap.entrySet()) {
					String name = entry.getKey();
					String value = ConvertUtils.convert(entry.getValue());
					if (StringUtils.isNotEmpty(name)) {
						nameValuePairs.add(new BasicNameValuePair(name, value));
					}
				}
			}
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			result = EntityUtils.toString(httpEntity);
			EntityUtils.consume(httpEntity);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
		return result;
	}

	/**
	 * GET请求
	 * @param url URL
	 * @param parameterMap 请求参数
	 * @return 返回结果
	 */
	public String get(String url, Map<String, Object> parameterMap) {
		Assert.hasText(url);
		String result = null;
		@SuppressWarnings("resource")
		HttpClient httpClient = new DefaultHttpClient();
		try {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			if (parameterMap != null) {
				for (Entry<String, Object> entry : parameterMap.entrySet()) {
					String name = entry.getKey();
					String value = ConvertUtils.convert(entry.getValue());
					if (StringUtils.isNotEmpty(name)) {
						nameValuePairs.add(new BasicNameValuePair(name, value));
					}
				}
			}
			HttpGet httpGet = new HttpGet(url + (StringUtils.contains(url, "?") ? "&" : "?") + EntityUtils.toString(new UrlEncodedFormEntity(nameValuePairs, "UTF-8")));
			HttpResponse httpResponse = httpClient.execute(httpGet);
			HttpEntity httpEntity = httpResponse.getEntity();
			result = EntityUtils.toString(httpEntity);
			EntityUtils.consume(httpEntity);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
		return result;
	}

	public String getFillError(String errCode) {
		if (errCode.equals("0000")) {
			return "成功";
		} else
		if (errCode.equals("0001")) {
			return "支付失败";
		} else
		if (errCode.equals("0002")) {
			return "未知错误";
		} else
		if (errCode.equals("0999")) {
			return "未开通直冲功能";
		} else
		if (errCode.equals("1000")) {
			return "下单失败";
		}else
		if (errCode.equals("1001")) {
			return "传入参数不完整";
		}else
		if (errCode.equals("1002")) {
			return "验证摘要串验证失败";
		}else
		if (errCode.equals("1005")) {
			return "没有对应充值产品";
		}else
		if (errCode.equals("1006")) {
			return "系统异常，请稍后重试";
		}else
		if (errCode.equals("1007")) {
			return "账户余额不足";
		}else
		if (errCode.equals("1008")) {
			return "此产品超出当天限额";
		}else
		if (errCode.equals("1010")) {
			return "产品与手机号不匹配";
		}else
		if (errCode.equals("1011")) {
			return "定单号不允许重复";
		}else
		if (errCode.equals("1013")) {
			return "暂不可充值";
		}else
		if (errCode.equals("1015")) {
			return "无法查到对应号段";
		}else
    	if (errCode.equals("1017")) {
			return "电信手机10秒内不能重复充值";
		}else
		if (errCode.equals("1020")) {
			return "号码不支持流量充值卡";
		}else
		if (errCode.equals("1022")) {
			return "充值号码格式错误";
		}else
		if (errCode.equals("1028")) {
			return "下单接口请求次数超限";
		} else {
			return "末知错误";
		}
	}
	
	public Map<String, Object> directFill(String prodid,String sn,String mobile) {
		String verifystring=DigestUtils.md5Hex("prodid="+prodid+"&agentid="+agentId+"&backurl=&returntype=2&orderid="+sn+"&mobilenum="+mobile+"&source=esales&mark=&merchantKey="+key);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("prodid", prodid);		
		params.put("agentid", agentId);		
		params.put("backurl", "");		
		params.put("returntype", "2");		
		params.put("orderid", sn);		
		params.put("mobilenum", mobile);		
		params.put("source", "esales");		
		params.put("mark", "");		
		params.put("verifystring", verifystring);		
		//String resp = get("http://219.143.36.227:101/dealer/directfill/directFill.do",params);
		String resp = get("http://hfjk.19ego.com/esales2/directfill/directFill.do",params);
		
		Map<String, Object> map = new  HashMap<String, Object>();
		if (resp.length()==4) {
			map.put("code", resp);
		} else {
			Document document;
			try {
				document = DocumentHelper.parseText(resp);
		        Element root = document.getRootElement();
			    for  ( Iterator i = root.elementIterator(); i.hasNext(); ) {
			            Element element = (Element) i.next();
			            for  ( Iterator j = element.elementIterator(); j.hasNext(); ) {
			               Element item = (Element) j.next();
			               map.put(item.attribute("name").getText(),item.attribute("value").getText());
			            }
			         }
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				map.put("code","0001");
			}   
			map.put("code",map.get("resultno"));
		}
		return map;
		
	}
	
	public String getProductError(String errCode) {
		if (errCode.equals("0000")) {
			return "成功";
		} else
		if (errCode.equals("1000")) {
			return "系统异常";
		} else
		if (errCode.equals("1001")) {
			return "传入参数不完整";
		} else
		if (errCode.equals("0999")) {
			return "未开通直冲功能";
		} else
		if (errCode.equals("1002")) {
			return "验证摘要串验证失败";
		} else 
		if (errCode.equals("1005")) {
			return "查询数据库无产品返回";
		}else {
			return "末知异常";
		}
	}
	
	@Transactional
	public Map<String, Object> directProduct() {
		String verifystring=DigestUtils.md5Hex("agentid="+agentId+"&source=esales&merchantKey="+key);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("agentid", agentId);		
		params.put("source", "esales");		
		params.put("verifystring", verifystring);		
		//String resp = get("http://219.143.36.227:101/dealer/prodquery/directProduct.do",params);
		String resp = get("http://hfjk.19ego.com/esales2/prodquery/directProduct.do",params);
		
		Map<String, Object> map = new  HashMap<String, Object>();
		if (resp.length()==4) {
			map.put("code", resp);
		} else {
			Document document;
			try {
				document = DocumentHelper.parseText(resp);
		        Element root = document.getRootElement();
			    for  ( Iterator i = root.elementIterator(); i.hasNext(); ) {
			            Element element = (Element) i.next();
						MobilePrice price = new MobilePrice();
					   
			            for  ( Iterator j = element.elementIterator(); j.hasNext(); ) {
			            	Element product = (Element) j.next();
			            	if (product.attribute("name").getText().equals("prodId")) {
			            		price.setProdId(product.attribute("value").getText());
			            	}
			            	if (product.attribute("name").getText().equals("prodPrice")) {
			            		price.setCost(new BigDecimal(product.attribute("value").getText()));
			            	}
			            	if (product.attribute("name").getText().equals("prodContent")) {
			            		price.setDenomination(new Integer(product.attribute("value").getText()));
			            	}
			            	if (product.attribute("name").getText().equals("prodIsptype")) {
			            		price.setIspType(java.net.URLDecoder.decode(product.attribute("value").getText(), "utf-8"));
			            	}
			            	if (product.attribute("name").getText().equals("prodProvinceid")) {
			            		price.setProvince(java.net.URLDecoder.decode(product.attribute("value").getText(), "utf-8"));
			            	}
			            	if (product.attribute("name").getText().equals("prodDelaytimes")) {
			            		price.setDelaytimes(java.net.URLDecoder.decode(product.attribute("value").getText(), "utf-8"));
			            	}
			            	if (product.attribute("name").getText().equals("prodType")) {
			            		price.setProdType(java.net.URLDecoder.decode(product.attribute("value").getText(), "utf-8"));
			            	}
			            }
			            price.setPrice(price.getCost().add(price.getCost().multiply(new BigDecimal("0.006"))).setScale(2,BigDecimal.ROUND_UP));

                        MobilePrice save = mobilePriceDao.findByProdId(price.getProdId());
                        if (save==null) {
                        	mobilePriceDao.persist(price);
                        } else {
                        	BeanUtils.copyProperties(price, save);
                        	mobilePriceDao.persist(save);
                        }
                        
			         }
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				map.put("code","0001");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				map.put("code","0001");
			}   
			map.put("code", "0000");
		}
		return map;
		
	}		
		
	public String getSegError(String errCode) {
		if (errCode.equals("0000")) {
			return "成功";
		} else
		if (errCode.equals("1000")) {
			return "系统异常";
		} else
		if (errCode.equals("1001")) {
			return "传入参数不完整";
		} else
		if (errCode.equals("0999")) {
			return "未开通直冲功能";
		} else
		if (errCode.equals("1002")) {
			return "验证摘要串验证失败";
		} else 
		if (errCode.equals("1022")) {
			return "充值号码格式错误";
		}else {
			return "末知异常";
		}
	}
	public Map<String, Object> getSegment(String mobile) {
		String verifystring=DigestUtils.md5Hex("agentid="+agentId+"&source=esales&mobilenum="+mobile+"&merchantKey="+key);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("agentid", agentId);		
		params.put("source", "esales");		
		params.put("mobilenum", mobile);		
		params.put("verifystring", verifystring);		
		//String resp = get("http://219.143.36.227:101/dealer/accegment/accsegment.do",params);
		String resp = get("http://hfjk.19ego.com/esales2/accegment/accsegment.do",params);

		Map<String, Object> map = new  HashMap<String, Object>();
		if (resp.length()==4) {
			map.put("code", resp);
		} else {
			Document document;
			try {
				document = DocumentHelper.parseText(resp);
		        Element root = document.getRootElement();
			    for  ( Iterator i = root.elementIterator(); i.hasNext(); ) {
			       Element element = (Element) i.next();
		           for  ( Iterator j = element.elementIterator(); j.hasNext(); ) {
		              	Element info = (Element) j.next();
		            	map.put(info.attribute("name").getText(),java.net.URLDecoder.decode(info.attribute("value").getText(), "utf-8"));
		            	}
			    }
			    map.put("code","0000");
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				map.put("code","0001");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				map.put("code","0001");
			} 
		}
		return map;
		
	}
	
	public String getSearchError(String errCode) {
		if (errCode.equals("0000")) {
			return "成功";
		} else
		if (errCode.equals("1000")) {
			return "系统异常";
		} else
		if (errCode.equals("1001")) {
			return "传入参数不完整";
		} else
		if (errCode.equals("0999")) {
			return "未开通直冲功能";
		} else
		if (errCode.equals("1002")) {
			return "验证摘要串验证失败";
		}else
		if (errCode.equals("1006")) {
			return "系统异常，请稍后重试";
		}else
		if (errCode.equals("1009")) {
			return "没有对应订单";
		}else 
		if (errCode.equals("1018")) {
			return "查询接口查询频率超限";
		}else {
			return "末知异常";
		}
	}
	public Map<String, Object> directSearch(String sn) {
		String verifystring=DigestUtils.md5Hex("agentid="+agentId+"&backurl=&returntype=2&orderid="+sn+"&source=esales&merchantKey="+key);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("agentid", agentId);		
		params.put("source", "esales");		
		params.put("orderid", sn);		
		params.put("backurl", "");		
		params.put("returntype", "2");		
		params.put("verifystring", verifystring);		
		//String resp = get("http://219.143.36.227:101/dealer/orderquery/directSearch.do",params);
		String resp = get("http://hfjk.19ego.com/esales2/orderquery/directSearch.do",params);
		
		Map<String, Object> map = new  HashMap<String, Object>();
		if (resp.length()==4) {
			map.put("code", resp);
		} else {
			Document document;
			try {
				document = DocumentHelper.parseText(resp);
		        Element root = document.getRootElement();
			    for  ( Iterator i = root.elementIterator(); i.hasNext(); ) {
			       Element element = (Element) i.next();
		           for  ( Iterator j = element.elementIterator(); j.hasNext(); ) {
		              	Element info = (Element) j.next();
		            	map.put(info.attribute("name").getText(),java.net.URLDecoder.decode(info.attribute("value").getText(), "utf-8"));
		            	}
			    }
			    map.put("code","0000");
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				map.put("code","0001");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				map.put("code","0001");
			} 
		}
		return map;
		
	}
	

	public static void main(String[] args) throws Exception {
		
	    //String mm=	DigestUtils.md5Hex("13860431130"+"myjsy2014$$");
		MobileFactory factory = new MobileFactory();
		factory.directProduct();
		//factory.getSegment("13811528476");
		//factory.directSearch("201504273739");
	}
}
