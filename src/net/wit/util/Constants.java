package net.wit.util;

import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.springframework.core.io.ClassPathResource;

/**
 * 常量
 * 
 * @author Administrator
 * 
 */
public final class Constants {

	public static final String PASSWORD_INIT = "123456"; // 初始密码

	public static final String TOP_ZONE = "000000000000000000"; // 顶级区域

	public static final String PAGESIZE = "10"; // 分页每页大小
	public static final String PAGESIZE_PHONE = "10"; // 手机分页每页大小

	public static final String PARENT_ID = "-1";// 顶级父类id

	// ************流水号工具start****************
	public static final String SNO_INIT = "900"; // 初始化流水号
	// ************流水号工具end****************

	// ************日期格式化start***************
	public static final SimpleDateFormat DEFAULTDATEFORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static final SimpleDateFormat YMDHMFORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	public static final SimpleDateFormat YMDFORMAT = new SimpleDateFormat("yyyy-MM-dd");
	public static final SimpleDateFormat YMFORMAT = new SimpleDateFormat("yyyy-MM");
	public static final SimpleDateFormat YFORMAT = new SimpleDateFormat("yyyy");
	public static final DecimalFormat INTFORMT = new DecimalFormat("0");
	public static final DecimalFormat ONEDEFORMT = new DecimalFormat("0.0");
	public static final DecimalFormat THREEDEFORMT = new DecimalFormat("0.000");
	public static final String emptyDate = "1970-01-01 00:00:00";
	
	/**
	 * 每个月天数  30天
	 */
	public static final int DAYSOFMONTH = 30;
	public static final int DAY_TY=3;

	// ************日期格式化end***************

	public static final String THUMB = "thumb"; // 缩略图名称
	public static final int THUMB_WIDTH = 120; // 缩略图宽度
	public static final int THUMB_HEIGHT = 120; // 缩略图高度
	public static final String SUFFIX = ".jpg"; // 缩略图高度

	/**
	 * 原图地址转化成缩略图地址
	 * 
	 * @param url
	 *            原图地址 如 WEB-INF\resource\2013\9\137816948156838910.png
	 * @return 缩略图地址 如 WEB-INF\resource\2013\9\137816948156838910_thumb.png
	 */
	public static String convertToThumbUrl(String url) {
		if (StringUtils.isNotEmpty(url)) {
			String thumbTarget = url.substring(url.indexOf("."));// 取后缀
			url = url.substring(0, url.indexOf("."));// 取.之前的
			url += "_" + Constants.THUMB + thumbTarget;// 加入thumb拼接
			return url;
		}
		return "";
	}

	public static final double DISTANCE = 500000; // 默认距离

	// *************资源位置start*******************
	public static String getSeparator() {
		return SEPARATOR;
	}

	public static final String SEPARATOR = System.getProperty("file.separator");
	public static final String OSNAME = System.getProperty("os.name");

	private static final String SECURITY_RES = "WEB-INF" + SEPARATOR + "resource";

	private static final String SECURITY_UPLOAD = "upload" + SEPARATOR;

	private static final String SECURITY_TEMP = SECURITY_RES + SEPARATOR + "temp" + SEPARATOR;
	private static final String SECURITY_ATTACH = SECURITY_RES + SEPARATOR + "attach" + SEPARATOR;
	private static final String SECURITY_AUTHORIZE = SECURITY_RES + SEPARATOR + "authorize" + SEPARATOR;

	public static final String IMAGE_DEFAULT = SECURITY_RES + SEPARATOR + "default.jpg"; // 默认图片路径

	public static String getSecurityRes() {
		return SECURITY_RES;
	}

	public static String getSecurityUpload() {
		return SECURITY_UPLOAD;
	}

	public static String getSecurityTemp() {
		return SECURITY_TEMP;
	}

	public static String getSecurityAttach() {
		return SECURITY_ATTACH;
	}

	public static String getSecurityAuthorize() {
		return SECURITY_AUTHORIZE;
	}

	// *************资源位置end*******************

	// *********获取属性文件Start**********
	private static String CACHEPREFIX;

