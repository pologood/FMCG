/**
 * ====================================================
 * 文件名称: PushMessage.java
 * 修订记录：
 * No    日期				作者(操作:具体内容)
 * 1.    2014年8月23日			Administrator(创建:创建文件)
 * ====================================================
 * 类描述：(说明未实现或其它不应生成javadoc的内容)
 */
package net.wit.support;

import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.push.model.v20150827.PushRequest;
import com.aliyuncs.push.model.v20150827.PushResponse;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import net.wit.util.JsonUtils;
import net.wit.util.RandomGUID;
import org.apache.commons.net.util.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.Socket;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;


/**
 * @author Administrator
 * @ClassName: PushMessage
 * @Description: 信息推送
 * @date 2014年8月23日 下午2:26:28
 */

@Slf4j
public class PushMessage {

    private static String COMMAND = "MESSAGE";
    public static final String ALERT = "Test from API Example - alert";

    public static String getFixedLenString(String str, int len) throws UnsupportedEncodingException {
        int strlen = str.getBytes("GBK").length;
        if (strlen == 0) {
            str = "";
        }
        if (strlen == len) {
            return str;
        }
        if (strlen > len) {
            return str.substring(0, len);
        }
        String s = str;
        while (strlen < len) {
            s = s + "*";
            strlen = strlen + 1;
        }
        return s;
    }

