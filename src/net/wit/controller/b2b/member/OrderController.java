/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.b2b.member;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.wit.entity.*;
import net.wit.service.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import net.wit.Message;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.Setting;
import net.wit.controller.weixin.model.CouponCodeModel;
import net.wit.controller.weixin.model.DataBlock;
import net.wit.controller.b2c.BaseController;
import net.wit.domain.AuthenticodeStrategy;
import net.wit.entity.Order.OrderSource;
import net.wit.entity.Order.OrderStatus;
import net.wit.entity.Order.PaymentStatus;
import net.wit.entity.Order.QueryStatus;
import net.wit.entity.Order.ShippingStatus;
import net.wit.entity.ShippingMethod.Method;
import net.wit.entity.SpReturns.ReturnStatus;
import net.wit.plugin.PaymentPlugin;
import net.wit.util.SettingUtils;
import net.wit.util.WebUtils;

/**
 * Controller - 会员中心 - 订单
 * @author rsico Team
 * @version 3.0
 */
@Controller("b2bMemberOrderController")
@RequestMapping("/b2b/member/order")
public class OrderController extends BaseController {

	/** 每页记录数 */
	private static final int PAGE_SIZE = 10;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

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

	@Resource(name = "orderServiceImpl")
	private OrderService orderService;

	@Resource(name = "shippingServiceImpl")
	private ShippingService shippingService;

	@Resource(name = "pluginServiceImpl")
	private PluginService pluginService;

	@Resource(name = "appointmentServiceImpl")
	private AppointmentService appointmentService;

	@Resource(name = "deliveryCorpServiceImpl")
	private DeliveryCorpService deliveryCorpService;

	@Resource(name = "tradeServiceImpl")
	private TradeService tradeService;
	
	@Resource(name = "couponServiceImpl")
	private CouponService couponService;
	
	@Resource(name = "spReturnsServiceImpl")
	private SpReturnsService spReturnsService;
	
	@Resource(name = "snServiceImpl")
	private SnService snService;
	
	@Resource(name = "reviewServiceImpl")
	private ReviewService reviewService;
	
	@Resource(name = "rsaServiceImpl")
    private RSAService rsaService;

	@Resource(name = "activityPlanningServiceImpl")
	private ActivityPlanningService activityPlanningService;

	@Resource
    private AuthenticodeStrategy authenticodeStrategy;

	@Resource(name = "monthlyServiceImpl")
	private MonthlyService monthlyService;
	
