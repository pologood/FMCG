package net.wit.util;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class HttpClientUtil {
	private static Logger logger = Logger.getLogger(HttpClientUtil.class);
	private final static int DEFAULT_TIMEOUT = 30000;

	public static String doGet(String url) {
		return doGet(url, null, "utf-8", true);
	}

	public static String doGet(String url, String charset) {
		return doGet(url, null, charset, true);
	}

	 public static String getReturnDataByHttpPost(String urlString) throws UnsupportedEncodingException {  
	        String res = "";   
	        try {   
	            URL url = new URL(urlString);  
	            java.net.HttpURLConnection conn = (java.net.HttpURLConnection)url.openConnection();  
	            conn.setDoOutput(true);  
	            conn.setRequestMethod("POST");  
	            java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(conn.getInputStream(),"UTF-8"));  
	            String line;  
	            while ((line = in.readLine()) != null) {  
	                res += line;  
	            }  
	            in.close();  
	        } catch (Exception e) {  
	            logger.error("error in wapaction,and e is " + e.getMessage());  
	        }  
//	      System.out.println(res);  
	        return res;  
	    }  
	
	public static String postHttp(String param1,String param2,String url){
		String responseMsg = "";
		HttpClient httpClient=new HttpClient();
		httpClient.getParams().setContentCharset("GBK");
		PostMethod postMethod=new PostMethod(url);
		/*
		NameValuePair[] data = { new NameValuePair("param1", param1),
				new NameValuePair("param2", param2) };
		postMethod.setRequestBody(data);*/
		 //postMethod.addParameter("param1", param1);
		// postMethod.addParameter("param2", param2);
		 try {
			 httpClient.executeMethod(postMethod);//200
			 responseMsg = postMethod.getResponseBodyAsString().trim();
			 System.out.println(responseMsg);
		 } catch (HttpException e) {
			 e.printStackTrace();
		 }catch (IOException e) {
			 e.printStackTrace();
		 } finally {
			 postMethod.releaseConnection();
		 }
		return responseMsg;
	}
	
	public static String doGet(String url, String queryString, String charset, boolean pretty) {
		StringBuffer response = new StringBuffer();
		HttpClient client = new HttpClient();
		client.getHttpConnectionManager().getParams().setConnectionTimeout(DEFAULT_TIMEOUT);
		client.getHttpConnectionManager().getParams().setSoTimeout(DEFAULT_TIMEOUT);
		HttpMethod method = new GetMethod(url);
		try {
			if (queryString != null && !queryString.equals(""))
				method.setQueryString(URIUtil.encodeQuery(queryString));
			client.executeMethod(method);
			if (method.getStatusCode() == HttpStatus.SC_OK) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream(),
						charset));
				String line;
				while ((line = reader.readLine()) != null) {
					if (pretty)
						response.append(line).append(System.getProperty("line.separator"));
					else
						response.append(line);
				}
				reader.close();
			}
		} catch (Exception e) {
			logger.error("HttpClientUtil.doGet is Error:", e);
			return "";
		} finally {
			method.releaseConnection();
		}
		return response.toString();
	}

	public static String doPost(String urlStr, String params) {
		return doPost(urlStr, params, "GBK");
		// return doPost(urlStr, params, "gb2312");
	}

	public static String doPost(String urlStr, String params, String charset) {
		OutputStreamWriter output = null;
		BufferedReader input = null;
		try {
			URL url = new URL(urlStr);
			URLConnection conn = url.openConnection();
			// conn.setRequestProperty("accept", "*/*");
			// conn.setRequestProperty("connection", "Keep-Alive");
			// conn.setRequestProperty("user-agent",
			// "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			conn.setDoOutput(true);
			conn.setDoInput(true);

			output = new OutputStreamWriter(conn.getOutputStream(), charset);
			output.write(params);
			output.flush();
			output.close();

			String line = "", result = "";

			input = new BufferedReader(new InputStreamReader(conn.getInputStream(), "gb2312"));
			input = new BufferedReader(new InputStreamReader(conn.getInputStream(), charset));
			while ((line = input.readLine()) != null) {
				result += line;
			}
			logger.info("result"+result);
			return result;
		} catch (IOException e) {
			logger.error("doPost:url{" + urlStr + "?" + params + "}", e);
			logger.info("doPost:url{" + urlStr + "?" + params + "}", e);
		} finally {
			try {
				if (input != null) {
					input.close();
				}
				if (output != null) {
					output.close();
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return "";
	}

	public static String doPost(String url, Map<String, Object> params, String charset, boolean pretty) {
		StringBuffer response = new StringBuffer();
		HttpClient client = new HttpClient();
		HttpMethod method = new PostMethod(url);
		method.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);
		if (params != null) {
			HttpMethodParams p = new HttpMethodParams();
			for (Map.Entry<String, Object> entry : params.entrySet()) {
				System.out.println(entry.getKey() + ":" + entry.getValue());
				p.setParameter(entry.getKey(), entry.getValue());
			}
			method.setParams(p);
		}
		// method.setQueryString(new NameValuePair[] { new
		// NameValuePair("journalNo", "1555") });
		try {
			client.executeMethod(method);
			if (method.getStatusCode() == HttpStatus.SC_OK) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream(),
						charset));
				String line;
				while ((line = reader.readLine()) != null) {
					if (pretty)
						response.append(line).append(System.getProperty("line.separator"));
					else
						response.append(line);
				}
				reader.close();
			}
		} catch (IOException e) {
			logger.error("HttpClientUtil.doPost is Error:", e);
		} finally {
			method.releaseConnection();
		}
		return response.toString();
	}
	public static String handleServerForString(String urlStr,String requestStr,int port) throws Exception{
		if(StringUtils.isNotEmpty(requestStr)){
			String returnXml = HttpClientUtil.doSocket(urlStr,requestStr,port);
			if(returnXml !=""){
				return returnXml;
			}
		}
		return null;
	}
	
	public static String doSocket(String urlStr, String params,int port) throws Exception {
		//int port = 12346;
		Socket socket = new Socket(urlStr,port);
		BufferedReader in = null;
		PrintWriter out = null;
		try {
			in = new BufferedReader(new InputStreamReader(socket
                    .getInputStream(),"GBK"));
			out = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream(),"GBK")), true);
			out.print(params);
			out.flush();
			String line = "", result = "";
			while ((line = in.readLine()) != null) {
				result += line;
			}
			return result;
		} catch (IOException e) {
			logger.error("doPost:url{" + urlStr + "?" + params + "}", e);
		} finally {
			try {
				out.close();
				in.close();
				socket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return "";
	}
	
	public static String sendAndReceive(byte[] requestMsg, String ip, int port) throws Exception{
		Socket s = null;
		OutputStream out = null;
	    InputStream in = null;
	    try {
	    	s = new Socket(ip,port);
	    	//s.connect(new InetSocketAddress("localhost",Integer.valueOf("10892").intValue()), Integer.valueOf("1000").intValue()*1000/2);
	    	//s.setSoTimeout(Integer.valueOf("1000").intValue()*1000);
	    	out = s.getOutputStream();
	    	out.write(requestMsg);
	    	out.flush();
	    }catch(Exception e){
	    	e.printStackTrace();
	    	throw e;
	    }
	    try{
	    	in = s.getInputStream();
	    	int count = 0;
	    	  while (count == 0) {
	    	   count = in.available();
	    	  }
	    	  System.out.println(count);
	    	byte[] response1 = new byte[count];
	    	in.read(response1);
	    	String resMsg1 = new String(response1,"gb2312");
	    	return resMsg1; 
	    }catch(Exception e){
	    	throw e;
	    }finally {
	    	try {
	           if (out != null) {
	             out.close();
	           }
	           if (in != null) {
	             in.close();
	           }
	            if (s != null)
	              s.close();
	          }
	          catch (Exception e) {
	            e.printStackTrace();
	          }
	    }
	}
	
	public static void main(String[] args) {
		String result;
		// result = doGet(
		// "http://testapi.huitouke.cn/trade?sbs_id=106888888888888&card_no=00000818&password=7522520c746f&saving_sub=208&cash=0&bank_amount=0&point_sub=0&order_number=2013042588732&store_id=0&coupon_pwd=&t=1366897337&sign=97fa4a3ae2d7d7d20ff963f8a0ce02b4",
		// null, "utf-8", true);
		// System.out.println("doGet:" + result);

		String params="userId=61234567890&pay_for=&provName=%E6%B1%9F%E8%8B%8F&cityName=%E8%8B%8F%E5%B7%9E&type=001&chargeCompanyCode=v81494&chargeCompanyName=%E6%B1%9F%E8%8B%8F%E8%8B%8F%E5%B7%9E_%E8%8B%8F%E5%B7%9E%E5%B8%82%E8%87%AA%E6%9D%A5%E6%B0%B4%E5%85%AC%E5%8F%B8%28%E5%B8%82%E5%8C%BA%29_%E6%B0%B4%E8%B4%B9_%E6%88%B7%E5%8F%B7_%E7%9B%B4%E5%85%85%E4%BB%BB%E6%84%8F%E5%85%85&cardId=64250801&account=455912";
		String msg=doPost("http://181.18.18.104:9089/resourcePlatform/publicRechargeQuery", params);
        System.out.println(111);
		
		/*Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("sbs_id", "106888888888888");
		map.put("card_no", "00000818");
		map.put("password", "7522520c746f");
		map.put("saving_sub", "208");
		map.put("cash", "0");
		map.put("bank_amount", "0");
		map.put("point_sub", "0");
		map.put("order_number", "2013042588732");
		map.put("store_id", "0");
		// map.put("coupon_pwd", "");
		map.put("t", "1366897337");
		map.put("sign", "97fa4a3ae2d7d7d20ff963f8a0ce02b4");
		result = doPost("http://testapi.huitouke.cn/trade", map, "utf-8", false);
		System.out.println("doPost:" + result);

		Set<String> ks = map.keySet();
		for (String key : ks) {
			System.out.println(key + ":" + map.get(key));
		}*/

	}
}
