/**
 * @Title：BrowseUtil.java 
 * @Package：net.wit.util 
 * @Description：
 * @author：Chenlf
 * @date：2015年7月12日 上午9:58:09 
 * @version：V1.0   
 */

package net.wit.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName：BrowseUtil
 * @Description：
 * @author：Chenlf
 * @date：2015年7月12日 上午9:58:09
 */
public class BrowseUtil {

	public final static String BROWSE_VERSION = "browse_version";

	public final static String IE9 = "MSIE 9.0";

	public final static String IE8 = "MSIE 8.0";

	public final static String IE7 = "MSIE 7.0";

	public final static String IE6 = "MSIE 6.0";

	public final static String MAXTHON = "Maxthon";

	public final static String QQ = "QQBrowser";

	public final static String WEIXIN = "MicroMessenger";

	public final static String GREEN = "GreenBrowser";

	public final static String SE360 = "360SE";

	public final static String FIREFOX = "Firefox";

	public final static String OPERA = "Opera";

	public final static String CHROME = "Chrome";

	public final static String SAFARI = "Safari";

	public final static String OTHER = "其它";

	public static String checkBrowse(String userAgent) {
		if (regex(WEIXIN, userAgent))
			return WEIXIN;
		return OTHER;
	}
	
	public static  Boolean isWeixin(String userAgent) {
		return regex(WEIXIN, userAgent);
	}

	public static boolean regex(String regex, String str) {
		Pattern p = Pattern.compile(regex, Pattern.MULTILINE);
		Matcher m = p.matcher(str);
		return m.find();
	}

}
