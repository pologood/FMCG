package net.wit.weixin.main;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import net.sf.json.JSONObject;
import net.wit.entity.Message;
import net.wit.weixin.pojo.AccessToken;
import net.wit.weixin.util.WeixinUtil;

import javax.annotation.Resource;

/**
 * 菜单管理器类
 *
 * @author Administrator
 */
public class MessageManager {
    public static Boolean sendMsg(String data) {
        try {
            ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
            // 第三方用户唯一凭证
            String appId = bundle.getString("APPID");// 睿商圈
            // String appId = "wxd9cfce3d40f0caf7";//测试号
            // 第三方用户唯一凭证密钥
            String appSecret = bundle.getString("APPSECRET");
            //System.out.println(data);
            // 调用接口创建菜单
            int result = WeixinUtil.sendTemplete(appId, appSecret, data);
            return result == 0;
        } catch (Exception e) {
            return false;
        }
    }
    public static String createCouponTempelete(String openId, String title, String url, String tenantName, String couponName, Integer count, String date, String content) {
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        String templateId = "BQap-b-6M61lwut-ZvGO6YrYz9AfB9VGN8SXJRY_qhw";
        if (bundle.containsKey("couponTemplate")) {
            templateId = bundle.getString("couponTemplate");
        }
        String data = "";
        data += "{\"touser\":\"" + openId + "\",";
        data += "\"template_id\":\"" + templateId + "\",";
        if(url!=null){
            data += "\"url\":\"" + url + "\",";
        }
        data += "\"topcolor\":\"#FF0000\",";
        data += "\"data\":{";
        data += "\"first\": {\"value\":\"" + title + "\",\"color\":\"#FF0000\"},";
        data += "\"keyword1\": {\"value\":\"" + tenantName + "\",\"color\":\"#173177\"},";
        data += "\"keyword2\":{\"value\":\"" + couponName + "\",\"color\":\"#173177\"},";
        data += "\"keyword3\": {\"value\":\"" + count + "\",\"color\":\"#173177\"},";
        data += "\"keyword4\":{\"value\":\"" + date + "\",\"color\":\"#173177\"},";
        data += "\"remark\":{\"value\":\"" + content + "\",\"color\":\"#173177\"}";
        data += "}";
        data += "}";
        return data;
    }
    public static String createOrderTempelete(String openId, String title, String url, String sn, String status, String content, String date) {
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        String templateId = "bZJ0UZaWG_lIEhpLvniFC75einD4CDQHeJveqPg85Ic";
        if (bundle.containsKey("orderTemplate")) {
            templateId = bundle.getString("orderTemplate");
        }
        String data = "";
        data += "{\"touser\":\"" + openId + "\",";
        data += "\"template_id\":\"" + templateId + "\",";
        data += "\"url\":\"" + url + "\",";
        data += "\"topcolor\":\"#FF0000\",";
        data += "\"data\":{";
        data += "\"first\": {\"value\":\"" + title + "\",\"color\":\"#FF0000\"},";
        data += "\"OrderSn\": {\"value\":\"" + sn + "\",\"color\":\"#173177\"},";
        data += "\"OrderStatus\":{\"value\":\"" + status + "\",\"color\":\"#173177\"},";
        data += "\"keyword1\": {\"value\":\"" + sn + "\",\"color\":\"#173177\"},";
        data += "\"keyword2\":{\"value\":\"" + status + "\",\"color\":\"#173177\"},";
        data += "\"keyword3\":{\"value\":\"" + date + "\",\"color\":\"#173177\"},";
        data += "\"remark\":{\"value\":\"" + content + "\",\"color\":\"#173177\"}";
        data += "}";
        data += "}";
        return data;
    }

    public static String createReplyTempelete(String openId, String serviceInfo, String url, String serviceType, String serviceStatus, String time, String content) {
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        String templateId = "tJqSXgU0ghp-wtUfyxvUdNjIvdIkA4guoFQ4XBcv75w";
        if (bundle.containsKey("replyTemplate")) {
            templateId = bundle.getString("replyTemplate");
        }
        String data = "";
        data += "{\"touser\":\"" + openId + "\",";
        data += "\"template_id\":\"" + templateId + "\",";
        data += "\"url\":\"" + url + "\",";
        data += "\"topcolor\":\"#FF0000\",";
        data += "\"data\":{";
        data += "\"serviceInfo\": {\"value\":\"" + serviceInfo + "\",\"color\":\"#FF0000\"},";
        data += "\"serviceType\": {\"value\":\"" + serviceType + "\",\"color\":\"#173177\"},";
        data += "\"serviceStatus\":{\"value\":\"" + serviceStatus + "\",\"color\":\"#173177\"},";
        data += "\"time\":{\"value\":\"" + time + "\",\"color\":\"#173177\"},";
        data += "\"remark\":{\"value\":\"" + content + "\",\"color\":\"#173177\"}";
        data += "}";
        data += "}";
        return data;
    }

    public static String createDepsitTempelete(String openId, String title, String url, String date, String adCharge, String balance, String content) {
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        String templateId = "G2sYA5QUSE5w3gjeuJJhXRml7TYxOqqNKNhAp9XHGq0";
        if (bundle.containsKey("depsitTemplate")) {
            templateId = bundle.getString("depsitTemplate");
        }
        String data = "";
        data += "{\"touser\":\"" + openId + "\",";
        data += "\"template_id\":\"" + templateId + "\",";
        data += "\"url\":\"" + url + "\",";
        data += "\"topcolor\":\"#FF0000\",";
        data += "\"data\":{";
        data += "\"first\": {\"value\":\"" + title + "\",\"color\":\"#FF0000\"},";
        data += "\"date\": {\"value\":\"" + date + "\",\"color\":\"#173177\"},";
        data += "\"adCharge\":{\"value\":\"" + adCharge + "\",\"color\":\"#173177\"},";
        data += "\"type\":{\"value\":\"\",\"color\":\"#FF0000\"},";
        data += "\"cashBalance\":{\"value\":\"" + balance + "\",\"color\":\"#173177\"},";
        data += "\"remark\":{\"value\":\"" + content + "\",\"color\":\"#173177\"}";
        data += "}";
        data += "}";
        return data;
    }
    public  static  String createCertificationTempelete(String openId, String title, String url, String time,String content,String Status){
        ResourceBundle bundle=PropertyResourceBundle.getBundle("config");
        String templateId="KTDVPJ6VMVcpC_KKMOFYcNsqRZ5xKDnW8syXgtuQxQo";
        if(bundle.containsKey("certificationTempelete")){
            templateId=bundle.getString("certificationTempelete");
        }
        String data="";
        data += "{\"touser\":\"" + openId + "\",";
        data += "\"template_id\":\"" + templateId + "\",";
        data += "\"url\":\"" + url + "\",";
        data += "\"topcolor\":\"#FF0000\",";
        data += "\"data\":{";
        data += "\"first\": {\"value\":\"" + title + "\",\"color\":\"#FF0000\"},";
        data += "\"keyword1\": {\"value\":\"" + Status + "\",\"color\":\"#173177\"},";
        data += "\"keyword2\":{\"value\":\"" + time + "\",\"color\":\"#173177\"},";
        data += "\"remark\":{\"value\":\"" + content + "\",\"color\":\"#173177\"}";
        data += "}";
        data += "}";
        return data;
    }
}
