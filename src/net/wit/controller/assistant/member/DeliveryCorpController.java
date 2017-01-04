/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.assistant.member;

import net.wit.controller.assistant.BaseController;
import net.wit.controller.assistant.model.DataBlock;
import net.wit.controller.assistant.model.DeliveryCorpModel;
import net.wit.entity.DeliveryCorp;
import net.wit.service.DeliveryCorpService;
import net.wit.service.TenantService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * Controller - 物流公司
 * 
 * @author rsico Team
 * @version 3.0
 */
@Controller("assistantMemberDeliveryCorpController")
@RequestMapping("/assistant/member/delivery_corp")
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
	DataBlock list() {
	    List<DeliveryCorp> corps = deliveryCorpService.findAll();
		return DataBlock.success(DeliveryCorpModel.bindData(corps),"获取成功");
	
	}

}