package net.wit.webservice;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.wit.entity.Game;
import net.wit.util.MD5Utils;
import net.wit.webservice.TerminalServiceXmlServiceStub.DirectChargeResponse;
import net.wit.webservice.TerminalServiceXmlServiceStub.DirectOrderStateQueryResponse;
import net.wit.webservice.TerminalServiceXmlServiceStub.GetDirectAreaInfoResponse;
import net.wit.webservice.TerminalServiceXmlServiceStub.GetDirectSrvInfoResponse;

import org.apache.axis2.client.Options;
import org.apache.commons.codec.digest.DigestUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class TerminalXmlService {

	/**
	 * 签名
	 * @throws ParseException 
	 */
	private String generateSign(Game game) {
	    ResourceBundle bundle=PropertyResourceBundle.getBundle("config");
		StringBuilder builder = new StringBuilder();
		builder.append(game.getMerNo());
		builder.append(game.getTermNo());
		builder.append(game.getAccount());
 	    builder.append(game.getServer()); 
		builder.append(game.getArea());
		builder.append(game.getSn());
		builder.append(game.getCardId());
		builder.append(game.getPriceId());
		builder.append(game.getChargeCount());
		builder.append("00");
		builder.append(game.getReqTime());
		builder.append( DigestUtils.md5Hex(bundle.getString("dPosKey")) );
		
		Pattern p = Pattern.compile("\\r|\n");
		Matcher m = p.matcher(builder.toString());
		String str = m.replaceAll("");
		return DigestUtils.md5Hex(str);
	}

	private String generateRetSign(Game game) {
	    ResourceBundle bundle=PropertyResourceBundle.getBundle("config");
		StringBuilder builder = new StringBuilder();
		builder.append(game.getMerNo());
		builder.append(game.getTermNo());
		builder.append(game.getSn());
		builder.append(game.getReqTime());
		builder.append( DigestUtils.md5Hex(bundle.getString("dPosKey")) );
		
		Pattern p = Pattern.compile("\\r|\n");
		Matcher m = p.matcher(builder.toString());
		String str = m.replaceAll("");
		return DigestUtils.md5Hex(str);
	}
	
	public String directCharge(Game game) {
	   ResourceBundle bundle=PropertyResourceBundle.getBundle("config");
	   try {   
		  game.setTermNo(bundle.getString("dPosTermNo"));
		  game.setMerNo(bundle.getString("dPosMerNo"));
		  SimpleDateFormat ReqTime =new SimpleDateFormat("yyyyMMddHHmmss");
		  Date d = new Date();
		  game.setReqTime(ReqTime.format(d));
  		  StringBuilder builder = new StringBuilder();
		  builder.append("<directCharge>");
		  builder.append("<storeNo>"+game.getMerNo()+"</storeNo>");
		  builder.append("<terminalNo>"+game.getTermNo()+"</terminalNo>");
		  builder.append("<orderNo>"+game.getSn()+"</orderNo>");
		  builder.append("<cardId>"+game.getCardId()+"</cardId>");
		  builder.append("<priceId>"+game.getPriceId()+"</priceId>");
		  builder.append("<payType>00</payType>");
		  builder.append("<account>"+game.getAccount()+"</account>");
		  builder.append("<chargeCount>"+game.getChargeCount()+"</chargeCount>");
		  builder.append("<operator>"+game.getMerNo()+"</operator>");
		  builder.append("<requestTime>"+game.getReqTime()+"</requestTime>");
		  builder.append("<sign>"+generateSign(game)+"</sign>");
		  builder.append("</directCharge>");
		  TerminalServiceXmlServiceStub ePay = new TerminalServiceXmlServiceStub();
		  net.wit.webservice.TerminalServiceXmlServiceStub.DirectCharge in = new net.wit.webservice.TerminalServiceXmlServiceStub.DirectCharge();
		  org.apache.axis2.databinding.types.soapencoding.String instr = new org.apache.axis2.databinding.types.soapencoding.String();
		  instr.setString(builder.toString());
		  in.setRequestXml(instr);
		  DirectChargeResponse out = ePay.directCharge(in);
		  Document document = DocumentHelper.parseText(out.getDirectChargeReturn().getString());   
		  Map<String, String> respMap = new LinkedHashMap<String, String>();
	      Element root = document.getRootElement();
	      for  ( Iterator i = root.elementIterator(); i.hasNext(); ) {
	            Element element = (Element) i.next();
	            respMap.put(element.getName(), element.getText());
	         }

	      game.setRespCode(respMap.get("respCode"));
	      game.setRespMsg(respMap.get("respDisc"));
				  		  
    	} catch (Exception e) {
    		game.setRespCode("9999");
    		game.setRespMsg("末知异常");
	    }
		return game.getRespCode();
	}
	public String directOrderStateQuery(Game game) {
    	try {   
  		      SimpleDateFormat ReqTime =new SimpleDateFormat("yyyyMMddHHmmss");
  		      Date d = new Date();
  		      game.setReqTime(ReqTime.format(d));
    		  StringBuilder builder = new StringBuilder();
    		  builder.append("<directOrderStateQuery>");
    		  builder.append("<storeNo>"+game.getMerNo()+"</storeNo>");
    		  builder.append("<terminalNo>"+game.getTermNo()+"</terminalNo>");
    		  builder.append("<orderNo>"+game.getSn()+"</orderNo>");
    		  builder.append("<requestTime>"+game.getReqTime()+"</requestTime>");
    		  builder.append("<sign>"+generateRetSign(game)+"</sign>");
    		  builder.append("</directOrderStateQuery>");
    		  TerminalServiceXmlServiceStub ePay = new TerminalServiceXmlServiceStub();
    		  net.wit.webservice.TerminalServiceXmlServiceStub.DirectOrderStateQuery in = new net.wit.webservice.TerminalServiceXmlServiceStub.DirectOrderStateQuery();
    		  org.apache.axis2.databinding.types.soapencoding.String instr = new org.apache.axis2.databinding.types.soapencoding.String();
    		  instr.setString(builder.toString());
    		  in.setRequestXml(instr);
    		  DirectOrderStateQueryResponse out = ePay.directOrderStateQuery(in);
    		  Document document = DocumentHelper.parseText(out.getDirectOrderStateQueryReturn().getString());   
    		  Map<String, String> respMap = new LinkedHashMap<String, String>();
    	      Element root = document.getRootElement();
    	      for  ( Iterator i = root.elementIterator(); i.hasNext(); ) {
    	            Element element = (Element) i.next();
    	            respMap.put(element.getName(), element.getText());
    	         }

    	      game.setRetCode(respMap.get("respCode"));
    	      game.setRetMsg(respMap.get("respDisc"));
    	} catch (Exception e) {
    	  	  game.setRetCode("9999");
    		  game.setRetMsg("末知异常");
	    }
		return game.getRetCode();
	}
	
	
	public Map<String,String> getDirectAreaInfo(int cardId,int priceId) {
		Map<String, String> Map = new LinkedHashMap<String, String>();
		ResourceBundle bundle=PropertyResourceBundle.getBundle("config");
    	try {   
		  SimpleDateFormat ReqTime =new SimpleDateFormat("yyyyMMddHHmmss");
		  Date d = new Date();
		  String
		  signStr = bundle.getString("dPosTermNo");
		  signStr += String.valueOf(cardId);
		  signStr += String.valueOf(priceId);
		  signStr += null;
		  signStr += ReqTime.format(d);
		  signStr += DigestUtils.md5Hex(bundle.getString("dPosKey"));
		  signStr = DigestUtils.md5Hex(signStr);
  		  StringBuilder builder = new StringBuilder();
  		  builder.append("<getDirectAreaInfo>");
  		  builder.append("<terminalNo>"+bundle.getString("dPosTermNo")+"</terminalNo>");
  		  builder.append("<cardId>"+String.valueOf(cardId)+"</cardId>");
  		  builder.append("<priceId>"+String.valueOf(priceId)+"</priceId>");
  		  builder.append("<requestTime>"+ReqTime.format(d)+"</requestTime>");
  		  builder.append("<sign>"+signStr+"</sign>");
  		  builder.append("</getDirectAreaInfo>");
  		  TerminalServiceXmlServiceStub ePay = new TerminalServiceXmlServiceStub();
  		  net.wit.webservice.TerminalServiceXmlServiceStub.GetDirectAreaInfo in = new net.wit.webservice.TerminalServiceXmlServiceStub.GetDirectAreaInfo();
  		  org.apache.axis2.databinding.types.soapencoding.String instr = new org.apache.axis2.databinding.types.soapencoding.String();
  		  instr.setString(builder.toString());
  		  in.setRequestXml(instr);
  		  GetDirectAreaInfoResponse out = ePay.getDirectAreaInfo(in);
  		  Document document = DocumentHelper.parseText(out.getGetDirectAreaInfoReturn().getString());   
  		  Map<String, String> respMap = new LinkedHashMap<String, String>();
  		  Element areaMap = null;
  	      Element root = document.getRootElement();
  	      for  ( Iterator i = root.elementIterator(); i.hasNext(); ) {
  	            Element element = (Element) i.next();
  	            if (element.getName()=="gameArea") {
  	            	areaMap = element;
  	            } else
  	            {
  	                respMap.put(element.getName(), element.getText());
  	            }
  	         }
  	      if ("0".equals(respMap.get("respCode"))) {
    	  	  Map.put("0","请选择");
  	          for  ( Iterator i = areaMap.elementIterator(); i.hasNext(); ) {
    	        Element element = (Element) i.next();
    	        String myKey="";
    	        String myVal="";
    	          for  ( Iterator j = element.elementIterator(); j.hasNext(); ) {
    	    	    Element myElement = (Element) j.next();
    	    	    if ("int".equals(myElement.getName())) {
    	    	    	myKey = myElement.getText();
    	    	    }
    	    	    if ("string".equals(myElement.getName())) {
    	    	    	myVal = myElement.getText();
    	    	    }
    	          }
      	  	    Map.put(myKey,myVal);
  	          }
  	      } else {
        	  	Map.put("0","出错了");
    	      }


     	} catch (Exception e) {
    	  	Map.put("0","出错了");
     		e.printStackTrace();
	    }
		return Map;
	}
	

     public String utf82gbk(String v) throws UnsupportedEncodingException { 
    	 String unicode = new String(v.getBytes(),"ISO-8859-1");   
    	 //System.out.println(unicode.length()); 
    	 //System.out.println(unicode.getBytes().length); 
    	 String gbk = new String(unicode.getBytes("ISO-8859-1"),"GBK");  
    	 //System.out.println(gbk.length()); 
    	 //System.out.println(gbk.getBytes().length); 
    	 return gbk;
    } 

	public Map<String,String> getDirectSrvInfo(String areaName,int cardId,int priceId) {
		Map<String, String> Map = new LinkedHashMap<String, String>();
		ResourceBundle bundle=PropertyResourceBundle.getBundle("config");
    	try {   
		  SimpleDateFormat ReqTime =new SimpleDateFormat("yyyyMMddHHmmss");
		  Date d = new Date();
  		  StringBuilder sign = new StringBuilder();
  		  sign.append(bundle.getString("dPosTermNo"));
  		  sign.append(String.valueOf(cardId));
  		  sign.append(String.valueOf(priceId));
  		  sign.append(areaName);
  		  sign.append(ReqTime.format(d));
  		  sign.append(MD5Utils.getGBKMD5Str(bundle.getString("dPosKey")));
		  String signStr = MD5Utils.getGBKMD5Str(sign.toString());
  		  StringBuilder builder = new StringBuilder();
  		  builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
  		  builder.append("<getDirectSrvInfo>");
  		  builder.append("<terminalNo>"+bundle.getString("dPosTermNo")+"</terminalNo>");
  		  builder.append("<cardId>"+String.valueOf(cardId)+"</cardId>");
  		  builder.append("<priceId>"+String.valueOf(priceId)+"</priceId>");
  		  builder.append("<areaName>"+areaName+"</areaName>");
  		  builder.append("<requestTime>"+ReqTime.format(d)+"</requestTime>");
  		  builder.append("<sign>"+signStr+"</sign>");
  		  builder.append("</getDirectSrvInfo>");
  		  TerminalServiceXmlServiceStub ePay = new TerminalServiceXmlServiceStub();
  		  Options options = ePay._getServiceClient().getOptions();
  		  options.setProperty(org.apache.axis2.Constants.Configuration.CHARACTER_SET_ENCODING, "UTF-8");
  		  ePay._getServiceClient().setOptions(options);
  		  net.wit.webservice.TerminalServiceXmlServiceStub.GetDirectSrvInfo in = new net.wit.webservice.TerminalServiceXmlServiceStub.GetDirectSrvInfo();
  		  org.apache.axis2.databinding.types.soapencoding.String instr = new org.apache.axis2.databinding.types.soapencoding.String();
  		  instr.setString(builder.toString());
  		  in.setRequestXml(instr);
  		  GetDirectSrvInfoResponse out = ePay.getDirectSrvInfo(in);
  		  Document document = DocumentHelper.parseText(out.getGetDirectSrvInfoReturn().getString());   
  		  Map<String, String> respMap = new LinkedHashMap<String, String>();
  		  Element srvMap = null;
  	      Element root = document.getRootElement();
  	      for  ( Iterator i = root.elementIterator(); i.hasNext(); ) {
  	            Element element = (Element) i.next();
  	            if (element.getName()=="gameSrv") {
  	            	srvMap = element;
  	            } else
  	            {
  	                respMap.put(element.getName(), element.getText());
  	            }
  	         }
  	      if ("0".equals(respMap.get("respCode"))) {
    	  	  Map.put("0","请选择");
  	          for  ( Iterator i = srvMap.elementIterator(); i.hasNext(); ) {
    	        Element element = (Element) i.next();
    	        String myKey="";
    	        String myVal="";
    	          for  ( Iterator j = element.elementIterator(); j.hasNext(); ) {
    	    	    Element myElement = (Element) j.next();
    	    	    if ("int".equals(myElement.getName())) {
    	    	    	myKey = myElement.getText();
    	    	    }
    	    	    if ("string".equals(myElement.getName())) {
    	    	    	myVal = myElement.getText();
    	    	    }
    	          }
      	  	    Map.put(myKey,myVal);
  	          }
  	      } else {
       	  	Map.put("0","出错了");
  	      }

     	} catch (Exception e) {
    	  	Map.put("0","出错了");
     		e.printStackTrace();
	    }
		return Map;
	}
}
