package net.wit.webservice;

import java.io.IOException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class AliSendSms {
    public static void send(String mobile, String data, String template) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
            if (!bundle.containsKey("x-ca-key")) {
                return;
            }
            if (!bundle.containsKey("x-ca-secret")) {
                return;
            }
            //	请求地址
            HttpUriRequest httpGet = RequestBuilder
                    .get()
                    .setUri("https://ca.aliyuncs.com/gw/alidayu/sendSms")
                    .addHeader("X-Ca-Key", bundle.getString("x-ca-key"))
                    .addHeader("X-Ca-Secret", bundle.getString("x-ca-secret"))
                    .addParameter("rec_num", mobile)
                    .addParameter("sms_template_code", template)
                    .addParameter("sms_free_sign_name", bundle.getString("signature"))
                    .addParameter("sms_type", "normal")
                    .addParameter("extend", "1")
                    .addParameter("sms_param", data)
                    .build();

            //	TODO	设置请求超时时间
            //	处理请求结果
            ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
                @Override
                public String handleResponse(final HttpResponse response) throws IOException {
                    int status = response.getStatusLine().getStatusCode();
                    HttpEntity entity = response.getEntity();
                    return entity != null ? EntityUtils.toString(entity) : null;
                }
            };
            //	发起 API 调用
            String responseBody = httpClient.execute(httpGet, responseHandler);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
 