	public static String getCachePrifix() {
		if (CACHEPREFIX != null) {
			return CACHEPREFIX;
		} else {
			String cachePrefix = "SUPERVISE_MEM";
			try {
				Properties p = getProperties("config/jdbc.properties");
				cachePrefix = p.getProperty("mem_cache_prefix");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return cachePrefix;
		}
	}

	private static Properties getProperties(String url) throws Exception {
		InputStream in = new ClassPathResource(url).getInputStream();
		Properties p = new Properties();
		p.load(in);
		return p;
	}

	// *********获取属性文件end**********

	// *********日期方法start************
	private static Date MINDATE;
	private static Date MAXDATE;

	public static Date getMinDate() {
		if (MINDATE != null) {
			return MINDATE;
		} else {
			Date minDate = null;
			try {
				minDate = DEFAULTDATEFORMAT.parse("1970-01-01 00:00:00");
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return minDate;
		}
	}

	public static Date getMaxDate() {
		if (MAXDATE != null) {
			return MAXDATE;
		} else {
			Date maxDate = null;
			try {
				maxDate = DEFAULTDATEFORMAT.parse("9999-12-31 00:00:00");
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return maxDate;
		}
	}

	public static Date getFirstDayOfMth(Calendar calendar) {
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		Date fistDate = calendar.getTime();
		String dateTemp = Constants.YMDFORMAT.format(fistDate);
		try {
			fistDate = DEFAULTDATEFORMAT.parse(dateTemp + " 00:00:00");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return fistDate;
	}

	public static Date getLastDayOfMth(Calendar calendar) {
		int value = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		calendar.set(Calendar.DAY_OF_MONTH, value);
		Date lastDate = calendar.getTime();
		String dateTemp = Constants.YMDFORMAT.format(lastDate);
		try {
			lastDate = DEFAULTDATEFORMAT.parse(dateTemp + " 23:59:59");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return lastDate;
	}

	// *********日期方法end************

	/**
	 * 调度地址
	 */
	public static String getDspUrl() {
		try {
			Properties p = getProperties("wit.properties");
			return p.getProperty("vbox.dspServer");
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	/**
	 * V盟地址
	 */
	public static String getCrmUrl() {
		try {
			Properties p = getProperties("wit.properties");
			return p.getProperty("vbox.crmServer");
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	/**
	 * V盒地址
	 */
	public static String getVboxUrl() {
		try {
			Properties p = getProperties("wit.properties");
			return p.getProperty("vbox.vboxServer");
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	/**
	 * Video接口
	 * @author Administrator
	 *
	 */
	public static class Vbox {
		public static final String getAddStoreURL = getVboxUrl()+"/serve/sto/store_add.html";
	}
	
	/**
	 * Video接口
	 * @author Administrator
	 *
	 */
	public static class Video {
		public static final String videoAddrURL = "http://$[IP]/GetVideoAddressHandler?sxtid=$[CAMERAID]";
		public static final String quickConnURL = "http://$[IP]/ConfirmConnectVCHandler?sxtid=$[CAMERAID]&result=$[RESULT]";
		public static final String ptzCtrlURL = "http://$[IP]/ControlSxtHandler?sxtid=$[CAMERAID]&type=$[TYPE]&param=$[PARAM]&speed=$[SPEED]";
		public static final String getCameraURL = getDspUrl() + "/outsideCall/video.action?method=controlCenterUrl4vbox";
		public static final String playerVersionURL = getDspUrl() + "/videoApis/commons.apis?method=playerVersion";

		public static final String getVideoURL = getDspUrl()+"/search.action?method=userSystemInfo";
		public static final String videoListUrl = "http://$[IP]/outsideCall/video.action?method=getCameraInfoByEquId&equId=$[EQUID]";
		public static final String getCameraListUrl = "http://$[IP]/outsideCall/video.action?method=getCameraInfoByUsername&username=$[USERNAME]";
		public static final String getCameraInstallStatusUrl = "http://$[IP]/outsideCall/video.action?method=getCameraInstallStatus&sxtIds=$[SXTIDS]&signalTypes=$[SIGNALTYPES]";
		public static final String getCurrentTime = "http://$[IP]/videoApis/commons.apis?method=getSystemTimeInMillis";
		public static final String getNodeUrl = "http://$[IP]/outside/video.action?method=getChildrenOfNode2Vbox";
		//信息
		public static final String getFunctionStatus = "http://$[IP]/outsideCall/video.action?method=getFuncServerByVbox&functionId=$[FUNCTIONID]&userName=$[USERNAME]&equId=$[EQUID]";
		public static final String getAlarmTimeAndStatus = "http://$[IP]/outsideCall/video.action?method=getAlarmTimeAndStatus&equId=$[EQUID]&signalType=$[SIGNALTYPE]";
		public static final String getAlarmSwitchStatus = "http://$[IP]/outsideCall/video.action?method=getAlarmSwitchStatus";
		public static final String getAlarmTimeRange = "http://$[IP]/outsideCall/video.action?method=getAlarmTimeRange";
		public static final String setAlarmSwitch = "http://$[IP]/outsideCall/video.action?method=setAlarmSwitch";
		public static final String setAlarmTimeRange = "http://$[IP]/outsideCall/video.action?method=setAlarmTimeRange";
		public static final String getPreviousMsg = "http://$[IP]/outsideCall/video.action?method=getPreviousMsg";
		public static final String getMsg4NewCnt = "http://$[IP]/outsideCall/video.action?method=getMsg4NewCnt";
		public static final String getNextMsg = "http://$[IP]/outsideCall/video.action?method=getNextMsg";
		public static final String getMsgList4db = "http://$[IP]/outsideCall/video.action?method=getMsgList4db";
		public static final String alarmUrl = "http://$[IP]/outsideCall/video.action";
		//添加或删除客户端记录
		public static final String addClubClientUrl = "http://$[IP]/outsideCall/video.action?method=addClubClient&clubName=$[CLUBNAME]&clientId=$[CLIENTID]&clientType=$[CLIENTTYPE]&appId=$[APPID]&appKey=$[APPKEY]&masterSecret=$[MASTERSECRET]";
		public static final String delClubClientUrl = "http://$[IP]/outsideCall/video.action?method=delClubClient&clubName=$[CLUBNAME]&clientId=$[CLIENTID]&appId=$[APPID]";
		//体验账号
		public static final String getDemonClubUrl = getDspUrl() + "/outsideCall/video.action?method=findDemonClub";
		/** dsp用户登录 */
		public static final String loginUrl = getDspUrl() + "/outsideCall/video.action?method=userLogin";
		/** 激活设备 */
		public static final String activeEquipmentUrl = getDspUrl() + "/outsideCall/equipment.action?method=activeEquipment";
		/** 取消激活设备 */
		public static final String unbindEquipmentUrl = getDspUrl() + "/outsideCall/equipment.action?method=unbind4DVR";
		//判断平台是否拥有视频帐号
		public static final String existUserAndNote = getDspUrl() + "/outsideCall/video.action?method=existUserAndNote";
		//判断平台摄像头信息
		public static final String getCameraInfos = getDspUrl() + "/outsideCall/video.action?method=getCameraInfos";
		//判断平台摄像头节点列表信息
		public static final String getNodesByName = getDspUrl() + "/outsideCall/video.action?method=getNodesByName";
	}

	/**
	 * crm接口
	 * @author Administrator
	 *
	 */
	public static class CRM {
		public static final String registerUrl = getCrmUrl() + "/api/vbox/register.jhtml";
		public static final String updatePwdUrl = getCrmUrl() + "/api/vbox/updatePass.jhtml";
		public static final String backPwdUrl = getCrmUrl() + "/api/vbox/retrievePass.jhtml";
		public static final String getInstallerUrl = getCrmUrl() + "/api/vbox/getTenant.jhtml?id=$[ID]";
		public static final String searchInstallerNearUrl = getCrmUrl() + "/api/vbox/getListToLocation.jhtml?areaCode=$[AREACODE]&location_x=$[LOCATION_X]&location_y=$[LOCATION_Y]&count=$[COUNT]";
		public static final String searchInstallerUrl = getCrmUrl() + "/api/vbox/getTenantList.jhtml?id=$[ID]&name=$[NAME]&count=$[COUNT]";
		public static final String getAreaListUrl = getCrmUrl() + "/api/vbox/getAreaList.jhtml?id=$[ID]&count=$[COUNT]";
		public static final String acceptInstallerUrl = getCrmUrl() + "/api/vbox/accept.jhtml?id=$[ID]&username=$[USERNAME]&score=$[SCORE]&verification=$[VERIFICATION]";
		public static final String getInstallerWtUrl = getCrmUrl() + "/api/vbox/getAuth.jhtml?verification=$[VERIFICATION]";
		public static final String cancelInstallerWtUrl = getCrmUrl() + "/api/vbox/cancelAuth.jhtml?verification=$[VERIFICATION]";
		public static final String getInstallerIntrUrl = getCrmUrl() + "/api/vbox/getIntr.jhtml?id=$[ID]";
		public static final String applyInstallerWtUrl = getCrmUrl() + "/api/vbox/entrust.jhtml?verification=$[VERIFICATION]&id=$[ID]";
		/** 检测设备校验码 */
		public static final String validationUrl = getCrmUrl() + "/api/vbox/validation.jhtml";
	}
	
	public static class FileInfo {
		public static final String VBOX_TEMPLATE_PATH = "/WEB-INF/resource/attach/";
		public static final String SALEREPORT_TEMPLATE_FILEPATH = "/WEB-INF/resource/attach/salereporttemplate.xls";
		public static final String SALEREPORT_TEMPLATE_ID = "/WEB-INF/resource/attach/salereporttemplate_templateid.xls";
		public static final String DAYSALE_TEMPLATE_ID = "/WEB-INF/resource/attach/daysale_template.xls";
		public static final String ZONE_TEMPLATE_ID = "/WEB-INF/resource/attach/zone_template.xls";
		public static final String DAYSALE_TEMPLATE_FILENAME = "批量做销售excel模板.xls";
		public static final String ZONE_TEMPLATE_FILENAME = "区域excel模板.xls";
	}
	
	/**
	 * 支付相关公钥私钥
	 * @author Administrator
	 *
	 */
	public static class KEY {
		//支付宝-商家签名私钥
		public static final String alipay_privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAMVp2qNQS/YZ3QOGkdr3h9myxFVUP8gX2V95mzSID/M21z6trVQ4crZx9ZmUW/HvscI1koiUc3XozmF4LN6mjCzqwJwP5TxX/YEtHE6hM+g2ssB9AL7663Bvv8UdsjCLm6/B8bZYbE1hDRULmeDCg9fDm9dzxI5YxT8dS8bsacbnAgMBAAECgYEAlLrGdSoGwLgFkiVtd9nrqLENR/g8QWTuaQb2TqJ/2c4kZl1067HHgvrifT2P8/uRIe84oduuDVRDQJ3IuEoj6lJ+bvZbZdHd6NDdNXvnR+QGOWY4Fk/7mIdDxqdA9I0xIJpJ3kmrnI8W6w29fR9NpYvSQjokTsRU88Sf2USt23ECQQD64zk2ADDCt5JEe42fJFc48Fx+faBWHPf1v4VQU9ZdsCIpUo7P/Tjs0DfYS+NbifGwwUu6ReKtJYMd973jOJofAkEAyW+tqfLrO9OEONeOOjlQxppMqQXDh97eFYHlVRKXw4eMr8pIq+SyNWky2Z1v+i3nNOnpz67afw02C3kbdG3KOQJAGoh7XXWpsn4djvaOJL2AJ/prnckFcvX/V1MY80Taj+/3vO0JQ3/hTFI9BbgI5H5zPCFFM/7+GRe1hS7VOzmaOwJAd8axM+He4AcIkygRSSu1jTJIrSRhPNRXp+BCNlDE7x4VmJ2mkpd9I+c2tlE4OUG8PilsvvJQt8VqQm4z1gpGeQJAf5pa9CkfgCaPGqkElzwGUB0fg/wXMtxtPU3pw/ECaCYkyMYoqSrrVIYazdvyIW5Hw7exKLXq6LTFtwf9J+htXw==";
		//支付宝-支付宝签名公钥
		public static final String alipay_publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDTWBwKiyFJADr69h0jBWj1UA7sTpsMPqol34Pl4CxwWj7mkG7pm+gWIhCoh5hYO5IbnJet2vybNbLR0D9CqOafHoeq/GK4II7BGyajWHD2vHXSeJosJ5+umOjb3cGHHrCsslsI1P+O9ThIn9WCiVqL3L8xO2fivcZwURA7dyCFnwIDAQAB";
	}
	
	/**
	 * 个推
	 * @author Administrator
	 *
	 */
	public static class PUSH {
		public static final String APPID = "Ek4SGWjEZUAjhpryBApV86";
		public static final String APPKEY = "GmitGXSZOa5beSvNXlJVW2";
		public static final String MASTERSECRET = "EdjXR63M2c5fTgrySEWoF1";
	}
}
