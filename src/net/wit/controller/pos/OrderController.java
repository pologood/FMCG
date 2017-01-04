/**
 *====================================================
 * 文件名称: OrderController.java
 * 修订记录：
 * No    日期				作者(操作:具体内容)
 * 1.    2014年6月5日			Administrator(创建:创建文件)
 *====================================================
 * 类描述：(说明未实现或其它不应生成javadoc的内容)
 * 
 */
package net.wit.controller.pos;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import net.wit.Message;
import net.wit.entity.Area;
import net.wit.entity.DeliveryCenter;
import net.wit.entity.Member;
import net.wit.entity.Order;
import net.wit.entity.Order.OrderStatus;
import net.wit.entity.Order.PaymentStatus;
import net.wit.entity.Order.ShippingStatus;
import net.wit.entity.OrderItem;
import net.wit.entity.Shipping;
import net.wit.entity.ShippingItem;
import net.wit.entity.ShippingMethod;
import net.wit.entity.Sn;
import net.wit.entity.Tenant;
import net.wit.entity.Trade;
import net.wit.service.DeliveryCenterService;
import net.wit.service.MemberService;
import net.wit.service.OrderService;
import net.wit.service.SnService;
import net.wit.service.TenantService;
import net.wit.service.TradeService;
import net.wit.util.DateUtil;
import net.wit.util.GsonUtil;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @ClassName: OrderController
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author Administrator
 * @date 2014年6月5日 上午11:37:06
 */
@Controller("posOrderController")
@RequestMapping("/pos/order")
public class OrderController extends BaseController {

	@Resource(name = "tenantServiceImpl")
	private TenantService tenantService;

	@Resource(name = "orderServiceImpl")
	private OrderService orderService;

	@Resource(name = "tradeServiceImpl")
	private TradeService tradeService;
	
	@Resource(name = "snServiceImpl")
	private SnService snService;
	
	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	
	@Resource(name = "deliveryCenterServiceImpl")
	private DeliveryCenterService deliveryCenterService;
	
	/** 列表 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public Message list(Long tenantId, Date startDate, Date endDate, ShippingStatus shippingStatus) {
		if (tenantId == null) {
			return Message.error("商家信息标识为空!");
		}
		try {
			Tenant tenant = tenantService.find(tenantId);
			if (tenant == null) {
				return Message.error("商家信息为空!");
			}
			if (startDate != null) {
				startDate = DateUtil.setStartDay(Calendar.getInstance());
			} 
			if (endDate != null) {
				endDate = DateUtil.setEndDay(Calendar.getInstance());
			} 
			List<ShippingStatus> shippingStatuses = new ArrayList<ShippingStatus>();
			List<OrderStatus> orderStatuses = new ArrayList<OrderStatus>();
			orderStatuses.add(OrderStatus.confirmed);
			List<Order> orderList = orderService.findList(tenant, startDate, endDate, orderStatuses, shippingStatuses);
			String json = GsonUtil.toJson(orderList);
			return Message.success(json);
		} catch (Exception e) {
			return Message.error(e.getMessage());
		}
	}

	/**
	 * 订单锁定
	 */
	boolean lock(Order order,Member member) {
		
		if (order != null && !order.isLocked(member)) {
			order.setLockExpire(DateUtils.addSeconds(new Date(), 20));
			order.setOperator(member);
			orderService.update(order);
			return true;
		}
		return false;
	}

	/**
	 * 检查支付
	 */
	boolean checkPayment(Order order) {
		if (order != null && order.getOrderStatus().equals(OrderStatus.confirmed)) {
			return true;
		}
		return false;
	}

