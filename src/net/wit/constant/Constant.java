/**
 *====================================================
 * 文件名称: Constant.java
 * 修订记录：
 * No    日期				作者(操作:具体内容)
 * 1.    2014年6月18日			Administrator(创建:创建文件)
 *====================================================
 * 类描述：(说明未实现或其它不应生成javadoc的内容)
 * 
 */
package net.wit.constant;

/**
 * @ClassName: Constant
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author Administrator
 * @date 2014年6月18日 下午4:22:23
 */
public class Constant {

	public static final Double DEFAULT_DISTANCE = 1000d;

	public static final String UC_HTTP_CLIENT = "uc_http_client";

	public static final String UC_PUBLICKEY = "/register/submit-get.jhtml";

	/** § 1.3 登录 */
	public static final String UC_SSO_LOGIN = "/sso/user-login.jhtml";

	/** § 1.3 获取令牌 */
	public static final String UC_SSO_TOKENGET = "/sso/token-get.jhtml";

	/** § 1.4 通过令牌获取用户信息 */
	public static final String UC_SSO_TOKENUSER = "/sso/user-get.jhtml";

	/** § 1.5 检查用户名是否可用 */
	public static final String UC_CHECK_USERNAME = "/register/check_username.jhtml";

	/** § 1.6 检查E-mail是否存在 */
	public static final String UC_CHECK_EMAIL = "/register/check_email.jhtml";

	/** § 1.7 检查手机号是否存在 */
	public static final String UC_CHECK_MOBILE = "/register/check_mobile.jhtml";

	/** § 1.8 获取注册验证码及公钥 */
	public static final String UC_SUBMIT_GET = "/register/submit-get.jhtml";

	/** § 1.9 注册提交 */
	public static final String UC_REGISTER_SUBMIT = "/register/submit.jhtml";

	/** § 1.10 发送手机验证码 */
	public static final String UC_SEND_MOBILE = "/register/send_mobile.jhtml";

	/** § 1.10 获取验证码 */
	public static final String UC_CHALLEGE = "/sso/auth-get.jhtml";

	/** § 1.11 发送邮箱验证码 */
	public static final String UC_SEND_EMAIL = "/register/send_email.jhtml";

	/** § 1.12 获取用户注册验证码 */
	public static final String UC_CAPTCHA = "/common/captcha.jhtml";

	/** § 1.13 修改密码 */
	public static final String UC_UPDATE_PASS = "/register/updatePass.jhtml";

	/** § 1.14 找回密码-获取验证码 */
	public static final String UC_SEND_CAPTCHA = "/register/send_captcha.jhtml";

	/** § 1.15 找回密码-手机/邮箱验证 */
	public static final String UC_RETRIEVE_CODE = "/register/retrieveCode.jhtml";

	/** § 1.16 找回密码 */
	public static final String UC_RETRIEVE_PASS = "/register/retrievePass.jhtml";
	
	/** "UC_令牌"转向地址 */
	public static final String UC_TOKEN_REDIRECTURL = "redirecturl";

	public static final String generateKey = "uic.vsstoo$$.com";

	public static final String AUTH_GENERATE_KEY = "generateKey";

	/** § 1.18手机/邮箱绑定-获取验证码 */
	public static final String UC_BIND_SEND_CAPTCHA = "/register/bind_send_captcha.jhtml";

	/** § 1.19手机/邮箱绑定-验证验证码 */
	public static final String UC_BIND_RETRIEVE_CODE = "/register/bind_retrieveCode.jhtml";

	/** § 1.20手机/邮箱绑定 */
	public static final String UC_BIND_SAVE = "/register/bind_save.jhtml";

	/** § 1.21找回支付密码-获取验证码 */
	public static final String UC_PAYMENTPASS_CAPTCHA = "/register/payMentPass_captcha.jhtml";

	/** § 1.22找回支付密码 */
	public static final String UC_RETRIEVE_PAYMENTPASS = "/register/retrievePayMentPass.jhtml";

	/** § 1.23修改支付密码 */
	public static final String UC_UPDATE_PAYMENTPASS = "/register/updatePayMentPass.jhtml";

	/** § 1.24实名认证 */
	public static final String UC_APPLY = "/register/apply.jhtml";

	/** § 1.25上传头像 */
	public static final String UC_HEADIMG = "/register/uploadImg.jhtml";

	/** § 1.26删除用户 */
	public static final String UC_DELETEUSER = "/register/delUser.jhtml";

	/** § 1.26修改手机 */
	public static final String UC_MOBILEUPDATE = "/uicMember/mobileUpdate.jhtml";

	/** § 1.26修改邮箱 */
	public static final String UC_EMAILUPDATE = "/uicMember/emailUpdate.jhtml";

	/** § 1.26修改收货地址 */
	public static final String UC_USERADDRESSUPDATE = "/uicMember/userAddressUpdate.jhtml";

	/** § 1.26修改真实姓名 */
	public static final String UC_READYNAMEUPDATE = "/uicMember/readyNameUpdate.jhtml";

	/** § 1.26绑定手机 */
	public static final String UC_BINDMOBILE = "/uicMember/bindMobile.jhtml";

	/** § 1.26绑定邮箱 */
	public static final String UC_BINDEMAIL = "/uicMember/bindEmail.jhtml";

	/** § 1.9 注册提交 */
	public static final String UC_REGISTER_SUBMITMEMBER = "/register/submitMember.jhtml";

	/** § 1.10 注册会员 */
	public static final String UC_REGISTER_MEMBERSUBMIT = "/register/memberSubmit.jhtml";

	public static final class Cookies {
		public static final String HOST = "host";

		public static final String UC_TOKEN = "uctoken";
	}

}
