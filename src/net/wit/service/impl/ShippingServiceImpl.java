/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.service.impl;

import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.Setting;
import net.wit.dao.ShippingDao;
import net.wit.entity.Shipping;
import net.wit.service.ShippingService;
import net.wit.util.SettingUtils;
import net.wit.webservice.xxkyService;

import org.apache.commons.lang.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Service - 发货单
 * 
 * @author rsico Team
 * @version 3.0
 */
@Service("shippingServiceImpl")
public class ShippingServiceImpl extends BaseServiceImpl<Shipping, Long> implements ShippingService {

	@Resource(name = "shippingDaoImpl")
	private ShippingDao shippingDao;

	@Resource(name = "shippingDaoImpl")
	public void setBaseDao(ShippingDao shippingDao) {
		super.setBaseDao(shippingDao);
	}

	@Transactional(readOnly = true)
	public Shipping findBySn(String sn) {
		return shippingDao.findBySn(sn);
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
//	@Cacheable("shipping")
	public Map<String, Object> query(Shipping shipping) {
		Setting setting = SettingUtils.get();
		Map<String, Object> data = new HashMap<>();
		if (shipping != null && StringUtils.isNotEmpty(setting.getKuaidi100Key()) && StringUtils.isNotEmpty(shipping.getDeliveryCorpCode()) && StringUtils.isNotEmpty(shipping.getTrackingNo())) {
//			if(shipping.getDeliveryCorpCode().equals("debangwuliu")){
			try {
				ObjectMapper mapper = new ObjectMapper();
				URL url = new URL("http://api.kuaidi100.com/api?id=" + setting.getKuaidi100Key() + "&com=" + shipping.getDeliveryCorpCode() + "&nu=" + shipping.getTrackingNo() + "&show=0&muti=1&order=asc");
				data = mapper.readValue(url, Map.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
//			}else{
//				try{
//					xxkyService xxkyService = new xxkyService();
//					data = xxkyService.GetOrderTraceInfo(shipping.getTrackingNo());
//				}catch(Exception ex){
//					ex.printStackTrace();
//				}
//			}
		}
		return data;
	}

	@Override
	public Page<Shipping> findPage(Date beginDate, Date endDate,
			Pageable pageable) {
		return shippingDao.findPage(beginDate, endDate, pageable);
	}

}