/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.service;

import net.wit.Message;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Credit;
import net.wit.entity.Member;
import net.wit.entity.MinshengBank;
import net.wit.plugin.PaymentPlugin.RequestMethod;

/**
 * Service - 民生银行
 * @author rsico Team
 * @version 3.0
 */
public interface MinshengTransService extends BaseService<MinshengBank, Long> {

	Page<MinshengBank> findPage(Member member, Pageable pageable);
	
	public String getRequestUrl();
	public String getRequestCharset();
	public RequestMethod getRequestMethod();
//	public Message payToBank(String sn, Credit credit);
	public Message queryToBank(String sn, Credit credit);
	public Message sendBankmsg(String sn, Credit credit);
	public Message sendCostReimbmsg(String sn, Credit credit,String sbankcode,String sname);
//	public Message CostReimbPay(String sn, Credit credit); 
	public Message receiveBank(MinshengBank minshengBank, Credit credit,String msgtype, String receivetype);
	public MinshengBank findMinshengbyNo(String sn) ;
}