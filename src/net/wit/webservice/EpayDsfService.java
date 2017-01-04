package net.wit.webservice;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import net.wit.Message;
import net.wit.entity.PayBank;
import net.wit.webservice.EpayDsfServiceStub.PayToBank;
import net.wit.webservice.EpayDsfServiceStub.PayToBankResponse;
import net.wit.webservice.EpayDsfServiceStub.QueryDetail;
import net.wit.webservice.EpayDsfServiceStub.QueryDetailResponse;
import cn.epaylinks.dsf.ws.client.EpayDsfAPI;
import cn.epaylinks.dsf.ws.client.EpayDsfAPIConfig;
import cn.epaylinks.dsf.ws.client.utils.SecurityHelper;

public class EpayDsfService {
	
	/**
	 * 获取输入参数
	 */
	private String getParamterXml(PayBank payBank) {
		
		StringBuilder builder = new StringBuilder();
		builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		builder.append("<payToBank>");
		builder.append("<header>");
		builder.append("<tranCode>"+payBank.getTranCode()+"</tranCode>");
		builder.append("<termNo>"+payBank.getTermNo()+"</termNo>");
		builder.append("<merNo>"+payBank.getMerNo()+"</merNo>");
		builder.append("<trackNo>"+payBank.getTrackNo()+"</trackNo>");
		builder.append("<reqTime>"+payBank.getReqTime()+"</reqTime>");
		builder.append("<sign></sign>");
		builder.append("</header>");
		builder.append("<dataBody>");
		builder.append("<payCardNo>"+payBank.getPayCardNo()+"</payCardNo>");
		builder.append("<payBankAccNo>"+payBank.getPayBankAccNo()+"</payBankAccNo>");
		builder.append("<payPass>"+payBank.getPayPass()+"</payPass>");
		builder.append("<payWay>"+payBank.getPayWay()+"</payWay>");
		builder.append("<payType>"+payBank.getPayType()+"</payType>");
		builder.append("<toFlag>"+payBank.getToFlag()+"</toFlag>");
		builder.append("<orderNo>"+payBank.getOrderNo()+"</orderNo>");
		builder.append("<amount>"+payBank.getAmount().multiply(new BigDecimal(100)).toBigInteger().toString()+"</amount>");
		builder.append("<tradeDate>"+payBank.getTradeDate()+"</tradeDate>");
		builder.append("<bankCode>"+payBank.getBankCode()+"</bankCode>");
		builder.append("<depositBankNo></depositBankNo>");
		builder.append("<bankAccType>"+payBank.getBankAccType()+"</bankAccType>");
		builder.append("<bankAccProp>"+payBank.getBankAccProp()+"</bankAccProp>");
		builder.append("<bankAccNo>"+payBank.getBankAccNo()+"</bankAccNo>");
		builder.append("<bankAccName>"+payBank.getBankAccName()+"</bankAccName>");
		builder.append("<idType></idType>");
		builder.append("<idNo></idNo>");
		builder.append("<mobNo>"+payBank.getMobNo()+"</mobNo>");
		builder.append("<summary>"+payBank.getSummary()+"</summary>");
		builder.append("</dataBody>");
		builder.append("</payToBank>");
		
		String xml = builder.toString();
		String sign = EpayDsfAPI.caculateSign(xml);
		xml = xml.replaceAll("<sign>[0-9a-zA-Z]*</sign>|<sign\\s*/>", 
				"<sign>" + sign + "</sign>");
		return xml;
		
	}

	private String getParamterQueryXml(PayBank payBank) {
		
		StringBuilder builder = new StringBuilder();
		builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		builder.append("<queryDetail>");
		builder.append("<header>");
		builder.append("<tranCode>"+payBank.getTranCode()+"</tranCode>");
		builder.append("<termNo>"+payBank.getTermNo()+"</termNo>");
		builder.append("<merNo>"+payBank.getMerNo()+"</merNo>");
		builder.append("<trackNo>"+payBank.getTrackNo()+"</trackNo>");
		builder.append("<reqTime>"+payBank.getReqTime()+"</reqTime>");
		builder.append("<sign></sign>");
		builder.append("</header>");
		builder.append("<dataBody>");
		builder.append("<cardNo></cardNo>");
		builder.append("<tranCardNo></tranCardNo>");
		builder.append("<orderNo>"+payBank.getOrderNo()+"</orderNo>");
		builder.append("<batchNo></batchNo>");
		builder.append("<sn></sn>");
		builder.append("<beginDate></beginDate>");
		builder.append("<endDate></endDate>");
		builder.append("<busiCode></busiCode>");
		builder.append("<status></status>");
		builder.append("<sfType></sfType>");
		builder.append("<pageSize></pageSize>");
		builder.append("<nextId></nextId>");
		builder.append("<page></page>");
		builder.append("</dataBody>");
		builder.append("</queryDetail>");
		
		String xml = builder.toString();
		String sign = EpayDsfAPI.caculateSign(xml);
		xml = xml.replaceAll("<sign>[0-9a-zA-Z]*</sign>|<sign\\s*/>", 
				"<sign>" + sign + "</sign>");
		return xml;
		
	}

