/*
 * Copyright 2005-2013 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 */
package net.wit.job;

import javax.annotation.Resource;

import net.wit.service.CartService;
import net.wit.web.mobile.MobileFactory;

import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Job - 购物车
 * 
 * @author SHOP++ Team
 * @version 3.0
 */
@Component("mobileJob")
@Lazy(false)
public class MobileJob {

	@Resource(name = "mobileFactory")
	private MobileFactory mobileFactory;

	/**
	 * 清除过期购物车
	 */
	@Scheduled(cron = "${job.mobile_direct.cron}")
	public void directProduct() {
		return;
		//mobileFactory.directProduct();
	}

}