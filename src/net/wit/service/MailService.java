/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.service;

import java.util.Map;

import net.wit.entity.ProductNotify;
import net.wit.entity.SafeKey;

/**
 * Service - 邮件
 * 
 * @author rsico Team
 * @version 3.0
 */
public interface MailService {

	/**
	 * 发送邮件
	 * 
	 * @param smtpFromMail
	 *            发件人邮箱
	 * @param smtpHost
	 *            SMTP服务器地址
	 * @param smtpPort
	 *            SMTP服务器端口
	 * @param smtpUsername
	 *            SMTP用户名
	 * @param smtpPassword
	 *            SMTP密码
	 * @param toMail
	 *            收件人邮箱
	 * @param subject
	 *            主题
	 * @param templatePath
	 *            模板路径
	 * @param model
	 *            数据
	 * @param async
	 *            是否异步
	 */
	void send(String smtpFromMail, String smtpHost, Integer smtpPort,
			String smtpUsername, String smtpPassword, String toMail,
			String subject, String templatePath, Map<String, Object> model,
			boolean async);

	/**
	 * 发送邮件
	 * 
	 * @param toMail
	 *            收件人邮箱
	 * @param subject
	 *            主题
	 * @param templatePath
	 *            模板路径
	 * @param model
	 *            数据
	 * @param async
	 *            是否异步
	 */
	void send(String toMail, String subject, String templatePath,
			Map<String, Object> model, boolean async);

	/**
	 * 发送邮件(异步)
	 * 
	 * @param toMail
	 *            收件人邮箱
	 * @param subject
	 *            主题
	 * @param templatePath
	 *            模板路径
	 * @param model
	 *            数据
	 */
	void send(String toMail, String subject, String templatePath,
			Map<String, Object> model);

	/**
	 * 发送邮件(异步)
	 * 
	 * @param toMail
	 *            收件人邮箱
	 * @param subject
	 *            主题
	 * @param templatePath
	 *            模板路径
	 */
	void send(String toMail, String subject, String templatePath);

	/**
	 * 发送测试邮件
	 * 
	 * @param smtpFromMail
	 *            发件人邮箱
	 * @param smtpHost
	 *            SMTP服务器地址
	 * @param smtpPort
	 *            SMTP服务器端口
	 * @param smtpUsername
	 *            SMTP用户名
	 * @param smtpPassword
	 *            SMTP密码
	 * @param toMail
	 *            收件人邮箱
	 */
	void sendTestMail(String smtpFromMail, String smtpHost, Integer smtpPort,
			String smtpUsername, String smtpPassword, String toMail);

	/**
	 * 发送找回密码邮件
	 * 
	 * @param toMail
	 *            收件人邮箱
	 * @param username
	 *            用户名
	 * @param safeKey
	 *            安全密匙
	 */
	void sendFindPasswordMail(String toMail, String username, SafeKey safeKey);

	/**
	 * 发送找回密码邮件
	 * 
	 * @param toMail
	 *            收件人邮箱
	 * @param username
	 *            用户名
	 * @param safeKey
	 *            安全密匙
	 */
	void sendFindPaymentPasswordMail(String toMail, String username,
			SafeKey safeKey);

	/**
	 * 发送绑定邮件
	 * 
	 * @param toMail
	 *            收件人邮箱
	 * @param username
	 *            用户名
	 * @param securityCode
	 *            安全密匙
	 */
	void sendBindMail(String toMail, String username, SafeKey safeKey);

	/**
	 * 发送到货通知邮件
	 * 
	 * @param productNotify
	 *            到货通知
	 */
	void sendProductNotifyMail(ProductNotify productNotify);

	void sendMailInvaild(String email, String username, SafeKey safeKey);

	void sendRegisterMail(String email, String username, SafeKey safeKey);

	void sendRegisterSucessMail(String email, String username, SafeKey safeKey);

	void sendCheckCodeByMail(String toMail, String username, SafeKey safeKey);

	void b2cSendFindPaymentPasswordMail(String email, String username,
			SafeKey safeKey);

	void sendB2cBindMail(String email, String username, SafeKey safeKey);

	public void sendCheckCodeByMailCommon(String toMail, String username,
			SafeKey safeKey);
	
	public void sendGetBackPasswordMail(String toMail, String username,SafeKey safeKey);
	
	public void sendCheckCodeByMailBind(String toMail, String username,
			SafeKey safeKey);
}