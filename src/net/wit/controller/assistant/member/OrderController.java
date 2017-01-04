/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.assistant.member;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.Setting;
import net.wit.controller.assistant.BaseController;
import net.wit.controller.assistant.model.*;
import net.wit.entity.*;
import net.wit.entity.Order.*;
import net.wit.entity.Payment.Method;
import net.wit.entity.Payment.Status;
import net.wit.entity.Payment.Type;
import net.wit.service.*;
import net.wit.support.EntitySupport;
import net.wit.util.SettingUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

/**
 * Controller -  我的订单
 * @author rsico Team
 * @version 3.0
 */
@Controller("assistantMemberOrderController")
@RequestMapping("/assistant/member/order")
public class OrderController extends BaseController {

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "receiverServiceImpl")
	private ReceiverService receiverService;

	@Resource(name = "cartServiceImpl")
	private CartService cartService;

	@Resource(name = "paymentMethodServiceImpl")
	private PaymentMethodService paymentMethodService;

	@Resource(name = "shippingMethodServiceImpl")
	private ShippingMethodService shippingMethodService;

	@Resource(name = "couponCodeServiceImpl")
	private CouponCodeService couponCodeService;

	@Resource(name = "equipmentServiceImpl")
	private EquipmentService equipmentService;

	@Resource(name = "orderServiceImpl")
	private OrderService orderService;

	@Resource(name = "tradeServiceImpl")
	private TradeService tradeService;

	@Resource(name = "employeeServiceImpl")
	private EmployeeService employeeService;

	@Resource(name = "messageServiceImpl")
	private MessageService messageService;

	@Resource(name = "shippingServiceImpl")
	private ShippingService shippingService;

	@Resource(name = "snServiceImpl")
	private SnService snService;

	@Resource(name = "paymentServiceImpl")
	private PaymentService paymentService;

	@Resource(name = "spReturnsServiceImpl")
	private SpReturnsService spReturnsService;

	@Resource(name = "tenantServiceImpl")
	private TenantService tenantService;

	@Resource(name = "activityPlanningServiceImpl")
	private ActivityPlanningService activityPlanningService;

	@Resource(name = "deliveryCenterServiceImpl")
	private DeliveryCenterService deliveryCenterService;

	/**
	 * 订单锁定
	 */
	@RequestMapping(value = "/lock", method = RequestMethod.POST)
	public @ResponseBody
	DataBlock lock(String sn) {
		Member member = memberService.getCurrent();
		if (member == null) {
			DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		Order order = orderService.findBySn(sn);
		if (order != null && !order.isLocked(member)) {
			order.setLockExpire(DateUtils.addSeconds(new Date(), 20));
			order.setOperator(member);
			orderService.update(order);
			return DataBlock.success("success","执行成功");
		}
		return DataBlock.error("订单被锁定，稍候试试..","warn");
	}
	
	/**
	 * 优惠券信息
	 * code 优惠券编码
	 */
	@RequestMapping(value = "/coupon_info", method = RequestMethod.POST)
	public @ResponseBody
	DataBlock couponInfo(String code) {
		Map<String, Object> data = new HashMap<String, Object>();
		Member member = memberService.getCurrent();
		if (member == null) {
			DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		Cart cart = cartService.getCurrent();
		if (cart == null || cart.isEmpty()) {
			return DataBlock.error("ajax.cart.notEmpty");
			
		}
		if (!cart.isCouponAllowed()) {
			return DataBlock.error("ajax.order.couponNotAllowed");
			
		}
		CouponCode couponCode = couponCodeService.findByCode(code);
		if (couponCode != null && couponCode.getCoupon() != null) {
			Coupon coupon = couponCode.getCoupon();
			if (!coupon.getIsEnabled()) {
				return DataBlock.error("ajax.order.couponDisabled");
				
			}
			if (couponCode.hasExpired()) {
				return DataBlock.error("ajax.order.couponHasExpired");
				
			}
			if (!cart.isValid(coupon)) {
				return DataBlock.error("ajax.order.couponInvalid");
				
			}
			if (couponCode.getIsUsed()) {
				return DataBlock.error("ajax.order.couponCodeUsed");
			}
			data.put("couponName", coupon.getName());
			return DataBlock.success(coupon.getName(),"执行成功");
		} else {
			return DataBlock.error("ajax.order.couponCodeNotExist");
		}
	}

	/**
	 * 获取可用优惠券列表
	 * 
	 */
	@RequestMapping(value = "/coupons", method = RequestMethod.POST)
	public @ResponseBody
	DataBlock coupons() {
		Member member = memberService.getCurrent();
		if (member == null) {
			DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		List<CouponCode> couponCodes = couponCodeService.findEnabledList(member);
		Cart cart = cartService.getCurrent();
		if (cart == null || cart.isEmpty()) {
			return DataBlock.success(null,"执行成功");
		}
		if (!cart.isCouponAllowed()) {
			return DataBlock.success(null,"执行成功");
		}
		
		List<CouponCode> codes = new ArrayList<CouponCode>();
		for (CouponCode coupon:couponCodes) {
			if (!coupon.getCoupon().getType().equals(Coupon.Type.tenantCoupon) || !coupon.getCoupon().getIsEnabled() || coupon.hasExpired() ||!cart.isValid(coupon.getCoupon()) || coupon.getIsUsed()) {
			} else {
				codes.add(coupon);
			}
		}
		
		return DataBlock.success(CouponCodeModel.bindData(codes),"执行成功");
		
	}

	/**
	 * 获取店铺可用优惠券列表
	 *
	 */
	@RequestMapping(value = "/tenant/coupons", method = RequestMethod.POST)
	public @ResponseBody
	DataBlock tenantCoupons(Long tenantId) {
		Member member = memberService.getCurrent();
		if (member == null) {
			DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		Tenant tenant=tenantService.find(tenantId);
		if(tenant == null){
			return DataBlock.error("无效店铺id");
		}
		List<CouponCode> couponCodes = couponCodeService.findEnabledList(member);
		Cart cart = cartService.getCurrent();
		if (cart == null || cart.isEmpty()) {
			return DataBlock.success(null,"执行成功");
		}
		if (!cart.isCouponAllowed()) {
			return DataBlock.success(null,"执行成功");
		}

		List<CouponCode> codes = new ArrayList<CouponCode>();
		for (CouponCode coupon:couponCodes) {
			if (coupon.getCoupon().getTenant()!=tenant||!coupon.getCoupon().getIsEnabled() || coupon.hasExpired() ||!cart.isValid(coupon.getCoupon()) || coupon.getIsUsed()) {
			} else {
				codes.add(coupon);
			}
		}
		Collections.sort(codes, new Comparator<CouponCode>() {
			@Override
			public int compare(CouponCode o1, CouponCode o2) {
				return o2.getCoupon().getAmount().compareTo(o1.getCoupon().getAmount());
			}
		});

		return DataBlock.success(CouponCodeModel.bindData(codes),"执行成功");

	}
	
	/**
	 * 订单确认页
	 */
	@RequestMapping(value = "/info", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock info(Location location) {
		Map<String, Object> data = new HashMap<String, Object>();
		Member member = memberService.getCurrent();
		if (member == null) {
			DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		Cart cart = cartService.getCurrent();
		if (cart == null || cart.isEmpty()) {
			return DataBlock.error("ajax.cart.notEmpty");
		}
		if (!isValid(cart)) {
			return DataBlock.error("ajax.cart.isValid");
		}
    	cartService.clearActivity();
        ActivityPlanning activityPlanning = activityPlanningService.getCurrent(null, ActivityPlanning.Type.random);
        if (activityPlanning != null) {
            if (orderService.isLimit(member, activityPlanning.getActivityMaximumOrders())) {
                cartService.bindActivity();
            }
            //model.addAttribute("activityTenants", activityPlanning.getTenants());
        }

		//cartService.bindActivity();
		List<PaymentMethod> paymentMethods = paymentMethodService.findAll();
		List<ShippingMethod> shippingMethods = shippingMethodService.findAll();
		PaymentMethod paymentMethod = paymentMethods.size() > 0 ? paymentMethods.get(0) : null;
		ShippingMethod shippingMethod = shippingMethods.size() > 0 ? shippingMethods.get(0) : null;
		Order order = orderService.build(cart, null, paymentMethod, shippingMethod, null, false, null, false, null, null);
		OrderModel model = new OrderModel();

		List<CouponCode> codes = new ArrayList<CouponCode>();
		if (member == null) {
			DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		List<CouponCode> couponCodes = couponCodeService.findEnabledList(member);
		if (cart.isCouponAllowed()) {
			for (CouponCode coupon:couponCodes) {
				if (!coupon.getCoupon().getType().equals(Coupon.Type.tenantCoupon) || !coupon.getCoupon().getIsEnabled() || coupon.hasExpired() ||!cart.isValid(coupon.getCoupon()) || coupon.getIsUsed()) {
				} else {
					codes.add(coupon);
				}
			}
		}

		model.copyFrom(order,location,codes);

		data.put("order",model);
		data.put("paymentMethods", PaymentMethodModel.bindData(paymentMethods));
		data.put("shippingMethods", ShippingMethodModel.bindData(shippingMethods));
		ReceiverModel receiverModel=null;
		Receiver receiver=receiverService.findDefault(member);
		if(receiver!=null){
			receiverModel=new ReceiverModel();
			receiverModel.copyFrom(receiver);
		}
		data.put("receiver",receiverModel);
		return DataBlock.success(data,"执行成功");
	}

	/**
	 * 计算费用
	 * return 
	 *   quantity 合计数量
	 *   price 订单合计
	 *   freight 运费
	 *   promotionDiscount 促销折扣
	 *   couponDiscount 优惠券立减
	 *   tax 税
	 *   amountPayable 待支付金额
	 */
	@RequestMapping(value = "/calculate", method = RequestMethod.POST)
	public @ResponseBody
	DataBlock calculate(Long paymentMethodId, Long shippingMethodId,Long [] tenantIds, String[] code,Boolean isInvoice, String invoiceTitle,Boolean useBalance, String [] memo) {
		Map<String, Object> data = new HashMap<>();
		Cart cart = cartService.getCurrent();
		Member member = memberService.getCurrent();
		if (member == null) {
			DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		if (cart == null || cart.isEmpty()) {
			return DataBlock.error("ajax.cart.notEmpty");
		}
		PaymentMethod paymentMethod = paymentMethodService.find(paymentMethodId);
		ShippingMethod shippingMethod = shippingMethodService.find(shippingMethodId);

		Set<CouponCode> couponCodes = new HashSet<>();
		if (code != null) {
			for (String c : code) {
				CouponCode couponCode = couponCodeService.findByCode(c);
				couponCodes.add(couponCode);
			}
		}
		Order order = orderService.build(cart, null, paymentMethod, shippingMethod, couponCodes, isInvoice, invoiceTitle, useBalance, tenantIds,memo);

		data.put("quantity", order.getQuantity());
		data.put("price", order.getPrice());
		data.put("promotionDiscount", order.getPromotionDiscount());
		data.put("couponDiscount", order.getCouponDiscount());
		data.put("tax", order.getTax());
		data.put("offsetAmount", order.getOffsetAmount());
		data.put("freight", order.getFreight());
		data.put("discount", 0);
		data.put("amountPayable", order.getAmountPayable());
		List<Map<String, Object>> trades = new ArrayList<>();
		for (Trade trade : order.getTrades()) {
			Map<String, Object> map = new HashMap<>();
			map.put("tenantId", trade.getTenant().getId());
			map.put("promotionDiscount", trade.getPromotionDiscount());
			map.put("couponDiscount", trade.getCouponDiscount());
			map.put("tax", trade.getTax());
			map.put("offsetAmount", trade.getOffsetAmount());
			map.put("freight", trade.getFreight());
			map.put("amount", trade.getAmount());
			trades.add(map);
		}
		data.put("trades", trades);

		return DataBlock.success(data,"执行成功");
	}

	/**
	 * 提交订单创建
	 * receiverId 收货地址编号
	 * paymentMethodId 支付方式编号
	 * shippingMethodId 配送方式编号
	 * code 优惠券编号
	 * isInvoice 是否开票
	 * invoiceTitle 开票抬头
	 * useBalance 是否使用余额支付
	 * memo 备注
	 * extensionId 导购、分享者 编号
	 * uuid 购物屏设备编号
	 */
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public @ResponseBody
	DataBlock create(Long receiverId, Long paymentMethodId, Long shippingMethodId,String [] code,Boolean isInvoice,
			String invoiceTitle,Boolean useBalance, Long [] tenantIds, String [] memo, Long[] deliveryCenterIds, Long extensionId,String uuid,HttpServletRequest request) {
		
		if (extensionId!=null) {
			Member extension = memberService.find(extensionId);
			if (extension!=null) {
		    	request.getSession().setAttribute(Member.EXTENSION_ATTRIBUTE_NAME,extension.getUsername());
			}
			
		}
		
		Cart cart = cartService.getCurrent();
		Member member = memberService.getCurrent();
		if (member.getUsername().equals("13860431130")) {
			return DataBlock.error("体验账号不能购物");
		}
		if (member == null) {
			DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		if (cart == null || cart.isEmpty()) {
			return DataBlock.error("ajax.cart.notEmpty");
		}

		if (cart.getIsLowStock()) {
			return DataBlock.error("ajax.order.cartLowStock");
		}
		Receiver receiver = receiverService.find(receiverId);
		if (receiver == null) {
			return DataBlock.error("ajax.member.order.receiverNotExsit");
		}
		PaymentMethod paymentMethod = paymentMethodService.find(paymentMethodId);
		if (paymentMethod == null) {
			return DataBlock.error("ajax.order.paymentMethodNotExsit");
		}
		ShippingMethod shippingMethod = shippingMethodService.find(shippingMethodId);
		if (shippingMethod == null) {
			return DataBlock.error("ajax.order.shippingMethodNotExsit");
		}
		if (!paymentMethod.getShippingMethods().contains(shippingMethod)) {
			return DataBlock.error("ajax.order.deliveryUnsupported");
		}
		System.out.println("uuid="+uuid);
		Equipment equipment = equipmentService.findByUUID(uuid);
		Set<CouponCode> couponCodes = new HashSet<CouponCode>();
		if(code!=null){
			for (String c:code) {
				CouponCode couponCode = couponCodeService.findByCode(c);
				couponCodes.add(couponCode);
			}
		}
		List<DeliveryCenter> deliveryCenters=new ArrayList<>();
		if(deliveryCenterIds!=null){
			for(Long deliveryCenterId:deliveryCenterIds){
				deliveryCenters.add(deliveryCenterService.find(deliveryCenterId));
			}
		}
		Order order = orderService.create(cart, receiver, paymentMethod, shippingMethod, couponCodes, isInvoice, invoiceTitle, useBalance,tenantIds,memo,deliveryCenters,member, null,OrderSource.app,equipment, null);
		return DataBlock.success(order.getSn(),"执行成功");
	}

	@RequestMapping(value = "/create_json", method = RequestMethod.POST)
	public @ResponseBody
	DataBlock createJson(Long receiverId, Long paymentMethodId, Long shippingMethodId,String [] code,Boolean isInvoice,
					 String invoiceTitle,Boolean useBalance, Long [] tenantIds, String [] memo, Long[] deliveryCenterIds, Long extensionId,String uuid,HttpServletRequest request) {

		if (extensionId!=null) {
			Member extension = memberService.find(extensionId);
			if (extension!=null) {
				request.getSession().setAttribute(Member.EXTENSION_ATTRIBUTE_NAME,extension.getUsername());
			}

		}

		Cart cart = cartService.getCurrent();
		Member member = memberService.getCurrent();
		if (member.getUsername().equals("13860431130")) {
			return DataBlock.error("体验账号不能购物");
		}
		if (member == null) {
			DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		if (cart == null || cart.isEmpty()) {
			return DataBlock.error("ajax.cart.notEmpty");
		}

		if (cart.getIsLowStock()) {
			return DataBlock.error("ajax.order.cartLowStock");
		}
		Receiver receiver = receiverService.find(receiverId);
		if (receiver == null) {
			return DataBlock.error("ajax.member.order.receiverNotExsit");
		}
		PaymentMethod paymentMethod = paymentMethodService.find(paymentMethodId);
		if (paymentMethod == null) {
			return DataBlock.error("ajax.order.paymentMethodNotExsit");
		}
		ShippingMethod shippingMethod = shippingMethodService.find(shippingMethodId);
		if (shippingMethod == null) {
			return DataBlock.error("ajax.order.shippingMethodNotExsit");
		}
		if (!paymentMethod.getShippingMethods().contains(shippingMethod)) {
			return DataBlock.error("ajax.order.deliveryUnsupported");
		}
		System.out.println("uuid="+uuid);
		Equipment equipment = equipmentService.findByUUID(uuid);
		Set<CouponCode> couponCodes = new HashSet<CouponCode>();
		if(code!=null){
			for (String c:code) {
				CouponCode couponCode = couponCodeService.findByCode(c);
				couponCodes.add(couponCode);
			}
		}
		List<DeliveryCenter> deliveryCenters=new ArrayList<>();
		if(deliveryCenterIds!=null){
			for(Long deliveryCenterId:deliveryCenterIds){
				deliveryCenters.add(deliveryCenterService.find(deliveryCenterId));
			}
		}
		Order order = orderService.create(cart, receiver, paymentMethod, shippingMethod, couponCodes, isInvoice, invoiceTitle, useBalance,tenantIds,memo,deliveryCenters,member, null,OrderSource.app,equipment, null);
		Map<String,Object> map=new HashMap<>();
		map.put("sn",order.getSn());
		map.put("orderStatus",order.getOrderStatus());
		map.put("paymentStatus",order.getPaymentStatus());
		return DataBlock.success(map,"执行成功");
	}


	/**
	 * 我的订单列表
	 * type unshipped 待发货 unpaid 待支付 unreciver 待签收 unreview 待评价
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock list(QueryStatus queryStatus,String keyword,Pageable pageable) {
		Member member = memberService.getCurrent();
		if (member == null) {
			return DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		

		Page<Trade> page = tradeService.findPage(pageable,member,queryStatus,keyword);
		
		return DataBlock.success(TradeListModel.bindData(page.getContent()),page,"执行成功");
	}
	
	/**
	 * 订单统计
	 */
	@RequestMapping(value = "/count", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock count(Pageable pageable) {
		Member member = memberService.getCurrent();
		if (member == null) {
			DataBlock.error(DataBlock.SESSION_INVAILD);
		}

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("unpaid",new Long(tradeService.count(null,member,QueryStatus.unpaid,null)));
		data.put("unshiped",new Long(tradeService.count(null,member,QueryStatus.unshipped,null)));
		data.put("shipped",new Long(tradeService.count(null,member,QueryStatus.shipped,null)));
		data.put("reviewed",new Long(tradeService.count(null,member,QueryStatus.unreview,null)));
		data.put("returned",new Long(tradeService.count(null,member,QueryStatus.unrefunded,null)));
		return DataBlock.success(data,"执行成功");
	}

	/**
	 * 订单明细
	 * id 子订单id
	 */
	@RequestMapping(value = "/view", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock view(Long id) {
		Member member = memberService.getCurrent();
		if (member == null) {
			DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		Trade trade = tradeService.find(id);
		if (trade == null) {
			return DataBlock.error("ajax.member.order.notExist");
			
		}
		Payment payment = paymentService.findByTrade(trade,Payment.Status.success);
		TradeModel model = new TradeModel();
		model.copyFrom(trade);
		if(payment!=null){
			model.setPay_date(payment.getPaymentDate());
		}
		return DataBlock.success(model,"执行成功");
	}

	
	/**
	 * 取消订单
	 * sn 订单号 整单取消时传 sn
	 * id 单商家取消传 id
	 */
	@RequestMapping(value = "/cancel", method = RequestMethod.POST)
	public @ResponseBody
	DataBlock cancel(String sn,Long id) {
		Member member = memberService.getCurrent();
		if (member == null) {
			DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		if (sn!=null) {
			Order order = orderService.findBySn(sn);
			if (!member.equals(order.getMember())) {
				return DataBlock.error("该订单不属于您");
			}
			if (order.getOrderStatus() == OrderStatus.completed) {
				return DataBlock.error("订单已完成");
			}
			if (order.getOrderStatus() == OrderStatus.cancelled) {
				return DataBlock.error("订单已取消");
			}
			if (order != null && member.equals(order.getMember()) && order.getOrderStatus() == OrderStatus.unconfirmed
					&& order.getPaymentStatus() == PaymentStatus.unpaid) {
				if (order.isLocked(member)) {
					return DataBlock.error("ajax.member.order.locked");
				}
				orderService.cancel(order, null);
				return DataBlock.success("success","执行成功");
			}
		} else {
			Trade trade = tradeService.find(id);
			if (!member.equals(trade.getOrder().getMember())) {
				return DataBlock.error("该订单不属于您");
			}
			if (trade.getOrderStatus() == OrderStatus.completed) {
				return DataBlock.error("订单已完成");
			}
			if (trade.getOrderStatus() == OrderStatus.cancelled) {
				return DataBlock.error("订单已取消");
			}
			if (trade.getOrderStatus() == OrderStatus.unconfirmed
					&& trade.getPaymentStatus() == PaymentStatus.unpaid) {
				if (trade.getOrder().isLocked(member)) {
					return DataBlock.error("ajax.member.order.locked");
				}
				orderService.cancel(trade, null);
				return DataBlock.success("success","执行成功");
			}
		}
		return DataBlock.error("取消订单失败了");
	}

	/**
	 * 未发货前取消订单
	 * id 子订单
	 */
	@RequestMapping(value = "/refund", method = RequestMethod.POST)
	public @ResponseBody
	DataBlock refund(Long id) {
		Member member = memberService.getCurrent();
		if (member == null) {
			DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		Trade trade = tradeService.find(id);
		if (trade == null) {
			return DataBlock.error("订单不存在");
		}
		if (trade.isLocked(member)) {
			return DataBlock.error("订单被锁定");
		}
		
		if (trade.getPaymentStatus().equals(PaymentStatus.unpaid) && trade.getShippingStatus().equals(ShippingStatus.unshipped)) {
			orderService.cancel(trade, member);
			return DataBlock.success("success","订单已取消");
		}
		if (trade.isSpReturns() && !trade.getPaymentStatus().equals(PaymentStatus.refundApply) && !trade.getShippingStatus().equals(ShippingStatus.shippedApply)) {
		    orderService.spReturns(trade, null, member);
		    return DataBlock.success("success","已申请退货");
		} else {
			return DataBlock.error("该订单不能申请退货");

		}
	}
	
	/**
	 * 已发货后退货
	 * id 子订单
	 */
	@RequestMapping(value = "/return", method = RequestMethod.POST)
	public @ResponseBody
	DataBlock returns(Long id) {
		Member member = memberService.getCurrent();
		if (member == null) {
			DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		Trade trade = tradeService.find(id);
		if (trade == null) {
			return DataBlock.error("订单不存在");
		}
		if (trade.isLocked(member)) {
			return DataBlock.error("订单被锁定");
		}
		
		if (trade.getPaymentStatus().equals(PaymentStatus.unpaid) && trade.getShippingStatus().equals(ShippingStatus.unshipped)) {
			orderService.cancel(trade, member);
			return DataBlock.success("success","订单已取消");
		}
		if (trade.isSpReturns() && !trade.getPaymentStatus().equals(PaymentStatus.refundApply) && !trade.getShippingStatus().equals(ShippingStatus.shippedApply)) {
		    orderService.spReturns(trade, null, member);
		    return DataBlock.success("success","已申请退货");
		} else {
			return DataBlock.error("该订单不能申请退货");

		}
	}
	
	/**
	 * 买家-签收
	 * id 子订单
	 */
	@RequestMapping(value = "/confirm", method = RequestMethod.POST)
	public @ResponseBody DataBlock confirm(Long id) {
		Member member = memberService.getCurrent();
		if (member == null) {
			DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		try {
			Trade trade = tradeService.find(id);
			if (trade != null && memberService.getCurrent().equals(trade.getOrder().getMember()) ) {
				Order order = trade.getOrder();
				if (order.isLocked(member)) {
					return DataBlock.error("b2c.member.order.locked");
				}
				orderService.sign(order, trade, member);
				return DataBlock.success("success","执行成功");
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return DataBlock.error("签收出错了");
	}

	/**
	 * 提醒卖家发货
	 * params id 子订单
	 * return sn 付款单编号
	 * 
	 */
	@RequestMapping(value = "/remind", method = RequestMethod.POST)
	public @ResponseBody DataBlock remind(Long id) {
		Member member = memberService.getCurrent();
		if (member == null) {
			DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		Trade trade = tradeService.find(id);
		if (trade.getShippingStatus().equals(ShippingStatus.shippedApply)) {
			Message message = EntitySupport.createInitMessage(Message.Type.order,"买家催促处理退货",trade.getOrder().getSn(),
					trade.getTenant().getMember(), null);
			message.setTrade(trade);
			message.setWay(Message.Way.tenant);
			messageService.save(message);
		} else {
			Message message = EntitySupport.createInitMessage(Message.Type.order,"买家催促尽快发货",trade.getOrder().getSn(),
					trade.getTenant().getMember(), null);
			message.setTrade(trade);
			message.setWay(Message.Way.tenant);
			messageService.save(message);
		}
		return DataBlock.success("success","执行成功");
	}
	
	/**
	 * 发起支付-单个子订单
	 * id 子订单号
	 */
	@RequestMapping(value = "/payment/{id}", method = RequestMethod.POST)
	@ResponseBody
	public DataBlock tradePayment(@PathVariable Long id) {
		Member member = memberService.getCurrent();
		if (member == null) {
			DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		Payment payment = new Payment();
		Trade trade = tradeService.find(id);
		if (trade.getOrder().isLocked(member)) {
			return DataBlock.error("b2c.member.order.locked");
		}

			if (!member.equals(trade.getOrder().getMember()) || trade.getOrder().isExpired()) {
				return DataBlock.error("订单已过期");
			}
			if(OrderStatus.cancelled.equals(trade.getOrderStatus())){
				return DataBlock.error("订单已取消");
			}
			if (trade.getPaymentStatus() != PaymentStatus.unpaid && trade.getPaymentStatus() != PaymentStatus.partialPayment) {
				return DataBlock.error("订单已无效");
			}
			if (trade.getOrder().getAmountPayable().compareTo(new BigDecimal(0)) <= 0) {
				return DataBlock.error("付款金额不能为0");
			}
			payment.setMember(trade.getOrder().getMember());
			payment.setPayer(trade.getOrder().getMember().getName());
			payment.setMemo("订单支付");
			payment.setSn(snService.generate(Sn.Type.payment));
			payment.setType(Type.payment);
			payment.setMethod(Method.online);
			payment.setStatus(Status.wait);
			payment.setPaymentMethod("");
			payment.setFee(BigDecimal.ZERO);
			payment.setAmount(trade.getAmount());
			payment.setPaymentPluginId("");
			payment.setExpire(DateUtils.addMinutes(new Date(), 3600));
			payment.setTrade(trade);
			payment.setOrder(trade.getOrder());
			paymentService.save(payment);
		return DataBlock.success(payment.getSn(),"执行成功");
	}
	
	/**
	 * 发起支付--整单合并支付
	 * sn 订单号
	 */
	@RequestMapping(value = "/payment", method = RequestMethod.POST)
	@ResponseBody
	public DataBlock payment(String sn) {
		Member member = memberService.getCurrent();
		if (member == null) {
			DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		Payment payment = new Payment();
		Order order = orderService.findBySn(sn);
		if (order.isLocked(member)) {
			return DataBlock.error("b2c.member.order.locked");
		}

			if (order == null || !member.equals(order.getMember()) || order.isExpired()) {
				return DataBlock.error("订单已过期");
			}
			if(OrderStatus.cancelled.equals(order.getOrderStatus())){
				return DataBlock.error("订单已取消");
			}
			if (order.getPaymentStatus() != PaymentStatus.unpaid && order.getPaymentStatus() != PaymentStatus.partialPayment) {
				return DataBlock.error("订单已无效");
			}
			if (order.getAmountPayable().compareTo(new BigDecimal(0)) <= 0) {
				return DataBlock.error("付款金额不能为0");
			}
			payment.setMember(order.getMember());
			payment.setPayer(order.getMember().getName());
			payment.setMemo("订单支付");
			payment.setSn(snService.generate(Sn.Type.payment));
			payment.setType(Type.payment);
			payment.setMethod(Method.online);
			payment.setStatus(Status.wait);
			payment.setPaymentMethod("");
			payment.setFee(BigDecimal.ZERO);
			payment.setAmount(order.getAmountPayable());
			payment.setPaymentPluginId("");
			payment.setExpire(DateUtils.addMinutes(new Date(), 3600));
			payment.setOrder(order);
			paymentService.save(payment);
		return DataBlock.success(payment.getSn(),"执行成功");
	}
	
	/**
	 * 查询三方物流动态
	 */
	@RequestMapping(value = "/delivery_query", method = RequestMethod.GET)
	public @ResponseBody
	DataBlock deliveryQuery(String sn) {
		Map<String, Object> data = new HashMap<String, Object>();
		Member member = memberService.getCurrent();
		if (member == null) {
			DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		Shipping shipping = shippingService.findBySn(sn);
		Setting setting = SettingUtils.get();
		if (shipping != null && shipping.getOrder() != null && memberService.getCurrent().equals(shipping.getOrder().getMember())
				&& StringUtils.isNotEmpty(setting.getKuaidi100Key()) && StringUtils.isNotEmpty(shipping.getDeliveryCorpCode()) && StringUtils.isNotEmpty(shipping.getTrackingNo())) {
			data = shippingService.query(shipping);
		}
		return DataBlock.success(data,"执行成功");
	}

	/**退货维修订单列表*/
	@RequestMapping(value = "/return/list", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock returnList(Pageable pageable){
		Member member=memberService.getCurrent();
		Page<SpReturns> page =spReturnsService.findPage(member,null,pageable);
		return DataBlock.success(SpReturnListModel.bindData(page.getContent()),"执行成功");
	}



}