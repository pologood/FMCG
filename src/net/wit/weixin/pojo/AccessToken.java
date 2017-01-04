package net.wit.weixin.pojo;

import java.util.Date;

/**
 * 微信通用接口凭证
 * @author Administrator
 */
public class AccessToken {
	// 获取到的凭证
	private String token;

	// 凭证有效时间，单位：秒
	private int expiresIn;

	private String refreshToken;

	private String openid;

	private String scope;
	
	/**到期时间*/
	private Date expire;

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public int getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(int expiresIn) {
		this.expiresIn = expiresIn;
	}

	/**
	 * @return the expire
	 */
	public Date getExpire() {
		return expire;
	}

	/**
	 * @param expire the expire to set
	 */
	public void setExpire(Date expire) {
		this.expire = expire;
	}
}
