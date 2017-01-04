/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.pos;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import net.wit.Filter;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.Filter.Operator;
import net.wit.controller.ajax.BaseController;
import net.wit.controller.app.model.DataBlock;
import net.wit.controller.app.model.ShippingListModel;
import net.wit.controller.app.model.ShippingModel;
import net.wit.controller.app.model.TradeListModel;
import net.wit.controller.app.model.TradeModel;
import net.wit.dao.AdminDao;
import net.wit.display.DisplayEngine;
import net.wit.display.vo.TradeVo;
import net.wit.entity.Area;
import net.wit.entity.DeliveryCenter;
import net.wit.entity.Order;
import net.wit.entity.Order.OrderStatus;
import net.wit.entity.Order.OrderType;
import net.wit.entity.Order.PaymentStatus;
import net.wit.entity.Order.ShippingStatus;
import net.wit.entity.OrderItem;
import net.wit.entity.Product;
import net.wit.entity.Shipping;
import net.wit.entity.ShippingItem;
import net.wit.entity.ShippingMethod;
import net.wit.entity.Sn;
import net.wit.entity.SpReturns;
import net.wit.entity.SpReturns.ReturnStatus;
import net.wit.entity.Tenant;
import net.wit.entity.Trade;
import net.wit.exception.BalanceNotEnoughException;
import net.wit.service.AreaService;
import net.wit.service.DeliveryCenterService;
import net.wit.service.DeliveryCorpService;
import net.wit.service.MemberService;
import net.wit.service.OrderItemService;
import net.wit.service.OrderService;
import net.wit.service.PaymentMethodService;
import net.wit.service.ProductService;
import net.wit.service.ShippingMethodService;
import net.wit.service.ShippingService;
import net.wit.service.SnService;
import net.wit.service.TenantService;
import net.wit.service.TradeService;

/**
 * Controller - 发货单信息
 * @author rsico Team
 * @version 3.0
 */
@Controller("posMemberShippingController")
@RequestMapping("/pos/member/shipping")
public class ShippingController extends BaseController {

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	@Resource(name = "orderServiceImpl")
	private OrderService orderService;

	@Resource(name = "shippingServiceImpl")
	private ShippingService shippingService;

	@Resource(name = "deliveryCenterServiceImpl")
	private DeliveryCenterService deliveryCenterService;

	@Resource(name = "deliveryCorpServiceImpl")
	private DeliveryCorpService deliveryCorpService;

	@Resource(name = "snServiceImpl")
	private SnService snService;

	@Resource(name = "tradeServiceImpl")
	private TradeService tradeService;

	@Resource(name = "adminDaoImpl")
	private AdminDao adminDao;

	@Resource(name = "orderItemServiceImpl")
	private OrderItemService orderItemService;

	@Resource(name = "paymentMethodServiceImpl")
	private PaymentMethodService paymentMethodService;

	@Resource(name = "productServiceImpl")
	private ProductService productService;

	@Resource(name = "tenantServiceImpl")
	private TenantService tenantService;

	@Resource(name = "tradeDisplay")
	private DisplayEngine<Trade, TradeVo> tradeDisplay;

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public @ResponseBody DataBlock list(Long tenantId, Long tradeId,String key, Pageable pageable, ModelMap model, RedirectAttributes redirectAttributes) {
		if (tenantId == null) {
			return DataBlock.error("商家信息标识为空!"); 
		}
			Tenant tenant = tenantService.find(tenantId);
			if (tenant == null) {
				return DataBlock.error("商家信息为空!");
			}
			ResourceBundle bundle=PropertyResourceBundle.getBundle("config");
			String myKey = DigestUtils.md5Hex(tenantId+tradeId.toString()+bundle.getString("appKey"));
			if (!myKey.equals(key)) {
				return DataBlock.error("通讯密码无效");
			}
		
			List<Filter> filters = pageable.getFilters();
			filters.add(new Filter("trade", Operator.eq, tradeService.find(tradeId)));
			
		Page<Shipping> page = shippingService.findPage(pageable);
		
		return DataBlock.success(ShippingListModel.bindData(page.getContent()),"success");
		
	}

	/**
	 * 查看
	 */
	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public @ResponseBody DataBlock view(Long tenantId,Long id,String key) {
		if (tenantId == null) {
			return DataBlock.error("商家信息标识为空!");
		}
			Tenant tenant = tenantService.find(tenantId);
			if (tenant == null) {
				return DataBlock.error("商家信息为空!");
			}
			ResourceBundle bundle=PropertyResourceBundle.getBundle("config");
			String myKey = DigestUtils.md5Hex(tenantId.toString()+id.toString()+bundle.getString("appKey"));
			if (!myKey.equals(key)) {
				return DataBlock.error("通讯密码无效");
			}
		Shipping shipping = shippingService.find(id);
		ShippingModel model = new ShippingModel();
		model.copyFrom(shipping);
		return DataBlock.success(model,"success");
	}
}