	public static final String REGISTER_SECURITYCODE_SESSION = "register_securityCode_session";
	
//	@RequestMapping(value = "/order_submit", method = RequestMethod.GET)
//	public String order_submit(Integer pageNumber, ModelMap model) {
//		Member member = memberService.getCurrent();
//		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
//		model.addAttribute("member", member);
//		model.addAttribute("page", orderService.findPage(member, pageable));
//		model.addAttribute("area",areaService.getCurrent());
//		return "b2c/member/order/order_submit";
//	}
	
	
	/**
	 * 保存收货地址
	 */
	@RequestMapping(value = "/save_receiver", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> saveReceiver(Receiver receiver, Long areaId, boolean checkCodeFlag, String checkCode, HttpServletRequest request) {
		Map<String, Object> data = new HashMap<String, Object>();
		if (checkCodeFlag) {
			if (checkCode == null) {
				data.put("message", Message.error("验证码错误"));
				return data;
			}
			SafeKey safeKey = (SafeKey) request.getSession().getAttribute(REGISTER_SECURITYCODE_SESSION);
			if (safeKey == null || safeKey.getValue() == null || !safeKey.getValue().equals(checkCode)) {
				data.put("message", Message.error("验证码错误"));
				return data;
			}
			if (safeKey.hasExpired()) {
				data.put("message", Message.error("验证码过期"));
				return data;
			}
		}
		receiver.setArea(areaService.find(areaId));
		if (!isValid(receiver)) {
			data.put("message", ERROR_MESSAGE);
			return data;
		}
		Member member = memberService.getCurrent();
		if (Receiver.MAX_RECEIVER_COUNT != null && member.getReceivers().size() >= Receiver.MAX_RECEIVER_COUNT) {
			data.put("message", Message.error("shop.order.addReceiverCountNotAllowed", Receiver.MAX_RECEIVER_COUNT));
			return data;
		}
		receiver.setMember(member);
		receiverService.save(receiver);
		data.put("message", SUCCESS_MESSAGE);
		data.put("receiver", receiver);
		return data;
	}

	/**
	 * 检查支付
	 */
	@RequestMapping(value = "/check_payment", method = RequestMethod.POST)
	public @ResponseBody boolean checkPayment(String sn) {
		Order order = orderService.findBySn(sn);
		if (order != null && memberService.getCurrent().equals(order.getMember()) && order.getPaymentStatus() == PaymentStatus.paid) {
			return true;
		}
		return false;
	}

	/**
	 * 优惠券信息
	 */
	@RequestMapping(value = "/coupon_info", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> couponInfo(String code) {
		Map<String, Object> data = new HashMap<String, Object>();
		Cart cart = cartService.getCurrent();
		if (cart == null || cart.isEmpty()) {
			data.put("message", Message.warn("shop.order.cartNotEmpty"));
			return data;
		}
		if (!cart.isCouponAllowed()) {
			data.put("message", Message.warn("shop.order.couponNotAllowed"));
			return data;
		}
		CouponCode couponCode = couponCodeService.findByCode(code);
		if (couponCode != null && couponCode.getCoupon() != null) {
			Coupon coupon = couponCode.getCoupon();
			if (!coupon.getIsEnabled()) {
				data.put("message", Message.warn("shop.order.couponDisabled"));
				return data;
			}
			if (couponCode.hasExpired()) {
				data.put("message", Message.warn("shop.order.couponHasExpired"));
				return data;
			}
			if (!cart.isValid(coupon)) {
				data.put("message", Message.warn("shop.order.couponInvalid"));
				return data;
			}
			if (couponCode.getIsUsed()) {
				data.put("message", Message.warn("shop.order.couponCodeUsed"));
				return data;
			}
			data.put("message", SUCCESS_MESSAGE);
			data.put("couponName", coupon.getName());
			return data;
		} else {
			data.put("message", Message.warn("shop.order.couponCodeNotExist"));
			return data;
		}
	}
	
	/**
	 * 优惠券信息——处理订单内可用的优惠券
	 */
	@RequestMapping(value = "/coupons", method = RequestMethod.POST)
	public @ResponseBody
	DataBlock coupons(String code) {
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
			if (!coupon.getCoupon().getType().equals(Coupon.Type.tenantCoupon) ||!coupon.getCoupon().getIsEnabled() || coupon.hasExpired() ||!cart.isValid(coupon.getCoupon()) || coupon.getIsUsed()) {
			} else {
				codes.add(coupon);
			}
		}
		return DataBlock.success(CouponCodeModel.bindData(codes),"执行成功");
	}
	
	/**
	 * 信息
	 */
	@RequestMapping(value = "/order_submit", method = RequestMethod.GET)
	public String info(ModelMap model,RedirectAttributes redirectAttributes) {
		Member member = memberService.getCurrent();
		if (member == null) {
			return ERROR_VIEW;
		}
		Cart cart = cartService.getCurrent();
		if (cart == null || cart.isEmpty()) {
			return "redirect:/b2b/cart/list.jhtml";
		}
		if (!isValid(cart)) {
			return "redirect:/b2b/cart/list.jhtml";
		}
		ActivityPlanning activityPlanning = activityPlanningService.getCurrent(null, ActivityPlanning.Type.random);
		if (activityPlanning != null) {
			if (orderService.isLimit(member, activityPlanning.getActivityMaximumOrders())) {
				cartService.bindActivity();
			}
		}
		Order order = orderService.build(cart, null, null, null, null, false, null, false, null,null);
		List<Appointment> appointments = appointmentService.findAll();
		Appointment defaultAppointment = appointmentService.findDefault();
		List<CouponCode> couponCodes = couponCodeService.findEnabledList(member);
		List<CouponCode> codes = new ArrayList<CouponCode>();
		for (CouponCode coupon:couponCodes) {
			if (!coupon.getCoupon().getType().equals(Coupon.Type.tenantCoupon) || !coupon.getCoupon().getIsEnabled() || coupon.hasExpired() ||!cart.isValid(coupon.getCoupon()) || coupon.getIsUsed()) {
			} else {
				codes.add(coupon);
			}
		}
		List<PaymentMethod> paymentMethodList=paymentMethodService.findAll();
		Long defaultPaymentMethodId=null;
		if(paymentMethodList.size()>0){
			for(PaymentMethod paymentMethod:paymentMethodList){
				if(paymentMethod.getMethod()== PaymentMethod.Method.online){
					defaultPaymentMethodId=paymentMethod.getId();
				}
			}
		}
		model.addAttribute("couponCodes",codes);
		model.addAttribute("order", order);
		model.addAttribute("area",areaService.getCurrent());
		model.addAttribute("appointments", appointments);
		model.addAttribute("defaultAppointment", defaultAppointment);
		model.addAttribute("cartToken", cart.getToken());
		model.addAttribute("member", member);
		model.addAttribute("area", areaService.getCurrent());
		model.addAttribute("defaultPaymentMethodId",defaultPaymentMethodId );
		model.addAttribute("shippingMethods", shippingMethodService.findAll());
		return "/b2b/member/order/order_submit";
	}

	/**
	 * 计算
	 */
	@RequestMapping(value = "/calculate", method = RequestMethod.POST)
	public @ResponseBody DataBlock calculate(Long paymentMethodId, Long shippingMethodId, String[] code, @RequestParam(defaultValue = "false") Boolean isInvoice, String invoiceTitle, @RequestParam(defaultValue = "false") Boolean useBalance,
			Long[] tenantIds,String[] memo) {
		Map<String, Object> data = new HashMap<String, Object>();
		Cart cart = cartService.getCurrent();
		if (cart == null || cart.isEmpty()) {
			data.put("message", Message.error("shop.order.cartNotEmpty"));
			return DataBlock.error("购物车是空的");
		}
		PaymentMethod paymentMethod = paymentMethodService.find(paymentMethodId);
		ShippingMethod shippingMethod = shippingMethodService.find(shippingMethodId);
		Set<CouponCode> couponCodes = new HashSet<CouponCode>();
		if(code!=null){
			for (String c:code) {
			   CouponCode couponCode = couponCodeService.findByCode(c);
			   couponCodes.add(couponCode);
			}
		}
		
		Order order = orderService.build(cart, null, paymentMethod, shippingMethod, couponCodes, isInvoice, invoiceTitle, useBalance,tenantIds, memo);

		data.put("message", SUCCESS_MESSAGE);
		data.put("quantity", order.getQuantity());
		data.put("price", order.getPrice());
		data.put("freight", order.getFreight());
		data.put("promotionDiscount", order.getPromotionDiscount());
//		data.put("couponDiscount", order.getCouponDiscount());
		data.put("tax", order.getTax());
		data.put("amountPayable", order.getAmountPayable());
		data.put("couponAmount", "0");
		List<Map<String, Object>> trades=new ArrayList<Map<String, Object>>();
		for (Trade trade : order.getTrades()) {
			Map<String, Object> map=new HashMap<String, Object>();
			map.put("tenantId",trade.getTenant().getId().toString());
			map.put("freight",trade.getFreight());
			map.put("amount",trade.getAmount());
			map.put("couponDiscount", trade.getCouponDiscount());
			trades.add(map);
		}
		data.put("trades", trades);
		return DataBlock.success(data,"执行成功");
	}

	/**
	 * 创建
	 */
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public @ResponseBody Message create(String cartToken, Long receiverId, Long paymentMethodId, Long shippingMethodId, Long point, Long appointmentId,@RequestParam(defaultValue = "false") Boolean isInvoice, String invoiceTitle,
			@RequestParam(defaultValue = "false") Boolean useBalance,Long[] tenantIds, String[] memo, String[] code,HttpServletRequest request, HttpServletResponse response) {
		try {
			Cart cart = cartService.getCurrent();
			if (cart == null || cart.isEmpty()) {
				return Message.warn("shop.order.cartNotEmpty");
			}
			if (!StringUtils.equals(cart.getToken(), cartToken)) {
				return Message.warn("shop.order.cartHasChanged");
			}
			if (cart.getIsLowStock()) {
				return Message.warn("shop.order.cartLowStock");
			}
			Receiver receiver = receiverService.find(receiverId);
			if (receiver == null) {
				return Message.error("shop.order.receiverNotExsit");
			}
			PaymentMethod paymentMethod = paymentMethodService.find(paymentMethodId);
			if (paymentMethod == null) {
				return Message.error("shop.order.paymentMethodNotExsit");
			}
			ShippingMethod shippingMethod = shippingMethodService.find(shippingMethodId);
			if (shippingMethod == null) {
				return Message.error("shop.order.shippingMethodNotExsit");
			}
			Appointment appointment = null;
			if (shippingMethod.getMethod().equals(Method.PRIVY)) {
				appointment = appointmentService.find(appointmentId);
			}
			if (!paymentMethod.getShippingMethods().contains(shippingMethod)) {
				return Message.error("shop.order.deliveryUnsupported");
			}
			if (paymentMethod.getMethod().equals(PaymentMethod.Method.balance)) {
                useBalance = true;
            }
			if (useBalance) {
                Member member = memberService.getCurrent();
                String password = rsaService.decryptParameter("enPassword", request);
                rsaService.removePrivateKey(request);
                if (member.getPaymentPassword() == null) {
                    return Message.error("支付密码未设置,请前往设置！");
                }
                if (!member.getPaymentPassword().equals(DigestUtils.md5Hex(password))) {
                    return Message.error("支付密码错误！");
                }
            }
			Order order = new Order();
    		Set<CouponCode> couponCodes = new HashSet<CouponCode>();
    		if(code!=null){
	    		for (String c:code) {
	    		   CouponCode couponCode = couponCodeService.findByCode(c);
	    		   couponCodes.add(couponCode);
	    		}
    		}
			order = orderService.create(cart, receiver, paymentMethod, shippingMethod, couponCodes, isInvoice, invoiceTitle, useBalance,tenantIds, memo, null, memberService.getCurrent(), appointment, OrderSource.web,null,null);
	        WebUtils.addCookie(request, response, Cart.CART_COUNT, cart.getQuantity() + "", Cart.TIMEOUT);
//            if (order.getPaymentStatus().equals(PaymentStatus.paid)
//                    || order.getPaymentMethod().getMethod().equals(PaymentMethod.Method.offline)) {
//                authenticodeStrategy.sendNotify(order);
//                orderService.pushTo(order);
//            }
			return Message.success(order.getSn());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return Message.error("订单提交失败，系统错误");
		}
		
	}

	/**
	 * 支付
	 */
	@RequestMapping(value = "/order_payment", method = RequestMethod.GET)
	public String payment(String sn, ModelMap model) {
		Order order = orderService.findBySn(sn);
		if (order == null || !memberService.getCurrent().equals(order.getMember()) || order.isExpired() || order.getPaymentMethod() == null) {
			return ERROR_VIEW;
		}
		if (order.getPaymentMethod().getMethod() == PaymentMethod.Method.online) {
			List<PaymentPlugin> paymentPlugins = pluginService.getPaymentPlugins(true);
			if (!paymentPlugins.isEmpty()) {
				PaymentPlugin defaultPaymentPlugin = paymentPlugins.get(0);
				if (order.getPaymentStatus() == PaymentStatus.unpaid || order.getPaymentStatus() == PaymentStatus.partialPayment) {
					model.addAttribute("fee", new BigDecimal(0));
					model.addAttribute("amount", order.getAmountPayable());
				}
				model.addAttribute("defaultPaymentPlugin", defaultPaymentPlugin);
				model.addAttribute("paymentPlugins", paymentPlugins);
			}
		}
		model.addAttribute("order", order);
		model.addAttribute("area", areaService.getCurrent());
		return "/b2b/member/order/order_payment";
	}

	/**
	 * 计算支付金额
	 */
	@RequestMapping(value = "/calculate_amount", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> calculateAmount(String paymentPluginId, String sn) {
		Map<String, Object> data = new HashMap<String, Object>();
		Order order = orderService.findBySn(sn);
		PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin(paymentPluginId);
		if (order == null || !memberService.getCurrent().equals(order.getMember()) || order.isExpired() || order.getPaymentMethod() == null || order.getPaymentMethod().getMethod() == PaymentMethod.Method.offline
				|| paymentPlugin == null || !paymentPlugin.getIsEnabled()) {
			data.put("message", ERROR_MESSAGE);
			return data;
		}
		data.put("message", SUCCESS_MESSAGE);
		data.put("fee", new BigDecimal(0));
		data.put("amount", order.getAmountPayable());
		return data;
	}

	/**
	 * 订单完成
	 */
	@RequestMapping(value = "/order_complete", method = RequestMethod.GET)
	public String complete(String sn, ModelMap model) {
		Order order = orderService.findBySn(sn);
		if (order == null) {
			return ERROR_VIEW;
		}
		Member member = memberService.getCurrent();
		if (!member.getOrders().contains(order)) {
			return ERROR_VIEW;
		}
		model.addAttribute("shippingMethods", shippingMethodService.findAll());
		model.addAttribute("deliveryCorps", deliveryCorpService.findAll());
		model.addAttribute("order", order);
		if (order.getShippingMethod().getMethod().equals(ShippingMethod.Method.F2F)) {
			model.addAttribute("trades", order.getTrades());
		}
		model.addAttribute("member", member);
		return "b2b/member/order/order_complete";
	}
	
	/**
	 * 我的订单
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(QueryStatus queryStatus,String beginDate,String endDate, Pageable pageable, String keywords,ModelMap model) {
		pageable.setPageSize(10);
		if (queryStatus==null) {
			queryStatus = QueryStatus.none;
		}
		Member member = memberService.getCurrent();
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
		Date beginTime=null;
		Date endTime=null;
		if(beginDate!=null){
			try {
				beginTime=dateFormat.parse(beginDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(endDate!=null){
			try {
				endTime=dateFormat.parse(endDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Page<Trade> page = tradeService.findPage(member,queryStatus, beginTime, endTime, keywords, pageable);
		model.addAttribute("member",member);
		model.addAttribute("page", page);

		List<SpReturns> spReturnsList = new ArrayList<>();
		for(Trade trade:page.getContent()){
			for(SpReturns spReturns:trade.getSpReturns()){
				spReturnsList.add(spReturns);
			}
		}
		String isMonthly="false";
		if(monthlyService.isMonthly(member,null)){
			isMonthly="true";
		}
		model.addAttribute("isMonthly",isMonthly);
		Long unpaid = tradeService.count(null,member,QueryStatus.unpaid,null);//待支付
		Long unshipped = tradeService.count(null,member,QueryStatus.unshipped,null); //待发货
		Long shipped = tradeService.count(null,member,QueryStatus.shipped,null);//已发货
		Long unreview = tradeService.count(null,member,QueryStatus.unreview,null);//待评价
		model.addAttribute("spReturns", spReturnsList);
		model.addAttribute("unpaid", unpaid);
		model.addAttribute("unshipped", unshipped);
		model.addAttribute("shipped", shipped);
		model.addAttribute("unreview", unreview);

		model.addAttribute("queryStatus", queryStatus);
		model.addAttribute("menu", "order");
		model.addAttribute("area", areaService.getCurrent());
		model.addAttribute("keywords",keywords);
		model.addAttribute("beginDate",beginDate);
		model.addAttribute("endDate",endDate);
		return "/b2b/member/order/list";
	}
	/**
	 * 我的订单
	 */
	@RequestMapping(value = "/all_list", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> allList(QueryStatus queryStatus,String beginDate,String endDate, Pageable pageable, String keywords,ModelMap model) {
		if (queryStatus==null) {
			queryStatus = QueryStatus.none;
		}
		Member member = memberService.getCurrent();
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
		Date beginTime=null;
		Date endTime=null;
		if(beginDate!=null){
			try {
				beginTime=dateFormat.parse(beginDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(endDate!=null){
			try {
				endTime=dateFormat.parse(endDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		List<Trade> trades = tradeService.findListByExport(member,queryStatus, beginTime, endTime, keywords, null);
		List<Map<String, Object>> trade=new ArrayList<Map<String, Object>>();
		for(Trade trad:trades){
			Map<String, Object> map=new HashMap<>();
			map.put("sn", trad.getOrder().getSn());
			map.put("createData",dateFormat.format(trad.getCreateDate()));
			map.put("amount", trad.getAmount());
			map.put("quantity", trad.getQuantity());
			map.put("name", trad.getTenant().getName());
			map.put("status", trad.getFinalOrderStatus().get(0).getDesc());
			trade.add(map);
		}
		return trade;
	}
	/**
	 * 订单详情
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/order_detail", method = RequestMethod.GET)
	public String detail(Long id, ModelMap model) {
		Member member = memberService.getCurrent();
		Trade trade=tradeService.find(id);
		if(trade==null){
			return ERROR_VIEW;
		}
		boolean isReview =reviewService.hasReviewed(member, trade);
		String flag="false";
		if(isReview){
			flag="true";
		}
		model.addAttribute("menu","order");
		model.addAttribute("trade", trade);
		model.addAttribute("member", member);
		model.addAttribute("area",areaService.getCurrent());
		model.addAttribute("isReview",flag);
		return "b2b/member/order/order_detail";
	}

	//评价及未评价订单
	@RequestMapping(value = "/evaluate/list", method = RequestMethod.GET)
	public String evaluate(QueryStatus queryStatus,Pageable pageable, ModelMap model){
		pageable.setPageSize(10);

		if(queryStatus==null){
			queryStatus=QueryStatus.unreview;
		}

		Member member = memberService.getCurrent();
		Page<Trade> page = tradeService.findPage(pageable,member,queryStatus,null);
		Long unreviewCount = tradeService.count(null,member,QueryStatus.unreview,null);//待评价数
		Long reviewedCount = tradeService.count(null,member,QueryStatus.reviewed,null);//已评价数

		model.addAttribute("page",page);
		model.addAttribute("queryStatus",queryStatus);
		model.addAttribute("unreviewCount",unreviewCount);
		model.addAttribute("reviewedCount",reviewedCount);

		model.addAttribute("menu","evaluate");
		model.addAttribute("area",areaService.getCurrent());
		return "/b2b/member/order/evaluate/list";
	}

	/**
	 * 查看
	 */
	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public String view(String sn, ModelMap model) {
		Order order = orderService.findBySn(sn);
		if (order == null) {
			return ERROR_VIEW;
		}
		Member member = memberService.getCurrent();
		if (!member.getOrders().contains(order)) {
			return ERROR_VIEW;
		}
		model.addAttribute("shippingMethods", shippingMethodService.findAll());
		model.addAttribute("deliveryCorps", deliveryCorpService.findAll());
		model.addAttribute("order", order);
		if (order.getShippingMethod().getMethod().equals(ShippingMethod.Method.F2F)) {
			model.addAttribute("trades", order.getTrades());
		}
		model.addAttribute("member", member);
		return "b2b/member/order/view";
	}

	/**
	 * 取消
	 */
	@RequestMapping(value = "/cancel", method = RequestMethod.POST)
	public @ResponseBody Message cancel(String sn) {
		Member member=memberService.getCurrent();
		Order order = orderService.findBySn(sn);
		if (order != null && memberService.getCurrent().equals(order.getMember()) && !order.isExpired() && order.getOrderStatus() == OrderStatus.unconfirmed && order.getPaymentStatus() == PaymentStatus.unpaid) {
			if (order.isLocked(member)) {
				return Message.warn("shop.member.order.locked");
			}
			orderService.cancel(order, null);
			return SUCCESS_MESSAGE;
		}
		return ERROR_MESSAGE;
	}

	/**
	 * 物流动态
	 */
	@RequestMapping(value = "/delivery_query", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> deliveryQuery(String sn) {
		Map<String, Object> data = new HashMap<String, Object>();
		Shipping shipping = shippingService.findBySn(sn);
		Setting setting = SettingUtils.get();
		if (shipping != null && shipping.getOrder() != null && memberService.getCurrent().equals(shipping.getOrder().getMember()) && StringUtils.isNotEmpty(setting.getKuaidi100Key()) && StringUtils.isNotEmpty(shipping.getDeliveryCorpCode())
				&& StringUtils.isNotEmpty(shipping.getTrackingNo())) {
			data = shippingService.query(shipping);
		}
		return data;
	}

	/**
	 * 退款
	 */
	@RequestMapping(value = "/refund", method = RequestMethod.POST)
	public @ResponseBody Message refund(String sn) {
		Member member=memberService.getCurrent();
		Order order = orderService.findBySn(sn);
		if (order != null && memberService.getCurrent().equals(order.getMember()) && !order.isExpired() && order.getPaymentStatus() == PaymentStatus.paid && order.getShippingStatus() == ShippingStatus.unshipped) {
			if (order.isLocked(member)) {
				return Message.warn("b2b.member.order.locked");
			}
			order.setPaymentStatus(PaymentStatus.refundApply);
			orderService.update(order);
			return SUCCESS_MESSAGE;
		}
		return ERROR_MESSAGE;
	}
	
	/**
	 * 退货
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
	 * 退货管理
	 * @param model
	 * @param returnStatus
	 * @param id
	 * @param pageable
	 * @return
	 */
	@RequestMapping(value = "/return/return_management",method = RequestMethod.GET)
	public String management(ModelMap model,ReturnStatus returnStatus,Long id,Pageable pageable){
		Member member=memberService.getCurrent();
		Page<SpReturns> page =spReturnsService.findPage(member,returnStatus,pageable);
		model.addAttribute("member",member);
		model.addAttribute("area",areaService.getCurrent());
		model.addAttribute("page",page);
		model.addAttribute("return_trade",tradeService.find(id));
		model.addAttribute("id",id);
		model.addAttribute("unconfirmed_size",spReturnsService.findPage(member,ReturnStatus.unconfirmed,pageable).getTotal());
		model.addAttribute("confirmed_size",spReturnsService.findPage(member,ReturnStatus.confirmed,pageable).getTotal());
		model.addAttribute("audited_size",spReturnsService.findPage(member,ReturnStatus.audited,pageable).getTotal());
		model.addAttribute("completed_size",spReturnsService.findPage(member,ReturnStatus.completed,pageable).getTotal());
		model.addAttribute("cancelled_size",spReturnsService.findPage(member,ReturnStatus.cancelled,pageable).getTotal());
		model.addAttribute("returnStatus",returnStatus);
		model.addAttribute("menu","return");
		return "b2b/member/order/return/return_management";
	}
	/**
	 * 申请退货
	 * @param maps
	 * @return
	 */
	@RequestMapping(value = "/create_return",method = RequestMethod.POST)
	@ResponseBody
	public Message create_return(@RequestBody List<Map<String, Object>> maps){
		Member member = memberService.getCurrent();
		if (member == null) {
			DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		Long id=null;
		BigDecimal amount = null;
		for(Map<String, Object> map:maps){
			id=Long.parseLong(map.get("id").toString());
			amount=new BigDecimal(map.get("amount").toString());
			break;
		}
		Trade trade=tradeService.find(id);
		Order order=tradeService.find(id).getOrder();
		if (trade == null) {
			return Message.error("订单不存在");
		}
		if (trade.isLocked(member)) {
			return Message.error("订单被锁定");
		}
		if (trade.getPaymentStatus().equals(PaymentStatus.unpaid) && trade.getShippingStatus().equals(ShippingStatus.unshipped)) {
			orderService.cancel(trade, member);
			return Message.error("该订单已取消");
		}
		List<SpReturnsItem> returnsItems=new ArrayList<SpReturnsItem>();
		SpReturns spReturns=new SpReturns();
		if (trade.isSpReturns() && !trade.getPaymentStatus().equals(PaymentStatus.refundApply) && !trade.getShippingStatus().equals(ShippingStatus.shippedApply)) {
			spReturns.setType(SpReturns.Type.event);
			spReturns.setAddress(order.getAddress());
			spReturns.setArea(order.getAreaName());
			spReturns.setFreight(BigDecimal.ZERO);
			spReturns.setPhone(order.getPhone());
			spReturns.setReturnStatus(ReturnStatus.unconfirmed);
			spReturns.setTrade(trade);
			spReturns.setZipCode(order.getZipCode());
			spReturns.setSn(snService.generate(Sn.Type.returns));
			spReturns.setOperator(member.getUsername());
			spReturns.setShipper(order.getConsignee());
			spReturns.setShippingMethod("退货申请");
			spReturns.setSupplier(trade.getOrderItems().get(0).getSupplier());
			for(Map<String, Object> map:maps){
				SpReturnsItem spReturnsItem=new SpReturnsItem();
				spReturnsItem.setReturnQuantity(Integer.parseInt(map.get("quantity").toString()));
				spReturnsItem.setName(map.get("pname").toString());
				spReturnsItem.setReturns(spReturns);
				spReturnsItem.setShippedQuantity(Integer.parseInt(map.get("shippedQuantity").toString()));
				spReturnsItem.setSn(map.get("sn").toString());
				spReturnsItem.setOrderItem(trade.getOrderItem(map.get("sn").toString()));
				returnsItems.add(spReturnsItem);
			}
			if (trade.getPaymentStatus().equals(PaymentStatus.paid)) {
				spReturns.setAmount(trade.getAmount());
				spReturns.setCost(spReturns.calcCost());
			} else {
				spReturns.setAmount(BigDecimal.ZERO);
				spReturns.setCost(BigDecimal.ZERO);
			}
			spReturns.setReturnsItems(returnsItems);
			orderService.spReturns(trade, spReturns, member);
		    return Message.success("已申请退货");
		} else {
			return Message.error("该订单不能申请退货");
		}
	}
	
	/**
     * 取消退货
     * @param id
     * @return
     */
    @RequestMapping(value = "/cancel_return", method = RequestMethod.POST)
	public @ResponseBody Message cancel(Long id) {
    	Member member=memberService.getCurrent();
    	SpReturns spReturns= spReturnsService.find(id);
    	if(spReturns!=null && spReturns.getReturnStatus() == ReturnStatus.unconfirmed){
    		spReturns.setReturnStatus(ReturnStatus.cancelled);
	    	spReturnsService.update(spReturns);
			return Message.success("取消成功");
    	}
    	return Message.error("取消失败");
	}
    
	/**
	 * 买家-确认
	 */
	@RequestMapping(value = "/confirm", method = RequestMethod.POST)
	public @ResponseBody Message confirm(String sn) {
		Member member=memberService.getCurrent();
		Order order = orderService.findBySn(sn);
		if (order != null && memberService.getCurrent().equals(order.getMember()) && !order.isExpired() && order.getIsCompleteAllowed()) {
			if (order.isLocked(member)) {
				return Message.warn("b2c.member.order.locked");
			}
			order.setOrderStatus(OrderStatus.completed);
			orderService.update(order);
			return SUCCESS_MESSAGE;
		}
		return ERROR_MESSAGE;
	}
}