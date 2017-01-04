package net.wit.uic.api;

import java.io.InputStream;
import java.util.Properties;

import org.springframework.core.io.ClassPathResource;

public final class UICConfig {
	/**
	 * 用户中心接口
	 * @author Administrator
	 *
	 */
	public static class UC {
		
		private static Properties getProperties(String url) throws Exception {
			InputStream in = new ClassPathResource(url).getInputStream();
			Properties p = new Properties();
			p.load(in);
			return p;
		}
		/**
		 * 用户中心地址
		 */
		public static String getUcUrl() {
			try {
				Properties p = getProperties("wit.properties");
				return p.getProperty("vbox.ucServer");
			} catch (Exception e) {
				e.printStackTrace();
				return "";
			}
		}
		/** 发送手机验证码 */
		public static final String sendMobileUrl = getUcUrl() + "/uic/user/send_mobile.jhtml";
		/** 发送邮箱验证码 */
		public static final String sendMailUrl = getUcUrl() + "/uic/user/send_email.jhtml";
		/** 检测验证码是否正确 */
		public static final String checkCaptchaUrl = getUcUrl() + "/uic/user/check_captcha.jhtml";
		/** 检测验证码是否正确 */
		public static final String checkPaymentPassword = getUcUrl() + "/sso/check-password.jhtml";
		/** 注册提交 */
		public static final String registerUrl = getUcUrl() + "/uic/user/register.jhtml";
		/** 修改密码 */
		public static final String updatePassUrl = getUcUrl() + "/uic/user/updatePass.jhtml";
		/** 绑定保存 */
		public static final String bindUpdateUrl = getUcUrl() + "/uic/user/bindUpdate.jhtml";
		/** 修改支付密码 */
		public static final String updatePaymentPassUrl = getUcUrl() + "/uic/user/updatePaymentPass.jhtml";
		/** 实名认证 */
		public static final String idcardSaveUrl = getUcUrl() + "/uic/user/idcardSave.jhtml";
		/** 修改用户信息 */
		public static final String updateUserInfoUrl = getUcUrl() + "/uic/user/editInfo.jhtml";
		/** 找回密码 */
		public static final String backPassUrl = getUcUrl() + "/uic/user/retrievePass.jhtml";
		/** 找回支付密码 */
		public static final String backPaymentPassUrl = getUcUrl() + "/uic/user/retrievePaymentPass.jhtml";
		/** 通过会话获取用户信息 */
		public static final String getUserInfoUrl = getUcUrl() + "/uic/user/info.jhtml";
		/** 获取登录校验码 */
		public static final String getAuthUrl = getUcUrl() + "/sso/auth-get.jhtml";
		/** 用户登录 */
		public static final String loginUrl = getUcUrl() + "/sso/user-login.jhtml";
		/** 通过令牌获取用户信息 */
		public static final String getUserUrl = getUcUrl() + "/sso/user-get.jhtml";
		/** 通过令牌获取用户信息 */
		public static final String getToken = getUcUrl() + "/sso/token-get.jhtml";
		/** 通过令牌登录 */
		public static final String tokenLogin = getUcUrl() + "/sso/token-login.jhtml";
		
		
	}
	
	public static final String generateKey = "uic.vsstoo$$.com";

	public static final String AUTH_GENERATE_KEY = "generateKey";

	public static final class Cookies {
		public static final String HOST = "host";

		public static final String UC_TOKEN = "uctoken";
	}
}
