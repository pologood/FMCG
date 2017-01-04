/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.pos;

import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import javax.annotation.Resource;

import net.wit.controller.app.model.DataBlock;
import net.wit.controller.app.model.DeliveryCorpModel;
import net.wit.entity.DeliveryCorp;
import net.wit.entity.Tenant;
import net.wit.service.DeliveryCorpService;
import net.wit.service.TenantService;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller - 物流公司
 * 
 * @author rsico Team
 * @version 3.0
 */
@Controller("posDeliveryCorpController")
@RequestMapping("/pos/delivery_corp")
public class DeliveryCorpController extends BaseController {

	@Resource(name = "deliveryCorpServiceImpl")
	private DeliveryCorpService deliveryCorpService;

	@Resource(name = "tenantServiceImpl")
	private TenantService tenantService;

	/**
	 * 获取物流公司
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public @ResponseBody
	DataBlock list(Long tenantId,String key) {
		if (tenantId == null) {
			return DataBlock.error("商家信息标识为空!");
		}
			Tenant tenant = tenantService.find(tenantId);
			if (tenant == null) {
				return DataBlock.error("商家信息为空!");
			}
			ResourceBundle bundle=PropertyResourceBundle.getBundle("config");
			String myKey = DigestUtils.md5Hex(tenantId+bundle.getString("appKey"));
			if (!myKey.equals(key)) {
				return DataBlock.error("通讯密码无效");
			}
	    List<DeliveryCorp> corps = deliveryCorpService.findAll();
		return DataBlock.success(DeliveryCorpModel.bindData(corps),"获取成功");
	
	}

}