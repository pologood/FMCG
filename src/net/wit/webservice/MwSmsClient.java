package net.wit.webservice;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * 梦网平台发送短信客户端实体类
 * @author Administrator
 *
 */
public class MwSmsClient {
	
	//private String serviceURL="http://ws.montnets.com:9003/MWGate/wmgw.asmx";
	private String serviceURL="http://61.145.229.29:9003/MWGate/wmgw.asmx";
	//private String userId="";
	//private String password="";
	
	public MwSmsClient(){
	}
	
	/**
	 * 1短信息发送（相同内容群发，可自定义流水号）
	 * @param userId 用户账号
	 * @param password 用户密码
	 * @param pszMobis 手机号码，用英文逗号(,)分隔，最大100个号码
	 * @param pszMsg 短信内容,相同信息内容的号码一定要打包发送
	 * @param iMobiCount 手机号码个数
	 * @param pszSubPort 扩展子号，不需要扩展子号请填星号“*”
	 * @param MsgId 自定义消息编号（注：不需要请填0）
	 * @return 成功：发送成功：平台消息编号，如：-8485643440204283743或1485643440204283743
	 * 	             错误：-1 	参数为空。信息、电话号码等有空指针，登陆失败;-12	有异常电话号码;-14	实际号码个数超过100;-999	web服务器内部错误
	 */
	public String MongateSendSubmit(String userId, String password,String pszMobis, String pszMsg, int iMobiCount,String pszSubPort,String MsgId){
		String result="";
		String soapAction="http://tempuri.org/MongateSendSubmit";
		String xml="<?xml version=\"1.0\" encoding=\"utf-8\"?>";
		xml+="<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">";
		xml+="<soap:Body>";
		xml+="<MongateSendSubmit xmlns=\"http://tempuri.org/\">";
		xml+="<userId>"+userId+"</userId>";
		xml+="<password>"+password+"</password>";
		xml+="<pszMobis>"+pszMobis+"</pszMobis>";
		xml+="<pszMsg>"+pszMsg+"</pszMsg>";
		xml+="<iMobiCount>"+iMobiCount+"</iMobiCount>";
		xml+="<pszSubPort>"+pszSubPort+"</pszSubPort>";
		xml+="<MsgId>"+MsgId+"</MsgId>";
		xml+="</MongateSendSubmit>";
		xml+="</soap:Body>";
		xml+="</soap:Envelope>";
		
		URL url;
		try {
			url=new URL(serviceURL);
			URLConnection connection = url.openConnection();
			HttpURLConnection httpconn = (HttpURLConnection) connection;
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			bout.write(xml.getBytes("utf-8"));
			byte[] b = bout.toByteArray();
			httpconn.setRequestProperty("Content-Length", String.valueOf(b.length));
			httpconn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
			httpconn.setRequestProperty("SOAPAction", soapAction);
			httpconn.setRequestMethod("POST");
			httpconn.setDoInput(true);
			httpconn.setDoOutput(true);
			
			OutputStream out=httpconn.getOutputStream();
			out.write(b);
			out.close();
			
			InputStreamReader isr=new InputStreamReader(httpconn.getInputStream());
			BufferedReader in=new BufferedReader(isr);
			String inputLine;
			while(null!=(inputLine=in.readLine())){
				Pattern pattern=Pattern.compile("<MongateSendSubmitResult>(.*)</MongateSendSubmitResult>");
				Matcher matcher=pattern.matcher(inputLine);
				while(matcher.find()){
					result=matcher.group(1);
				}
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "-1";
		}
	}
	
	/**
	 * 2短信息发送（不同内容群发，可自定义不同流水号，自定义不同扩展号）
	 * @param userId 用户账号
	 * @param password 用户密码
	 * @param multix_mt 批量短信请求包:结构体"自定义消息编号（注：不需要请填0）|通道号（注：不需要扩展子号请填星号“*”）|手机号|短信内容（注：采用base64编码-gbk编码）",例如：不需扩展不需流水号"0|*|13800138000|xOO6wyy7ttOtyrnTwyE="，(每个结构体间用固定的英文逗号分隔符隔开)
	 * @return 成功：发送成功：平台消息编号，如：-8485643440204283743或1485643440204283743
	 * 	             错误：-1 	参数为空。信息、电话号码等有空指针，登陆失败;-12	有异常电话号码;-14	实际号码个数超过100;-999	web服务器内部错误
	 */
	public String MongateMULTIXSend(String userId, String password,String multix_mt){
		String result="";
		String soapAction="http://tempuri.org/MongateMULTIXSend";
		String xml="<?xml version=\"1.0\" encoding=\"utf-8\"?>";
		xml+="<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">";
		xml+="<soap:Body>";
		xml+="<MongateMULTIXSend xmlns=\"http://tempuri.org/\">";
		xml+="<userId>"+userId+"</userId>";
		xml+="<password>"+password+"</password>";
		xml+="<multixmt>"+multix_mt+"</multixmt>";
		xml+="</MongateMULTIXSend>";
		xml+="</soap:Body>";
		xml+="</soap:Envelope>";
		
		URL url;
		try {
			url=new URL(serviceURL);
			URLConnection connection = url.openConnection();
			HttpURLConnection httpconn = (HttpURLConnection) connection;
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			bout.write(xml.getBytes("utf-8"));
			byte[] b = bout.toByteArray();
			httpconn.setRequestProperty("Content-Length", String.valueOf(b.length));
			httpconn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
			httpconn.setRequestProperty("SOAPAction", soapAction);
			httpconn.setRequestMethod("POST");
			httpconn.setDoInput(true);
			httpconn.setDoOutput(true);
			
			OutputStream out=httpconn.getOutputStream();
			out.write(b);
			out.close();
			
			InputStreamReader isr=new InputStreamReader(httpconn.getInputStream());
			BufferedReader in=new BufferedReader(isr);
			String inputLine;
			while(null!=(inputLine=in.readLine())){
				Pattern pattern=Pattern.compile("<MongateMULTIXSendResult>(.*)</MongateMULTIXSendResult>");
				Matcher matcher=pattern.matcher(inputLine);
				while(matcher.find()){
					result=matcher.group(1);
				}
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "-1";
		}
	}
	
	/**
	 * 3查询余额
	 * @param userId 用户账号
	 * @param password 用户密码
	 * @return 正数或0：帐户可发送条数;负数：登陆失败
	 */
	public int MongateQueryBalance(String userId,String password){
		int result=1;
		String soapAction="http://tempuri.org/MongateQueryBalance";
		String xml="<?xml version=\"1.0\" encoding=\"utf-8\"?>";
		xml+="<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">";
		xml+="<soap:Body>";
		xml+="<MongateQueryBalance xmlns=\"http://tempuri.org/\">";
		xml+="<userId>"+userId+"</userId>";
		xml+="<password>"+password+"</password>";
		xml+="</MongateQueryBalance>";
		xml+="</soap:Body>";
		xml+="</sopa:Envelope>";
		
		URL url;
		try {
			url=new URL(serviceURL);
			URLConnection connection=url.openConnection();
			HttpURLConnection httpconn=(HttpURLConnection)connection;
			ByteArrayOutputStream bout=new ByteArrayOutputStream();
			bout.write(xml.getBytes("utf-8"));
			byte[] b=bout.toByteArray();
			httpconn.setRequestProperty("Content-Length", String.valueOf(b.length));
			httpconn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
			httpconn.setRequestProperty("SOAPAction", soapAction);
			httpconn.setRequestMethod("POST");
			httpconn.setDoInput(true);
			httpconn.setDoOutput(true);
			
			OutputStream out=httpconn.getOutputStream();
			out.write(b);
			out.close();
			
			InputStreamReader isr=new InputStreamReader(httpconn.getInputStream());
			BufferedReader in=new BufferedReader(isr);
			String inputLine;
			while(null!=(inputLine=in.readLine())){
				Pattern pattern=Pattern.compile("<MongateQueryBalanceResult>(.*)</MongateQueryBalanceResult>");
				Matcher matcher=pattern.matcher(inputLine);
				while(matcher.find()){
					result=Integer.valueOf(matcher.group(1)) ;
				}
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	/**
	 * 4获取上行/状态报告等
	 * @param userId 用户账号 ,长度最大6个字节
	 * @param password 用户密码
	 * @param iReqType 请求类型(1:上行 2: 状态报告)
	 * @return null 无信息;(信息类型(上行标志1),时间,上行手机号,上行通道号,扩展子号(若无扩展这里填*),*,上行信息内容);或：信息类型(状态报告标志2),时间,平台消息编号,通道号,手机号,MongateSendSubmit时填写的MsgId（用户自编消息编号）,*,状态值(0:成功 非0:失败),详细错误原因
	 */
	public String[] MongateGetDeliver (String userId,String password,int iReqType){
		String[] result=new String[]{};
		String soapAction="http://tempuri.org/MongateGetDeliver";
		String xml="<?xml version=\"1.0\" encoding=\"utf-8\"?>";
		xml+="<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">";
		xml+="<soap:Body>";
		xml+="<MongateGetDeliver xmlns=\"http://tempuri.org/\">";
		xml+="<userId>"+userId+"</userId>";
		xml+="<password>"+password+"</password>";
		xml+="<iReqType>"+iReqType+"</iReqType>";
		xml+="</MongateGetDeliver>";
		xml+="</soap:Body>";
		xml+="</soap:Envelope>";
		
		URL url;
		try {
			url=new URL(serviceURL);
			URLConnection connection=url.openConnection();
			HttpURLConnection httpconn=(HttpURLConnection)connection;
			ByteArrayOutputStream bout=new ByteArrayOutputStream();
			bout.write(xml.getBytes("utf-8"));
			byte[] b=bout.toByteArray();
			httpconn.setRequestProperty("Content-Length", String.valueOf(b.length));
			httpconn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
			httpconn.setRequestProperty("SOAPAction", soapAction);
			httpconn.setRequestMethod("POST");
			httpconn.setDoInput(true);
			httpconn.setDoOutput(true);
			
			OutputStream out=httpconn.getOutputStream();
			out.write(b);
			out.close();
			
			InputStreamReader isr=new InputStreamReader(httpconn.getInputStream());
			BufferedReader in=new BufferedReader(isr);
			String inputLine;
			while(null!=(inputLine=in.readLine())){
				Pattern pattern=Pattern.compile("<MongateGetDeliverResult>(.*)</MongateGetDeliverResult>");
				Matcher matcher=pattern.matcher(inputLine);
				while(matcher.find()){
					result=matcher.group().split(",");
				}
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return result;
		}
	}
	
}
