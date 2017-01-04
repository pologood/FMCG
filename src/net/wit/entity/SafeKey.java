/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.time.DateUtils;

/**
 * @ClassName: SafeKey
 * @Description: 安全密钥
 * @author Administrator
 * @date 2014年10月14日 上午9:11:26
 */
@Embeddable
public class SafeKey implements Serializable {

	private static final long serialVersionUID = -8536541568286987548L;

	/** 密钥 */
	@Column(name = "safe_key_value")
	private String value;

	/** 到期时间 */
	@Column(name = "safe_key_expire")
	private Date expire;

	/** 创建时间 */
	@Column(name = "safe_key_create")
	private Date create ;
	
	/** 判断是否已过期 */
	public boolean hasExpired() {
		return getExpire() != null && new Date().after(getExpire());
	}

	/** 判断是否超过1分钟 */
	public boolean canReset() {
		return getCreate() == null || new Date().after(DateUtils.addMinutes(getCreate(),1));
	}
	
	public SafeKey() {}

	public SafeKey(String value, Date expire) {
		this.value = value;
		this.expire = expire;
		this.create = new Date();
	}

	// ===========================================getter/setter===========================================//
	/**
	 * 获取密钥
	 * @return 密钥
	 */
	public String getValue() {
		return value;
	}

	/**
	 * 设置密钥
	 * @param value 密钥
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * 获取到期时间
	 * @return 到期时间
	 */
	public Date getExpire() {
		return expire;
	}

	public Date getCreate() {
		return create;
	}

	public void setCreate(Date create) {
		this.create = create;
	}

	/**
	 * 设置到期时间
	 * @param expire 到期时间
	 */
	public void setExpire(Date expire) {
		this.expire = expire;
		this.create = new Date();
	}

}