	@RequestMapping(value = "/detail", method = RequestMethod.GET)
	@ResponseBody
	public Message detail(Long orderId,String sn, Long tenantId, String username) {

		if (tenantId == null) {
			return Message.error("商家信息标识为空!");
		}
		Tenant tenant = tenantService.find(tenantId);
		if (tenant == null) {
			return Message.error("商家信息为空!");
		}
		Order order = null;
		if (orderId == null) {
			Trade trade = tradeService.findBySn(sn, tenant);
			if (trade==null) {
				return Message.error("无效提货码!");
			}
			order = trade.getOrder();
		} else {
			order = orderService.find(orderId);			
		}
		Member member = memberService.findByUsername(username);

		if (!lock(order,member)) {
			return Message.error("已经锁定,此订单正在处理中");
		}
		if (!checkPayment(order)) {
			return Message.error("没有支付不能发货!");
		}

		Trade trade = order.getTrade(tenant);
		
		String json = GsonUtil.toJson(trade);
	    json = "{order:{\"sn\":\""+order.getSn()+"\",\"consignee\":\""+order.getConsignee()+"\",\"areaName\":\""+order.getAreaName()+"\",\"address\":\""+order.getAddress()+"\",\"phone\":\""+order.getPhone()+"\",\"username\":\""+order.getMember().getUsername()+"\"},"+json.substring(2);
		return Message.success(json);
	}

	@RequestMapping(value = "/shipped", method = RequestMethod.GET)
	@ResponseBody
	public Message shipped(Long orderId, Long tenantId, String shopId, String username) {
		
		Tenant tenant = tenantService.find(tenantId);
		Order order = orderService.find(orderId);
		
		Member member = memberService.findByUsername(username);
		
		if (!lock(order,member)) {
			return Message.error("已经锁定,此订单正在处理中");
		}
		if (OrderStatus.cancelled == order.getOrderStatus()) {
			return Message.error("订单已经取消!");
		}
		Trade trade = null;
		for (Trade t : order.getTrades()) {
			if (t.getTenant().getId().equals(tenantId)) {
				trade = t;
				break;
			}
		}
		
		if (trade == null) {
			return Message.error("子订单信息查询为空!");
		}
		if (ShippingStatus.shipped == trade.getShippingStatus()) {
			return Message.error("提交订单已经发货!");
		}
		
		Shipping shipping = new Shipping();
		
		
		for (Iterator<OrderItem> iterator = trade.getOrderItems().iterator(); iterator.hasNext();) {
			OrderItem orderItem = iterator.next();
			ShippingItem shippingItem = new ShippingItem();
			shippingItem.setQuantity(orderItem.getQuantity()-orderItem.getShippedQuantity());
			shippingItem.setSn(orderItem.getSn());
			shippingItem.setName(orderItem.getFullName());
			shippingItem.setShipping(shipping);
			shipping.getShippingItems().add(shippingItem);
		}
		shipping.setTrade(trade);
		shipping.setOrder(order);
		ShippingMethod shippingMethod = trade.getOrder().getShippingMethod();
		shipping.setShippingMethod(shippingMethod != null ? shippingMethod.getName() : null);
		DeliveryCenter deliveryCenter = deliveryCenterService.findByCode(tenant, shopId);
		if (deliveryCenter==null) {
			deliveryCenter = deliveryCenterService.findDefault(tenant);
		}
		shipping.setDeliveryCenter(deliveryCenter);
		
		//DeliveryCorp deliveryCorp = deliveryCorpService
		shipping.setDeliveryCorp("线下配送");
		shipping.setDeliveryCorpUrl(null);
		shipping.setDeliveryCorpCode(null);
		Area area = order.getArea();
		shipping.setArea(area != null ? area.getFullName() : null);
		shipping.setAddress(order.getAddress());
		shipping.setPhone(order.getPhone());
		shipping.setZipCode(order.getZipCode());
		shipping.setConsignee(order.getConsignee());

		shipping.setSn(snService.generate(Sn.Type.shipping));
		shipping.setOperator("pos");
		orderService.shipping(order, shipping, null);
				
		return SUCCESS_MESSAGE;
	}

}
