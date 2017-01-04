/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Transient;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * 身份信息
 * 
 * @author rsico Team
 * @version 3.0
 */
public class Principal implements Serializable {

	private static final long serialVersionUID = 5798882004228239559L;

	/** ID */
	private Long id;

	/** 用户名 */
	private String username;

	/** 时间戳 */
	private String timestamp;
	
	/** 签名 */
	private String sign;
	
	public Principal() {
	}
	
	/**
	 * @param id
	 *            ID
	 * @param username
	 *            用户名
	 */
	public Principal(Long id, String username) {
		this.id = id;
		this.username = username;
	}

	/**
	 * 获取ID
	 * 
	 * @return ID
	 */
	public Long getId() {
		return id;
	}

	/**
	 * 设置ID
	 * 
	 * @param id
	 *            ID
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * 获取用户名
	 * 
	 * @return 用户名
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * 设置用户名
	 * 
	 * @param username
	 *            用户名
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * 获取时间戳
	 * 
	 * @return 时间戳
	 */
	public String getTimestamp() {
		return timestamp;
	}

	/**
	 * 设置时间戳
	 * 
	 * @param timestamp
	 *            时间戳
	 */
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * 获取签名
	 * 
	 * @return 签名
	 */
	public String getSign() {
		return sign;
	}

	/**
	 * 设置签名
	 * 
	 * @param sign
	 *            签名
	 */
	public void setSign(String sign) {
		this.sign = sign;
	}

	@Override
	public String toString() {
		return username;
	}
	
	/**
	 * 生成签名
	 */
	@Transient
	public void createSign() {
		SimpleDateFormat reqTime =new SimpleDateFormat("yyyyMMddHHmmss");
		this.timestamp = reqTime.format(new Date());
		StringBuilder builder = new StringBuilder();
		builder.append(String.valueOf(getId()));
		builder.append(getUsername());
		builder.append(getTimestamp());
		builder.append("wit@2014-2020$$");
		this.sign = DigestUtils.md5Hex(builder.toString());
	}

	/**
	 * 检测签名
	 */
	@Transient
	public boolean checkSign() {
		try {
	    	//SimpleDateFormat reqTime =new SimpleDateFormat("yyyyMMddHHmmss");
	    	//Date tms = reqTime.parse(getTimestamp());
	    	//Date ems = new Date();
	    	//if ((ems.getTime() - tms.getTime())>120000) {
		    //	return false;
		    //}
		    StringBuilder builder = new StringBuilder();
		    builder.append(String.valueOf(getId()));
		    builder.append(getUsername());
		    builder.append(getTimestamp());
		    builder.append("wit@2014-2020$$");
	    	return this.sign == DigestUtils.md5Hex(builder.toString());
		} catch (Exception e) {
			return false;
		}
	}
	
}