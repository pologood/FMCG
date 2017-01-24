package net.wit.util;

import java.io.IOException;
import java.math.BigDecimal;

import net.sf.json.JSONObject;
import net.wit.entity.Location;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

/**
 * 判断IP地址所属城市
 */
public class LBSUtil {
    private static final double x_pi = 3.14159265358979324 * 3000.0 / 180.0;

    private static final double pi = 3.14159265358979324;

    public static String getAreaFromLocation(String lat, String lng) {
        String result = "000000";
        @SuppressWarnings("resource")
        HttpClient httpClient = new DefaultHttpClient();
        try {
            HttpGet httpGet = new HttpGet("http://api.map.baidu.com/geocoder/v2/?location=" + lat + "," + lng + "+&output=json&ak=keMkSUZaY8i8CPocLM9zeL4F");
            HttpResponse httpResponse = httpClient.execute(httpGet);

            HttpEntity httpEntity = httpResponse.getEntity();
            String jsonStr = EntityUtils.toString(httpEntity);
            EntityUtils.consume(httpEntity);
            JSONObject jsonObject = JSONObject.fromObject(jsonStr);
            if (jsonObject.getString("status").equals("0")) {
                JSONObject jsonObject1 = JSONObject.fromObject(jsonObject.getString("result"));
                JSONObject jsonObject2 = JSONObject.fromObject(jsonObject1.getString("addressComponent"));
                result = jsonObject2.getString("adcode");
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        return result;
    }

    //百度转高德
    public static Location bd_decrypt(Location location) {
        double bd_lat = location.getLat().doubleValue();
        double bd_lon = location.getLng().doubleValue();
        double x = bd_lon - 0.0065, y = bd_lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
        Location gd = new Location();
        gd.setLat(new BigDecimal(z * Math.sin(theta)));
        gd.setLng(new BigDecimal(z * Math.cos(theta)));
        return gd;
    }
}