    /*
     * 向极光推送注册用户
     */
    @SuppressWarnings("deprecation")
    public static boolean jpush_register(String username, String password) {
        String result = null;
        HttpClient httpClient = new DefaultHttpClient();
        try {
            //读取配置文件
            ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
            String pushKey = bundle.getString("pushKey");
            String pushSecret = bundle.getString("pushSecret");
            //base64加密算法
            Base64 base64 = new Base64();
            String base64_encode = base64.encodeToString((pushKey + ":" + pushSecret).
                    getBytes()).replace("\r\n", "");
            HttpPost httpPost = new HttpPost("https://api.im.jpush.cn/v1/users/");
            httpPost.addHeader("Authorization", "Basic " + base64_encode);
            httpPost.addHeader("Content-Type", "application/json");
            String jso = "[{'username':" + username + ",'password':" + password + "}]";
            httpPost.setEntity(new StringEntity(jso.toString(), "utf-8"));
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = (HttpEntity) httpResponse.getEntity();
            result = EntityUtils.toString(httpEntity);
//			System.out.println(result);
            JSONObject js = JSON.parseObject(JSON.parseArray(result).toArray()[0].toString());
            if (js.containsKey("error")) {
                String error_code = js.get("error").toString();
                if (error_code.contains("899001")) {
                    return true;
                } else {
                    return false;
                }
            } else {
                EntityUtils.consume(httpEntity);
                return true;
            }
        } catch (ClientProtocolException e) {
            //e.printStackTrace();
            //System.out.println("PushMessage--->jpush_register 出错 ");
            return false;
        } catch (Exception e) {
            //e.printStackTrace();
            //System.out.println("PushMessage--->jpush_register 出错 ");
            return false;
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
    }


    /*
     * 向环信推送注册用户
     */
    @SuppressWarnings("deprecation")
    public static boolean ease_register(String username, String password, String nickname) {
        String result = null;
        HttpClient httpClient = new DefaultHttpClient();
        try {
            ResourceBundle bundle = PropertyResourceBundle.getBundle("config");

            //读取配置文件
            String easeKey = bundle.getString("easeKey");
            String easeSecret = bundle.getString("easeSecret");
            String easeAppId = bundle.getString("easeAppId");

            HttpPost httpPost = new HttpPost("https://a1.easemob.com/" + easeAppId + "/token");
            httpPost.addHeader("Content-Type", "application/json");
            String jso = "{\"grant_type\":\"client_credentials\",\"client_id\":\"" + easeKey + "\",\"client_secret\":\"" + easeSecret + "\"}";
            httpPost.setEntity(new StringEntity(jso, "utf-8"));
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = (HttpEntity) httpResponse.getEntity();
            result = EntityUtils.toString(httpEntity);

            System.out.println("result=" + result);

            JSONObject js = JSON.parseObject(result);
            String token = js.get("access_token").toString();

            HttpPost httpUser = new HttpPost("https://a1.easemob.com/" + easeAppId + "/users");
            httpUser.addHeader("Authorization", "Bearer " + token);
            httpUser.addHeader("Content-Type", "application/json");
            String jsuser = "[{\"username\":\"" + username + "\",\"password\":\"" + password + "\",\"nickname\":\"" + nickname + "\"}]";
            httpUser.setEntity(new StringEntity(jsuser, "utf-8"));
            HttpResponse usersResponse = httpClient.execute(httpUser);
            HttpEntity userEntity = (HttpEntity) usersResponse.getEntity();
            result = EntityUtils.toString(userEntity);

            JSONObject ujs = JSON.parseObject(result);
            //System.out.println("环信注册result:"+result);
            if (ujs.containsKey("entities")) {
                return true;
            } else {
                return ujs.getString("error").equals("duplicate_unique_property_exists");
            }
        } catch (ClientProtocolException e) {
            //e.printStackTrace();
            // System.out.println("PushMessage--->ease_register 出错 ");
            return false;
        } catch (Exception e) {
            //e.printStackTrace();
            //System.out.println("PushMessage--->ease_register 出错 ");
            return false;
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
    }


    public static boolean send(String msg, String url, String user) throws UnsupportedEncodingException {
        // String gbk = new String(msg.getBytes("GBK"),"GBK");
        // System.out.print(gbk.getBytes("GBK").length);
        if (user == null) {
            return false;
        }
        RandomGUID myGUID = new RandomGUID();
        String id = myGUID.toString() + "00";
        StringBuffer sb = new StringBuffer();
        // command 10
        char len = 7;
        sb.append(len).append(getFixedLenString(COMMAND, 10));
        len = 4;
        // myUsername 25
        sb.append(len).append(getFixedLenString("java", 25));
        len = (char) user.length();
        // ReceiverName 25
        sb.append(len).append(getFixedLenString(user, 25));
        len = (char) msg.getBytes("GBK").length;
        // msg 255
        sb.append(len).append(getFixedLenString(msg, 255));
        len = (char) id.length();
        // id 36
        sb.append(len).append(getFixedLenString(id, 36));
        len = (char) url.getBytes("GBK").length;
        // url 255
        sb.append(len).append(getFixedLenString(url, 255));

        try {
            ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
            @Cleanup
            Socket client = new Socket(bundle.getString("imhost"), new Integer(bundle.getString("import")));
            // Socket client = new Socket("localhost",new Integer(bundle.getString("import")));
            client.setSoLinger(true, 30000);
            @Cleanup
            Writer writer = new OutputStreamWriter(client.getOutputStream(), "GBK");
            writer.write(sb.toString());
            writer.flush();
            client.close();
            return true;
        } catch (Exception e) {
            //e.printStackTrace();
            //System.out.println("PushMessage--->send 出错 ");
        }
        return false;
    }

    public static boolean jpush(net.wit.entity.Message message) {
        MessageModel model = new MessageModel();
        model.id = message.getId();
        model.msg = message.getContent();
        if (message.getReceiver() == null) {
            return false;
        }
        model.title = message.getTitle();
        model.type = message.getType();
        if (message.getTrade() != null) {
            model.setSid(message.getTrade().getId());
        }
        model.setCreate_date(message.getCreateDate());
        return PushMessage.jpush(model, message.getReceiver().getUsername());
    }

    public static boolean jpush(MessageModel model, String recver) {
        try {
            ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
            JPushClient jpushClient = new JPushClient(bundle.getString("pushSecret"), bundle.getString("pushKey"), 3);
            Audience audience = null;
            if (recver == null) {
                audience = Audience.all();
            } else {
                audience = Audience.alias(recver);
            }
            Message message = Message.content(JsonUtils.toJson(model));
            PushPayload iosPayload =
                    PushPayload.newBuilder()
                            .setPlatform(Platform.ios())
                            .setAudience(audience)
                            .setNotification(Notification.newBuilder()
                                    .addPlatformNotification(IosNotification.newBuilder()
                                            .incrBadge(1)
                                            .addExtra("id", model.getId())
                                            .addExtra("create_date", Long.toString(model.getCreate_date().getTime()))
                                            .addExtra("sid", model.getSid())
                                            .addExtra("msg", model.getMsg())
                                            .addExtra("title", model.getTitle())
                                            .addExtra("type", model.getType().toString())
                                            .setAlert(model.msg)
                                            .setSound("default")
                                            .build())
                                    .build())
                            .setOptions(Options.newBuilder()
                                    .setApnsProduction(true)
                                    .build())
                            .build();
            PushPayload andriodPayload =
                    PushPayload.newBuilder()
                            .setPlatform(Platform.android())
                            .setAudience(audience)
                            .setMessage(message)
                            .setOptions(Options.newBuilder()
                                    .setApnsProduction(true)
                                    .build())
                            .build();

            try {
                PushResult iosResult = jpushClient.sendPush(iosPayload);
            } catch (Exception e) {
                //e.printStackTrace();
                return false;
            }
            try {
                PushResult androidResult = jpushClient.sendPush(andriodPayload);
            } catch (Exception e) {
                //e.printStackTrace();
                return false;
            }

        } catch (Exception e) {
            //e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 阿里云推送
     *
     * @param message 消息
     */
    public static boolean aliPush(net.wit.entity.Message message, Long unReadCount) {
        if (message.getReceiver() == null) return false;

        MessageModel model = new MessageModel();
        model.setId(message.getId());
        model.setMsg(message.getContent());
        model.setTitle(message.getTitle());
        model.setType(message.getType());
        model.setCreate_date(message.getCreateDate());
        if (message.getTrade() != null) {
            model.setSid(message.getTrade().getId());
        }
        if (message.getWay() == null) {
            message.setWay(net.wit.entity.Message.Way.all);
        }

        if (unReadCount == null) {
            unReadCount = 0L;
        }
        unReadCount++;

        String receiver = message.getReceiver().getId().toString();

        if (message.getWay() == net.wit.entity.Message.Way.all) {
            return PushMessage.aliPush(model, receiver, net.wit.entity.Message.Way.member, unReadCount, "account", 3) &&
                    PushMessage.aliPush(model, receiver, net.wit.entity.Message.Way.tenant, unReadCount, "account", 3);
        }
        return PushMessage.aliPush(model, receiver, message.getWay(), unReadCount, "account", 3);
    }

    /**
     * 阿里云推送高级接口
     */
    public static boolean aliPush(MessageModel message, String receiver, net.wit.entity.Message.Way way, Long unReadCount, String target, Integer deviceType) {
        try {
            ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
            Long appKey = null;
            if (way == net.wit.entity.Message.Way.member) {
                appKey = Long.parseLong(bundle.getString("ali-appKey"));
            } else if (way == net.wit.entity.Message.Way.tenant) {
                appKey = Long.parseLong(bundle.getString("ali-b-appKey"));
            }
            String accessKeyId = bundle.getString("ali-accessKeyId");
            String accessKeySecret = bundle.getString("ali-accessKeySecret");

            IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
            DefaultAcsClient client = new DefaultAcsClient(profile);

            PushRequest pushRequest = new PushRequest();
            // 推送目标
            pushRequest.setAppKey(appKey);
            pushRequest.setTarget(target); //推送目标: device:推送给设备; account:推送给指定帐号,tag:推送给自定义标签; all: 推送给全部
            pushRequest.setTargetValue(receiver); //根据Target来设定，如Target=device, 则对应的值为 设备id1,设备id2. 多个值使用逗号分隔.(帐号与设备有一次最多100个的限制)
            pushRequest.setDeviceType(deviceType); // 设备类型deviceType 取值范围为:0~3. iOS设备: 0; Android设备: 1; 全部: 3, 这是默认值.

            // 推送配置
            pushRequest.setType(1); // 0:表示消息(默认为0), 1:表示通知
            pushRequest.setTitle(message.getTitle()); // Android消息标题 | Android通知标题 | iOS消息标题，最长20个字符，中文为1个字符
            pushRequest.setBody(message.getMsg()); // Android消息内容 | Android通知内容 | iOS消息内容
            pushRequest.setSummary(message.getMsg()); // iOS通知内容

            // 推送配置: iOS
            pushRequest.setiOSBadge(unReadCount.toString()); // iOS应用图标右上角角标
            pushRequest.setiOSMusic("default"); // iOS通知声音
            pushRequest.setiOSExtParameters("{\"type\":\"" + message.getType() + "\",\"id\":\"" + message.getId() + "\",\"create_date\":\"" + message.getCreate_date() + "\",\"sid\":\"" + message.getSid() + "\"}"); //自定义的kv结构,开发者扩展用 针对iOS设备
//            pushRequest.setApnsEnv("DEV");
            pushRequest.setRemind(true); // 当APP不在线时候，是否通过通知提醒

            // 推送配置: Android
            pushRequest.setAndroidOpenType("1"); // 点击通知后动作,1:打开应用 2: 打开应用Activity 3:打开 url
//		    pushRequest.setAndroidOpenUrl("http://www.baidu.com"); // Android收到推送后打开对应的url,仅仅当androidOpenType=3有效
            pushRequest.setAndroidExtParameters("{\"_NOTIFY_TYPE_\":\"2\",\"type\":\"" + message.getType() + "\",\"id\":\"" + message.getId() + "\",\"create_date\":\"" + message.getCreate_date() + "\",\"sid\":\"" + message.getSid() + "\"}"); // 设定android类型设备通知的扩展属性（目前android通知的提醒方式的设置在该参数里面:“_NOTIFY_TYPE_=1\2\3”,1：振动，2：声音，3:声音和振动）

            // 推送控制
//		    final Date pushDate = new Date(System.currentTimeMillis() + 3600 * 1000); // 一小时后发送, 也可以设置成你指定固定时间
//		    final String pushTime = ParameterHelper.getISO8601Time(pushDate);
//		    pushRequest.setPushTime(pushTime); // 延后推送。可选，如果不设置表示立即推送
            pushRequest.setStoreOffline(true); // 离线消息是否保存,若保存, 在推送时候，用户即使不在线，下一次上线则会收到
//			final String expireTime = ParameterHelper.getISO8601Time(new Date(System.currentTimeMillis() + 12 * 3600 * 1000)); // 12小时后消息失效, 不会再发送
//			pushRequest.setExpireTime(expireTime);
//			pushRequest.setBatchNumber("100010"); // 批次编号,用于活动效果统计. 设置成业务可以记录的字符串

            PushResponse pushResponse = client.getAcsResponse(pushRequest);
//            System.out.printf("RequestId: %s, ResponseId: %s, message: %s\n",
//                    pushResponse.getRequestId(), pushResponse.getResponseId(), pushResponse.getMessage());
        } catch (Exception e) {
            // System.out.print("云推送接口调用失败：");
            //e.printStackTrace();
            return false;
        }
        return true;
    }


    public static void aliSendSms() {
        //TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
        //AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
        //req.setExtend("123456");
        //req.setSmsType("normal");
        //req.setSmsFreeSignName("阿里大于");
        //req.setSmsParamString("{\"code\":\"1234\",\"product\":\"alidayu\"}");
        //req.setRecNum("13000000000");
        //req.setSmsTemplateCode("SMS_585014");
        //AlibabaAliqinFcSmsNumSendResponse rsp = client.execute(req);
    }

    public static void main(String[] args) throws Exception {
//		PushMessage pushMessage=new PushMessage();
        MessageModel model = new MessageModel();
//		model.setId(1L);
//		model.msg = "2233223";
//		model.title = "账单提醒";
        //model.type = Message.Type.account;
        //pushMessage.jpush(model);
        model.setTitle("title");
        model.setMsg("通知推送测试");
        model.setType(net.wit.entity.Message.Type.account);

//        aliPush(model, "13335553232", "all", 3);

    }

    @Test
    public void easeTest() {
        MessageModel model = new MessageModel();
        model.msg = "1";
        model.title = "1";
        model.type = net.wit.entity.Message.Type.order;
        PushMessage.aliPush(model, "370", net.wit.entity.Message.Way.tenant, 1L, "account", 3);
        PushMessage.aliPush(model, "370", net.wit.entity.Message.Way.member, 1L, "account", 3);
//        System.out.println(ease_register("9994","rzico@2015","测试账号"));
    }
}