	public void payToBank(PayBank payBank) {
	    ResourceBundle bundle=PropertyResourceBundle.getBundle("config");
		try {   
			EpayDsfAPIConfig.loadConfig();
			payBank.setTranCode("8010");
			payBank.setTermNo(EpayDsfAPIConfig.terminalNo);
			payBank.setMerNo(EpayDsfAPIConfig.merchantNo);
			SimpleDateFormat ReqTime =new SimpleDateFormat("yyyyMMddHHmmss");
			payBank.setReqTime(ReqTime.format(new Date()));

			SimpleDateFormat tradeDate =new SimpleDateFormat("yyyyMMdd");
			payBank.setTradeDate(tradeDate.format(new Date()));
			payBank.setPayCardNo(bundle.getString("ePayCardNo"));
			payBank.setPayBankAccNo(bundle.getString("ePayBankAccNo"));
			String ePayPwd = bundle.getString("ePayPwd");
			payBank.setPayPass(EpayDsfAPI.desEncrypt(SecurityHelper.getMd5Hex(ePayPwd.getBytes())) );
			payBank.setPayType("0");
			payBank.setPayWay("1");
			payBank.setToFlag("0");
			EpayDsfServiceStub epay = new EpayDsfServiceStub();
			PayToBank in = new PayToBank();
			in.setXml(getParamterXml(payBank));
			PayToBankResponse out = epay.payToBank(in);
			//String resp = EpayDsfAPI.callWs("payToBank",new Object[]{getParamterXml(payBank)});
			String resp = out.get_return();
            if (resp!=null && !resp.isEmpty()) {
		    	Map<String, String> HeaderMap = new LinkedHashMap<String, String>();
		    	HeaderMap = EpayDsfAPI.parseXmlHeader(resp);
		    	payBank.setRespCode(HeaderMap.get("respCode"));
		    	payBank.setRespMsg(HeaderMap.get("respMsg"));
		    	if ("2109".equals(payBank.getRespCode()) ) {
		    		payBank.setRespMsg("当天超银行最大限额，请选择普通及快速汇款");
		    	}
		    	if ("2399".equals(payBank.getRespCode()) ) {
		    		payBank.setRespMsg("当天超银行最大限额，请选择普通及快速汇款");
		    	}
			    if ("0000".equals(payBank.getRespCode()) || "2300".equals(payBank.getRespCode()) ) {
			    	Map<String, String> BodyMap = new LinkedHashMap<String, String>();
			    	BodyMap = EpayDsfAPI.parseXmlBody(resp);
		        	payBank.setRetCode(BodyMap.get("retCode"));
			        payBank.setRetMsg(BodyMap.get("retMsg"));
		        	payBank.setFinishTime(BodyMap.get("finishTime"));
			        payBank.setBusiRefNo(BodyMap.get("busiRefNo"));
			        payBank.setBalance( new BigDecimal(BodyMap.get("balance")).divide(new BigDecimal(100),2,BigDecimal.ROUND_DOWN) );
			        payBank.setServiceFee(new BigDecimal(BodyMap.get("serviceFee")).divide(new BigDecimal(100),2,BigDecimal.ROUND_DOWN) );
		    	}
            } else {
            	payBank.setRespCode("9999");
    	    	payBank.setRespMsg("末知异常");
            }
    	}  catch (Exception e) {
	    	payBank.setRespCode("9999");
	    	payBank.setRespMsg("末知异常");
		}
		
	}
	public Message queryToBank(PayBank payBank) {
		try {   
			EpayDsfAPIConfig.loadConfig();
			SimpleDateFormat ReqTime =new SimpleDateFormat("yyyyMMddHHmmss");
			payBank.setReqTime(ReqTime.format(new Date()));
			EpayDsfServiceStub epay = new EpayDsfServiceStub();
			QueryDetail in = new QueryDetail();
			in.setXml(getParamterQueryXml(payBank));
			QueryDetailResponse out = epay.queryDetail(in);
			//String resp = EpayDsfAPI.callWs("payToBank",new Object[]{getParamterXml(payBank)});
			String resp = out.get_return(); 
//			String resp = EpayDsfAPI.callWs("queryDetail",new Object[]{getParamterQueryXml(payBank)});
	        if (resp!=null && !resp.isEmpty()) {
		     	Map<String, String> HeaderMap = new LinkedHashMap<String, String>();
			   	HeaderMap = EpayDsfAPI.parseXmlHeader(resp);
			    if ("0000".equals(HeaderMap.get("respCode")) ) {
			    	List<Map<String, String>> detailMap = EpayDsfAPI.parseXmlBodyDetail(resp);
			    	
			    	if (detailMap.size()>1) {
			        	return Message.error("查询返回多行结果，查询结果无效");
			    	}
			    	
			    	Map<String,String> map = detailMap.get(0);
			    	
			    	payBank.setRetMsg(map.get("statusMemo"));
			    	payBank.setRetCode(map.get("status"));
			    	
			    	if ("0".equals(map.get("status"))) {
			        	return Message.success("未支付");
			    	} else
			    	if ("1".equals(map.get("status"))) {
			        	return Message.success("交易成功");
			    	} else
			    	if ("2".equals(map.get("status"))) {
			        	return Message.success("交易失败");
			    	} else
			    	if ("4".equals(map.get("status"))) {
			        	return Message.success("处理中");
			    	} else 	{
			        	return Message.error("无效返回码");
			    	} 
			    	
			    } else {
				    return Message.error(HeaderMap.get("respMsg") );
		    	
			    }
	        } else {
	        	return Message.error("查询出现未知异常，请稍候重试");
	        }
		
    	}  catch (Exception e) {
	    	return Message.error("查询出现未知异常，请稍候重试");
		}
	}
}
