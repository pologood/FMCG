/**
 * @Title：HttpSupport.java 
 * @Package：net.wit.support 
 * @Description：
 * @author：Chenlf
 * @date：2015年2月14日 下午4:41:32 
 * @version：V1.0   
 */

package net.wit.support;

import org.apache.http.Header;
import org.apache.http.HttpResponse;

/**
 * @ClassName：HttpSupport
 * @Description：
 * @author：Chenlf
 * @date：2015年2月14日 下午4:41:32
 */
public class HttpSupport {

	public static String getJsessionid(HttpResponse httpResponse) {
		String JSESSIONID = "";
		Header[] heard = httpResponse.getHeaders("Set-Cookie");
		for (int i = 0; i < heard.length; i++) {
			if (heard[i].getValue().contains("JSESSIONID")) {
				JSESSIONID = heard[i].getValue();
				break;
			}
		}
		return JSESSIONID;
	}
}
