package net.wit.controller.helper.member;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.wit.service.*;
import org.apache.commons.lang.StringUtils;
import org.nutz.http.Http.multipart;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Coupon;
import net.wit.entity.Member;
import net.wit.entity.Order.OrderStatus;
import net.wit.entity.Order.PaymentStatus;
import net.wit.entity.Order.ShippingStatus;
import net.wit.entity.OrderItem;
import net.wit.entity.PaymentMethod.Method;
import net.wit.entity.SpReturns;
import net.wit.entity.SpReturns.ReturnStatus;
import net.wit.entity.SpReturnsItem;
import net.wit.entity.Tenant;
import net.wit.entity.Trade;

/**
 * Created by Administrator on 2016/7/13.（聚德汇）
 */
@Controller("helperMemberStatisticsDateController")
@RequestMapping("/helper/member/statistics")
public class StatisticsDataController extends BaseController {

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "tenantServiceImpl")
    private TenantService tenantService;
    
    @Resource(name = "tenantRelationServiceImpl")
    private TenantRelationService tenantRelationService;
    
    @Resource(name = "supplierServiceImpl")
    private SupplierService supplierService;
    
    @Resource(name = "tradeServiceImpl")
    private TradeService tradeService;
    
    @Resource(name = "orderItemServiceImpl")
    private OrderItemService orderItemService;
    
    @Resource(name = "spReturnsServiceImpl")
    private SpReturnsService spReturnsService;
    
    @Resource(name = "couponServiceImpl")
    private CouponService couponService;

    @Resource(name = "spReturnsItemServiceImpl")
    private SpReturnsItemService spReturnsItemService;
    /**
     * 销售统计（聚德汇）
     */
    @RequestMapping(value = "/sale_total", method = RequestMethod.GET)
    public String saleTotal(String type,String begin_date, String end_date,Long couponId, String keywords, Model model, Pageable pageable) {
    	Member member = memberService.getCurrent();
        Tenant tenant = member.getTenant();
        Coupon coupon=couponService.find(couponId);
        Date beginDate = null;
        Date endDate = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (StringUtils.isNotBlank(begin_date)) {
            beginDate = sdf.parse(begin_date, new ParsePosition(0));
        }
        if (StringUtils.isNotBlank(end_date)) {
            endDate = sdf.parse(end_date, new ParsePosition(0));
        }
        Page<Trade> page=tradeService.findTradeTotal(tenant, null, beginDate, endDate, keywords, coupon, pageable);
        model.addAttribute("page", page);
        model.addAttribute("begin_date", begin_date);
        model.addAttribute("end_date", end_date);
        model.addAttribute("keywords", keywords);
        model.addAttribute("menu", "sale_total");
        model.addAttribute("type", "order_total");
        return "/helper/member/statistics/sale_total";
    }
    /**
     * 销售统计（聚德汇-导出功能）
     */
    @RequestMapping(value = "/sale_total_export", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> saleTotalExport( String begin_date, String end_date, String keywords) {
    	Member member = memberService.getCurrent();
        Tenant tenant = member.getTenant();
        Date beginDate = null;
        Date endDate = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (StringUtils.isNotBlank(begin_date)) {
            beginDate = sdf.parse(begin_date, new ParsePosition(0));
        }
        if (StringUtils.isNotBlank(end_date)) {
            endDate = sdf.parse(end_date, new ParsePosition(0));
        }
        List<Map<String, Object>> maps=new ArrayList<Map<String, Object>>();
        List<Trade> trades=new ArrayList<Trade>();
        trades=tradeService.findTradeByTenant(tenant, beginDate, endDate, keywords, null,"order_total");
        for(Trade trade:trades){
        	Map<String,Object> map=new HashMap<String,Object>();
            if(trade.getShippingDate()!=null){
                map.put("time", sdf.format(trade.getShippingDate()));
            }else {
                map.put("time","--");
            }
            map.put("date",sdf.format(trade.getCreateDate()));
        	map.put("sn", trade.getOrder().getSn());
        	map.put("pQ",trade.getQuantity());
        	map.put("sQ",trade.getShippedQuantity());
        	map.put("amount", trade.getPrice());
        	map.put("cost", trade.getCost());
        	map.put("promoDis", trade.getPromotionDiscount());
        	map.put("couDis", trade.getCouponDiscount());
        	map.put("offsetAmount", trade.getOffsetAmount());
            map.put("freight",trade.getFreight());
        	Integer quan=0;
        	if(trade.getGiftItems()!=null){
        		for(OrderItem orderItem:trade.getGiftItems()){
        			quan=quan+orderItem.getQuantity();
        		}
        	}
        	map.put("gQ", quan);
        	if(trade.getOrder().getPaymentMethod().getMethod()==Method.balance){
        		map.put("paymentMethod", "余额支付");
        	}else if(trade.getOrder().getPaymentMethod().getMethod()==Method.online){
        		map.put("paymentMethod", "线上支付");
        	}else if(trade.getOrder().getPaymentMethod().getMethod()==Method.offline){
        		map.put("paymentMethod", "线下支付");
        	}
        	if(trade.getOrder().getOrderStatus()==OrderStatus.unconfirmed){
        		map.put("orderStatus", "未确认");
        	}else if(trade.getOrder().getOrderStatus()==OrderStatus.confirmed){
        		map.put("orderStatus", "已确认");
        	}else if(trade.getOrder().getOrderStatus()==OrderStatus.completed){
        		map.put("orderStatus", "已完成");
        	}else if(trade.getOrder().getOrderStatus()==OrderStatus.cancelled){
        		map.put("orderStatus", "已取消");
        	}
        	if(trade.getShippingStatus()==ShippingStatus.accept){
        		map.put("shippingStatus", "已签收");
        	}else if(trade.getShippingStatus()==ShippingStatus.partialReturns){
        		map.put("shippingStatus", "部分退货");
        	}else if(trade.getShippingStatus()==ShippingStatus.partialShipment){
        		map.put("shippingStatus", "部分发货");
        	}else if(trade.getShippingStatus()==ShippingStatus.returned){
        		map.put("shippingStatus", "已退货");
        	}else if(trade.getShippingStatus()==ShippingStatus.shipped){
        		map.put("shippingStatus", "已发货");
        	}else if(trade.getShippingStatus()==ShippingStatus.shippedApply){
        		map.put("shippingStatus", "退货中");
        	}else if(trade.getShippingStatus()==ShippingStatus.unshipped){
        		map.put("shippingStatus", "未发货");
        	}
        	if(trade.getSuppliered()!=null){
        		if(trade.getSuppliered()){
	        		map.put("clearing", "已结算");
	        	}else{
	        		map.put("clearing", "未结算");
	        	}
        	}else{
        		map.put("clearing", "--");
        	}
        	maps.add(map);
        }
        return maps;
    }
    
    /**
     * 产品统计（聚德汇）
     */
    @RequestMapping(value = "/product_total", method = RequestMethod.GET)
    public String productTotal(String type,String begin_date, String end_date,Long couponId, String keywords, Model model, Pageable pageable) {
    	Member member = memberService.getCurrent();
        Tenant tenant = member.getTenant();
        Coupon coupon=couponService.find(couponId);
        Date beginDate = null;
        Date endDate = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (StringUtils.isNotBlank(begin_date)) {
            beginDate = sdf.parse(begin_date, new ParsePosition(0));
        }
        if (StringUtils.isNotBlank(end_date)) {
            endDate = sdf.parse(end_date, new ParsePosition(0));
        }
        Page<Map<String, Object>> page = orderItemService.openPageGroupBy(beginDate, endDate, tenant, keywords,pageable);
        List<Map<String, Object>> maps=new ArrayList<Map<String, Object>>();
        for(Map<String, Object> m:page.getContent()){
        	Map<String, Object> map=new HashMap<String,Object>();
            map.put("create_date", m.get("create_date"));
            map.put("shipping_date",m.get("shipping_date"));
        	map.put("name", m.get("fullName"));
        	map.put("sn", m.get("sn"));
        	if(m.get("barcode")!=null){
        		map.put("barcode", m.get("barcode"));
        	}else{
        		map.put("barcode", "--");
        	}
        	map.put("unit", m.get("unit"));
        	map.put("shippedQuantity", m.get("shippedQuantity"));
        	map.put("returnQuantity", m.get("returnQuantity"));
        	map.put("price", m.get("price"));
        	map.put("cost", m.get("cost"));
        	map.put("totalPrice", m.get("totalPrice"));
        	map.put("totalCost", m.get("totalCost"));
        	map.put("reTotalPrice", m.get("reTotalPrice"));
        	map.put("reTotalCost", m.get("reTotalCost"));
			if(m.get("supplier")!=null){
				Long tenantId=Long.valueOf(m.get("supplier").toString());
				Tenant supplier=tenantService.find(tenantId);
				if(supplier!=null){
					map.put("supplier",supplier.getName());
				}else{
					map.put("supplier","--");
				}
			}else{
				map.put("supplier","--");
			}
        	maps.add(map);
    	}
        model.addAttribute("maps", maps);
        model.addAttribute("page",page);
        model.addAttribute("begin_date", begin_date);
        model.addAttribute("end_date", end_date);
        model.addAttribute("keywords", keywords);
        model.addAttribute("menu", "sale_total");
        model.addAttribute("type", "product_total");
        return "/helper/member/statistics/product_total";
    }
    /**
     * 产品统计（聚德汇-导出）
     */
    @RequestMapping(value = "/product_total_export", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String,Object>> productTotalExport(String type,String begin_date, String end_date,Long couponId, String keywords, Model model, Pageable pageable) {
    	Member member = memberService.getCurrent();
        Tenant tenant = member.getTenant();
        Date beginDate = null;
        Date endDate = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (StringUtils.isNotBlank(begin_date)) {
            beginDate = sdf.parse(begin_date, new ParsePosition(0));
        }
        if (StringUtils.isNotBlank(end_date)) {
            endDate = sdf.parse(end_date, new ParsePosition(0));
        }
        List<Map<String, Object>> maps=new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> orderItems=new ArrayList<Map<String, Object>>();
        orderItems=orderItemService.openListGroupBy(beginDate, endDate, tenant, keywords);
        for(Map<String, Object> m:orderItems){
        		Map<String, Object> map=new HashMap<String,Object>();
                map.put("create_date",sdf.format(m.get("create_date")));
                map.put("shipping_date",m.get("shipping_date"));
	        	map.put("name", m.get("fullName"));
	        	map.put("sn", m.get("sn"));
	        	if(m.get("barcode")!=null){
	        		map.put("barcode", m.get("barcode"));
	        	}else{
	        		map.put("barcode", "--");
	        	}
	        	map.put("unit", m.get("unit"));
	        	map.put("returnQuantity", m.get("returnQuantity"));
	        	map.put("shippedQuantity", m.get("shippedQuantity"));
//        		if(new BigDecimal(m.get("shippedQuantity").toString()).compareTo(BigDecimal.ZERO)>0){
//        			map.put("price",new BigDecimal(m.get("totalPrice").toString()).divide(new BigDecimal(m.get("shippedQuantity").toString())));
//        			map.put("cost",new BigDecimal(m.get("totalCost").toString()).divide(new BigDecimal(m.get("shippedQuantity").toString())));
//        		}else{
        			map.put("price",m.get("price"));
        			map.put("cost",m.get("cost"));
//        		}
	        	map.put("profit",new BigDecimal(m.get("totalPrice").toString()).subtract(new BigDecimal(m.get("totalCost").toString())) );
	        	BigDecimal a=new BigDecimal(m.get("totalPrice").toString()).subtract(new BigDecimal(m.get("totalCost").toString()));
	        	BigDecimal b=new BigDecimal(m.get("totalPrice").toString());
	        	BigDecimal c=new BigDecimal(100);
	        	DecimalFormat df=new DecimalFormat("#.00");
	        	if(b.compareTo(BigDecimal.ZERO)>0){
	        		map.put("profitRate",df.format(a.divide(b,3).multiply(c)));
	        	}else{
	        		map.put("profitRate","0");
	        	}
	        	map.put("totalPrice", m.get("totalPrice"));
	        	map.put("totalCost", m.get("totalCost"));
				if(m.get("supplier")!=null){
					Long tenantId=Long.valueOf(m.get("supplier").toString());
					Tenant supplier=tenantService.find(tenantId);
					if(supplier!=null){
						map.put("supplier",supplier.getName());
					}else{
						map.put("supplier","--");
					}
				}else{
					map.put("supplier","--");
				}

	        	maps.add(map);
        	}
        return maps;
    }
    
    /**
     * 销售明细（聚德汇）
     */
    @RequestMapping(value = "/sale_detail", method = RequestMethod.GET)
    public String saleDetail( String begin_date, String end_date, String keywords, Model model, Pageable pageable) {
    	Member member = memberService.getCurrent();
        Tenant tenant = member.getTenant();
        Date beginDate = null;
        Date endDate = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (StringUtils.isNotBlank(begin_date)) {
            beginDate = sdf.parse(begin_date, new ParsePosition(0));
        }
        if (StringUtils.isNotBlank(end_date)) {
            endDate = sdf.parse(end_date, new ParsePosition(0));
        }
        Page<OrderItem> page = orderItemService.openPage(beginDate, endDate, tenant, keywords,pageable);
        model.addAttribute("page", page);
        model.addAttribute("begin_date", begin_date);
        model.addAttribute("end_date", end_date);
        model.addAttribute("keywords", keywords);
        model.addAttribute("menu", "sale_detail");
        return "/helper/member/statistics/sale_detail";
    }
    /**
     * 销售明细（聚德汇-导出功能）
     */
    @RequestMapping(value = "/sale_detail_export", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> saleDetailExport(Long sellerId, String begin_date, String end_date, String keywords) {
    	Member member = memberService.getCurrent();
        Tenant tenant = member.getTenant();
        Date beginDate = null;
        Date endDate = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (StringUtils.isNotBlank(begin_date)) {
            beginDate = sdf.parse(begin_date, new ParsePosition(0));
        }
        if (StringUtils.isNotBlank(end_date)) {
            endDate = sdf.parse(end_date, new ParsePosition(0));
        }
        List<Map<String, Object>> maps=new ArrayList<Map<String, Object>>();
        List<OrderItem> orderItems=new ArrayList<OrderItem>();
        orderItems=orderItemService.openList(beginDate, endDate, tenant, keywords);
        for(OrderItem orderItem:orderItems){
        		Map<String, Object> map=new HashMap<String,Object>();
                if(orderItem.getTrade().getShippingDate()!=null){
                    map.put("date", sdf.format(orderItem.getTrade().getShippingDate()));
                }else{
                    map.put("date","--");
                }
                map.put("time", sdf.format(orderItem.getTrade().getCreateDate()));
	        	map.put("name", orderItem.getFullName());
	        	map.put("sn", orderItem.getSn());
	        	map.put("username", orderItem.getOrder().getPhone());
	        	if(orderItem.getProduct()!=null){
	        		map.put("barcode", orderItem.getProduct().getBarcode());
	        	}else{
	        		map.put("barcode", "--");
	        	}
	        	map.put("osn", orderItem.getTrade().getOrder().getSn());
	        	map.put("unit", orderItem.getPackagUnitName());
	        	map.put("quantity", orderItem.getShippedQuantity());
	        	map.put("price", orderItem.getPrice());
	        	map.put("totalPrice", new BigDecimal(orderItem.getShippedQuantity()).multiply(orderItem.getPrice()));
	        	map.put("cost", orderItem.getCost());
	        	map.put("totalCost", new BigDecimal(orderItem.getShippedQuantity()).multiply(orderItem.getCost()));
	        	if(orderItem.getSupplier()!=null){
	        		map.put("supplierName", orderItem.getSupplier().getName());
	        	}else{
	        		map.put("supplierName", "--");
	        	}
	        	if(orderItem.getTrade().getOrder().getPaymentMethod().getMethod()==Method.balance){
	        		map.put("settleStatus", "余额支付");
	        	}else if(orderItem.getTrade().getOrder().getPaymentMethod().getMethod()==Method.online){
	        		map.put("settleStatus", "线上支付");
	        	}else if(orderItem.getTrade().getOrder().getPaymentMethod().getMethod()==Method.offline){
	        		map.put("settleStatus", "线下支付");
	        	}
	        	if(orderItem.getTrade().getOrder().getOrderStatus()==OrderStatus.unconfirmed){
	        		map.put("orderStatus", "未确认");
	        	}else if(orderItem.getTrade().getOrder().getOrderStatus()==OrderStatus.confirmed){
	        		map.put("orderStatus", "已确认");
	        	}else if(orderItem.getTrade().getOrder().getOrderStatus()==OrderStatus.completed){
	        		map.put("orderStatus", "已完成");
	        	}else if(orderItem.getTrade().getOrder().getOrderStatus()==OrderStatus.cancelled){
	        		map.put("orderStatus", "已取消");
	        	}
	        	if(orderItem.getTrade().getShippingStatus()==ShippingStatus.accept){
	        		map.put("shippingStatus", "已签收");
	        	}else if(orderItem.getTrade().getShippingStatus()==ShippingStatus.partialReturns){
	        		map.put("shippingStatus", "部分退货");
	        	}else if(orderItem.getTrade().getShippingStatus()==ShippingStatus.partialShipment){
	        		map.put("shippingStatus", "部分发货");
	        	}else if(orderItem.getTrade().getShippingStatus()==ShippingStatus.returned){
	        		map.put("shippingStatus", "已退货");
	        	}else if(orderItem.getTrade().getShippingStatus()==ShippingStatus.shipped){
	        		map.put("shippingStatus", "已发货");
	        	}else if(orderItem.getTrade().getShippingStatus()==ShippingStatus.shippedApply){
	        		map.put("shippingStatus", "退货中");
	        	}else if(orderItem.getTrade().getShippingStatus()==ShippingStatus.unshipped){
	        		map.put("shippingStatus", "未发货");
	        	}
	        	if(orderItem.getTrade().getPaymentStatus()==PaymentStatus.paid){
	        		map.put("paymentStatus","已支付");
	        	}else if(orderItem.getTrade().getPaymentStatus()==PaymentStatus.unpaid){
	        		map.put("paymentStatus","未支付");
	        	}else if(orderItem.getTrade().getPaymentStatus()==PaymentStatus.partialPayment){
	        		map.put("paymentStatus","部分支付");
	        	}else if(orderItem.getTrade().getPaymentStatus()==PaymentStatus.partialRefunds){
	        		map.put("paymentStatus","部分退款");
	        	}else if(orderItem.getTrade().getPaymentStatus()==PaymentStatus.refunded){
	        		map.put("paymentStatus","已退款");
	        	}else if(orderItem.getTrade().getPaymentStatus()==PaymentStatus.refundApply){
	        		map.put("paymentStatus","退款中");
	        	}
	        	if(orderItem.getTrade().getSuppliered()!=null){
	        		if(orderItem.getTrade().getSuppliered()==true){
		        		map.put("clearing", "已结算");
		        	}else{
		        		map.put("clearing", "未结算");
		        	}
	        	}else{
	        		map.put("clearing", "--");
	        	}
	        	
	        	maps.add(map);
        	}
        return maps;
    }
    
    /**
     * 退货明细（聚德汇）
     */
    @RequestMapping(value = "/return_detail", method = RequestMethod.GET)
    public String returnDetail(String page_numb, Long sellerId, String begin_date, String end_date, String keywords, Model model, Pageable pageable) {
    	Member member = memberService.getCurrent();
        Tenant tenant = member.getTenant();
        Date beginDate = null;
        Date endDate = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (StringUtils.isNotBlank(begin_date)) {
            beginDate = sdf.parse(begin_date, new ParsePosition(0));
        }
        if (StringUtils.isNotBlank(end_date)) {
            endDate = sdf.parse(end_date, new ParsePosition(0));
        }
        if(page_numb!=null){
        	pageable.setPageNumber(1);
        }
        Page<SpReturns> page = new Page<SpReturns>();
        page=spReturnsService.findPageByTenant(tenant, null, beginDate, endDate, null, null, keywords, pageable);
        model.addAttribute("page", page);
        model.addAttribute("begin_date", begin_date);
        model.addAttribute("end_date", end_date);
        model.addAttribute("keywords", keywords);
        model.addAttribute("menu", "return_detail");
        return "/helper/member/statistics/return_detail";
    }
    /**
     * 退货明细（聚德汇-导出功能）
     */
    @RequestMapping(value = "/return_detail_export", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> returnDetailExport(Long sellerId, String begin_date, String end_date, String keywords) {
    	Member member = memberService.getCurrent();
        Tenant tenant = member.getTenant();
        Date beginDate = null;
        Date endDate = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (StringUtils.isNotBlank(begin_date)) {
            beginDate = sdf.parse(begin_date, new ParsePosition(0));
        }
        if (StringUtils.isNotBlank(end_date)) {
            endDate = sdf.parse(end_date, new ParsePosition(0));
        }
        List<Map<String, Object>> maps=new ArrayList<Map<String, Object>>();
        List<SpReturns> spReturns=new ArrayList<SpReturns>();
        spReturns=spReturnsService.findByTenant(tenant, null, beginDate, endDate, null, keywords);
        for(SpReturns spReturn:spReturns){
        	for(SpReturnsItem returnItem:spReturn.getReturnsItems()){
        		if(returnItem.getReturnQuantity()>0){
	        		Map<String, Object> map=new HashMap<String,Object>();
                    if(returnItem.getReturns().getCompletedDate()!=null){
                        map.put("time", sdf.format(returnItem.getReturns().getCompletedDate()));
                    }else{
                        map.put("time","--");
                    }

                    map.put("date", sdf.format(returnItem.getReturns().getCreateDate()));
	            	map.put("name", returnItem.getName());
	            	map.put("psn", returnItem.getSn());
	            	map.put("rsn", returnItem.getReturns().getSn());
	            	map.put("osn", returnItem.getOrderItem().getTrade().getOrder().getSn());
	            	map.put("username", returnItem.getOrderItem().getOrder().getPhone());
	            	map.put("unit", returnItem.getOrderItem().getPackagUnitName());
	            	map.put("quantity", returnItem.getOrderItem().getQuantity());
	            	map.put("price", returnItem.getOrderItem().getPrice());
	            	map.put("totalPrice", new BigDecimal(returnItem.getOrderItem().getQuantity()).multiply(returnItem.getOrderItem().getPrice()));
	            	map.put("cost", returnItem.getOrderItem().getCost());
	            	map.put("totalCost", new BigDecimal(returnItem.getOrderItem().getQuantity()).multiply(returnItem.getOrderItem().getCost()));
	            	map.put("amount", returnItem.getReturns().getAmount());
	            	map.put("o_amount", returnItem.getOrderItem().getTrade().getAmount());
	            	map.put("returnCost", returnItem.getReturns().getCost());
	            	map.put("shippedQuantity",returnItem.getShippedQuantity());
	            	map.put("returnQuantity", returnItem.getReturnQuantity());
	            	if(returnItem.getOrderItem().getSupplier()!=null){
	            		map.put("supplierName", returnItem.getOrderItem().getSupplier().getName());
	            	}else{
	            		map.put("supplierName", "--");
	            	}
	            	if(returnItem.getOrderItem().getTrade().getOrder().getPaymentMethod().getMethod()==Method.balance){
	            		map.put("settleStatus", "余额支付");
	            	}else if(returnItem.getOrderItem().getTrade().getOrder().getPaymentMethod().getMethod()==Method.online){
	            		map.put("settleStatus", "线上支付");
	            	}else if(returnItem.getOrderItem().getTrade().getOrder().getPaymentMethod().getMethod()==Method.offline){
	            		map.put("settleStatus", "线下支付");
	            	}
	            	if(returnItem.getOrderItem().getTrade().getOrder().getOrderStatus()==OrderStatus.unconfirmed){
	            		map.put("orderStatus", "未确认");
	            	}else if(returnItem.getOrderItem().getTrade().getOrder().getOrderStatus()==OrderStatus.confirmed){
	            		map.put("orderStatus", "已确认");
	            	}else if(returnItem.getOrderItem().getTrade().getOrder().getOrderStatus()==OrderStatus.completed){
	            		map.put("orderStatus", "已完成");
	            	}else if(returnItem.getOrderItem().getTrade().getOrder().getOrderStatus()==OrderStatus.cancelled){
	            		map.put("orderStatus", "已取消");
	            	}
	            	if(returnItem.getOrderItem().getTrade().getShippingStatus()==ShippingStatus.accept){
	            		map.put("shippingStatus", "已签收");
	            	}else if(returnItem.getOrderItem().getTrade().getShippingStatus()==ShippingStatus.partialReturns){
	            		map.put("shippingStatus", "部分退货");
	            	}else if(returnItem.getOrderItem().getTrade().getShippingStatus()==ShippingStatus.partialShipment){
	            		map.put("shippingStatus", "部分发货");
	            	}else if(returnItem.getOrderItem().getTrade().getShippingStatus()==ShippingStatus.returned){
	            		map.put("shippingStatus", "已退货");
	            	}else if(returnItem.getOrderItem().getTrade().getShippingStatus()==ShippingStatus.shipped){
	            		map.put("shippingStatus", "已发货");
	            	}else if(returnItem.getOrderItem().getTrade().getShippingStatus()==ShippingStatus.shippedApply){
	            		map.put("shippingStatus", "退货中");
	            	}else if(returnItem.getOrderItem().getTrade().getShippingStatus()==ShippingStatus.unshipped){
	            		map.put("shippingStatus", "未发货");
	            	}
	            	if(returnItem.getOrderItem().getTrade().getPaymentStatus()==PaymentStatus.paid){
		        		map.put("paymentStatus","已支付");
		        	}else if(returnItem.getOrderItem().getTrade().getPaymentStatus()==PaymentStatus.unpaid){
		        		map.put("paymentStatus","未支付");
		        	}else if(returnItem.getOrderItem().getTrade().getPaymentStatus()==PaymentStatus.partialPayment){
		        		map.put("paymentStatus","部分支付");
		        	}else if(returnItem.getOrderItem().getTrade().getPaymentStatus()==PaymentStatus.partialRefunds){
		        		map.put("paymentStatus","部分退款");
		        	}else if(returnItem.getOrderItem().getTrade().getPaymentStatus()==PaymentStatus.refunded){
		        		map.put("paymentStatus","已退款");
		        	}else if(returnItem.getOrderItem().getTrade().getPaymentStatus()==PaymentStatus.refundApply){
		        		map.put("paymentStatus","退款中");
		        	}
	            	if(returnItem.getReturns().getSuppliered()!=null){
	            		if(returnItem.getReturns().getSuppliered()==true){
		            		map.put("clearing", "已结算");
		            	}else{
		            		map.put("clearing", "未结算");
		            	}
	            	}else{
	            		map.put("clearing", "--");
	            	}
	            	
	            	if(returnItem.getReturns().getReturnStatus()==ReturnStatus.audited){
	            		map.put("returnStatus", "已认证");
	            	}else if(returnItem.getReturns().getReturnStatus()==ReturnStatus.cancelled){
	            		map.put("returnStatus", "已取消");
	            	}else if(returnItem.getReturns().getReturnStatus()==ReturnStatus.completed){
	            		map.put("returnStatus", "已完成");
	            	}else if(returnItem.getReturns().getReturnStatus()==ReturnStatus.confirmed){
	            		map.put("returnStatus", "已确认");
	            	}else if(returnItem.getReturns().getReturnStatus()==ReturnStatus.unconfirmed){
	            		map.put("returnStatus", "未确认");
	            	}
	            	maps.add(map);
        		}
        	}
        }
        return maps;
    }
    /**
     * 退货统计（聚德汇）
     */
    @RequestMapping(value = "/return_total", method = RequestMethod.GET)
    public String returnTotal(String page_numb, Long sellerId, String begin_date, String end_date, String keywords, Model model, Pageable pageable) {
        Member member = memberService.getCurrent();
        Tenant tenant = member.getTenant();
        Date beginDate = null;
        Date endDate = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (StringUtils.isNotBlank(begin_date)) {
            beginDate = sdf.parse(begin_date, new ParsePosition(0));
        }
        if (StringUtils.isNotBlank(end_date)) {
            endDate = sdf.parse(end_date, new ParsePosition(0));
        }
        if(page_numb!=null){
            pageable.setPageNumber(1);
        }
        Page<SpReturns> page = new Page<SpReturns>();
        page=spReturnsService.findPageByTenant(tenant, null, beginDate, endDate, null, null, keywords, pageable);
        model.addAttribute("page", page);
        model.addAttribute("begin_date", begin_date);
        model.addAttribute("end_date", end_date);
        model.addAttribute("keywords", keywords);
        model.addAttribute("menu", "return_total");
        model.addAttribute("type","return_total");
        return "/helper/member/statistics/return_total";
    }
    /**
     * 退货统计（聚德汇-导出功能）
     */
    @RequestMapping(value = "/return_total_export", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> returnTotalExport(Long sellerId, String begin_date, String end_date, String keywords) {
        Member member = memberService.getCurrent();
        Tenant tenant = member.getTenant();
        Date beginDate = null;
        Date endDate = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (StringUtils.isNotBlank(begin_date)) {
            beginDate = sdf.parse(begin_date, new ParsePosition(0));
        }
        if (StringUtils.isNotBlank(end_date)) {
            endDate = sdf.parse(end_date, new ParsePosition(0));
        }
        List<Map<String, Object>> maps=new ArrayList<Map<String, Object>>();
        List<SpReturns> spReturns=new ArrayList<SpReturns>();
        spReturns=spReturnsService.findByTenant(tenant, null, beginDate, endDate, null, keywords);
        for(SpReturns spReturn:spReturns){

                    Map<String, Object> map=new HashMap<String,Object>();
                    if(spReturn.getCompletedDate()!=null){
                        map.put("time", sdf.format(spReturn.getCompletedDate()));
                    }else{
                        map.put("time","--");
                    }
                    map.put("date", sdf.format(spReturn.getCreateDate()));
                    map.put("rsn", spReturn.getSn());
                    map.put("osn", spReturn.getTrade().getOrder().getSn());
                    map.put("username", spReturn.getTrade().getOrder().getPhone());

//                    map.put("quantity", spReturn.getQuantity());
//                    map.put("totalPrice", spReturn.getTrade().getAmount());
                    map.put("cost", spReturn.getCost());
//                    map.put("totalCost", spReturn.getTrade().getCost());
                    map.put("amount", spReturn.getAmount());
//                    map.put("o_amount", returnItem.getOrderItem().getTrade().getAmount());
                    map.put("total", spReturn.getTotal());
                    map.put("shippedQuantity",spReturn.getShippedQuantity());
                    map.put("returnQuantity", spReturn.getReturnQuantity());
                    if(spReturn.getSupplier()!=null){
                        map.put("supplierName", spReturn.getSupplier().getName());
                    }else{
                        map.put("supplierName", "--");
                    }
                    if(spReturn.getTrade().getOrder().getPaymentMethod().getMethod()==Method.balance){
                        map.put("settleStatus", "余额支付");
                    }else if(spReturn.getTrade().getOrder().getPaymentMethod().getMethod()==Method.online){
                        map.put("settleStatus", "线上支付");
                    }else if(spReturn.getTrade().getOrder().getPaymentMethod().getMethod()==Method.offline){
                        map.put("settleStatus", "线下支付");
                    }
                    if(spReturn.getTrade().getOrder().getOrderStatus()==OrderStatus.unconfirmed){
                        map.put("orderStatus", "未确认");
                    }else if(spReturn.getTrade().getOrder().getOrderStatus()==OrderStatus.confirmed){
                        map.put("orderStatus", "已确认");
                    }else if(spReturn.getTrade().getOrder().getOrderStatus()==OrderStatus.completed){
                        map.put("orderStatus", "已完成");
                    }else if(spReturn.getTrade().getOrder().getOrderStatus()==OrderStatus.cancelled){
                        map.put("orderStatus", "已取消");
                    }
                    if(spReturn.getTrade().getShippingStatus()==ShippingStatus.accept){
                        map.put("shippingStatus", "已签收");
                    }else if(spReturn.getTrade().getShippingStatus()==ShippingStatus.partialReturns){
                        map.put("shippingStatus", "部分退货");
                    }else if(spReturn.getTrade().getShippingStatus()==ShippingStatus.partialShipment){
                        map.put("shippingStatus", "部分发货");
                    }else if(spReturn.getTrade().getShippingStatus()==ShippingStatus.returned){
                        map.put("shippingStatus", "已退货");
                    }else if(spReturn.getTrade().getShippingStatus()==ShippingStatus.shipped){
                        map.put("shippingStatus", "已发货");
                    }else if(spReturn.getTrade().getShippingStatus()==ShippingStatus.shippedApply){
                        map.put("shippingStatus", "退货中");
                    }else if(spReturn.getTrade().getShippingStatus()==ShippingStatus.unshipped){
                        map.put("shippingStatus", "未发货");
                    }
                    if(spReturn.getTrade().getPaymentStatus()==PaymentStatus.paid){
                        map.put("paymentStatus","已支付");
                    }else if(spReturn.getTrade().getPaymentStatus()==PaymentStatus.unpaid){
                        map.put("paymentStatus","未支付");
                    }else if(spReturn.getTrade().getPaymentStatus()==PaymentStatus.partialPayment){
                        map.put("paymentStatus","部分支付");
                    }else if(spReturn.getTrade().getPaymentStatus()==PaymentStatus.partialRefunds){
                        map.put("paymentStatus","部分退款");
                    }else if(spReturn.getTrade().getPaymentStatus()==PaymentStatus.refunded){
                        map.put("paymentStatus","已退款");
                    }else if(spReturn.getTrade().getPaymentStatus()==PaymentStatus.refundApply){
                        map.put("paymentStatus","退款中");
                    }
                    if(spReturn.getSuppliered()!=null){
                        if(spReturn.getSuppliered()==true){
                            map.put("clearing", "已结算");
                        }else{
                            map.put("clearing", "未结算");
                        }
                    }else{
                        map.put("clearing", "--");
                    }

                    if(spReturn.getReturnStatus()==ReturnStatus.audited){
                        map.put("returnStatus", "已认证");
                    }else if(spReturn.getReturnStatus()==ReturnStatus.cancelled){
                        map.put("returnStatus", "已取消");
                    }else if(spReturn.getReturnStatus()==ReturnStatus.completed){
                        map.put("returnStatus", "已完成");
                    }else if(spReturn.getReturnStatus()==ReturnStatus.confirmed){
                        map.put("returnStatus", "已确认");
                    }else if(spReturn.getReturnStatus()==ReturnStatus.unconfirmed){
                        map.put("returnStatus", "未确认");
                    }
                    maps.add(map);

        }
        return maps;
    }
    /**
     * 退货产品统计（聚德汇）
     */
    @RequestMapping(value = "/return_product_total", method = RequestMethod.GET)
    public String returnProductTotal(String type,String begin_date, String end_date,Long couponId, String keywords, Model model, Pageable pageable) {
        Member member = memberService.getCurrent();
        Tenant tenant = member.getTenant();
        Coupon coupon=couponService.find(couponId);
        Date beginDate = null;
        Date endDate = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (StringUtils.isNotBlank(begin_date)) {
            beginDate = sdf.parse(begin_date, new ParsePosition(0));
        }
        if (StringUtils.isNotBlank(end_date)) {
            endDate = sdf.parse(end_date, new ParsePosition(0));
        }
        Page<Map<String,Object>> page=spReturnsItemService.returnProductTotal(tenant, null, beginDate, endDate, null, null, keywords, pageable);
        List<Map<String, Object>> maps=new ArrayList<Map<String, Object>>();
        for(Map<String, Object> m:page.getContent()){
            Map<String, Object> map=new HashMap<String,Object>();
            map.put("create_date", m.get("create_date"));
            map.put("completed_date",m.get("completed_date"));
            map.put("name", m.get("fullName"));
            map.put("sn", m.get("sn"));
            if(m.get("barcode")!=null){
                map.put("barcode", m.get("barcode"));
            }else{
                map.put("barcode", "--");
            }
            map.put("unit", m.get("unit"));
            map.put("shippedQuantity", m.get("shippedQuantity"));
            map.put("returnQuantity", m.get("returnQuantity"));
            map.put("price", m.get("price"));
            map.put("cost", m.get("cost"));
            map.put("totalPrice", m.get("totalPrice"));
            map.put("totalCost", m.get("totalCost"));
            map.put("reTotalPrice", m.get("reTotalPrice"));
            map.put("reTotalCost", m.get("reTotalCost"));
            Long tenantId=Long.valueOf(m.get("supplier").toString());
            Tenant supplier=tenantService.find(tenantId);
            if(supplier!=null){
                map.put("supplier",supplier.getName());
            }else{
                map.put("supplier","--");
            }
            maps.add(map);
        }
        model.addAttribute("maps", maps);
        model.addAttribute("page",page);
        model.addAttribute("begin_date", begin_date);
        model.addAttribute("end_date", end_date);
        model.addAttribute("keywords", keywords);
        model.addAttribute("menu", "return_product_total");
        model.addAttribute("type", "return_product_total");
        return "/helper/member/statistics/return_product_total";
    }
    /**
     * 退货产品统计（聚德汇-导出）
     */
    @RequestMapping(value = "/return_product_total_export", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String,Object>> returnProductTotalExp(String type,String begin_date, String end_date,Long couponId, String keywords, Model model, Pageable pageable) {
        Member member = memberService.getCurrent();
        Tenant tenant = member.getTenant();
        Date beginDate = null;
        Date endDate = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (StringUtils.isNotBlank(begin_date)) {
            beginDate = sdf.parse(begin_date, new ParsePosition(0));
        }
        if (StringUtils.isNotBlank(end_date)) {
            endDate = sdf.parse(end_date, new ParsePosition(0));
        }
        List<Map<String, Object>> maps=new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> returnItems=new ArrayList<Map<String, Object>>();
        returnItems=spReturnsItemService.returnProductTotalExp(tenant, null, beginDate,endDate,null,keywords);
        for(Map<String, Object> m:returnItems){
            Map<String, Object> map=new HashMap<String,Object>();
            map.put("create_date",sdf.format(m.get("create_date")));
            map.put("completed_date",sdf.format(m.get("completed_date")));
            map.put("name", m.get("fullName"));
            map.put("sn", m.get("sn"));
            if(m.get("barcode")!=null){
                map.put("barcode", m.get("barcode"));
            }else{
                map.put("barcode", "--");
            }
            map.put("unit", m.get("unit"));
            map.put("returnQuantity", m.get("returnQuantity"));
            map.put("shippedQuantity", m.get("shippedQuantity"));
            map.put("price",m.get("price"));
            map.put("cost",m.get("cost"));
            map.put("profit",new BigDecimal(m.get("totalPrice").toString()).subtract(new BigDecimal(m.get("totalCost").toString())) );
            BigDecimal a=new BigDecimal(m.get("totalPrice").toString()).subtract(new BigDecimal(m.get("totalCost").toString()));
            BigDecimal b=new BigDecimal(m.get("totalPrice").toString());
            BigDecimal c=new BigDecimal(100);
            DecimalFormat df=new DecimalFormat("#.00");
            if(b.compareTo(BigDecimal.ZERO)>0){
                map.put("profitRate",df.format(a.divide(b,3).multiply(c)));
            }else{
                map.put("profitRate","0");
            }
            map.put("totalPrice", m.get("totalPrice"));
            map.put("totalCost", m.get("totalCost"));
            map.put("reTotalPrice", m.get("reTotalPrice"));
            map.put("reTotalCost", m.get("reTotalCost"));
            Long tenantId=Long.valueOf(m.get("supplier").toString());
            Tenant supplier=tenantService.find(tenantId);
            if(supplier!=null){
                map.put("supplier",supplier.getName());
            }else{
                map.put("supplier","--");
            }
            maps.add(map);
        }
        return maps;
    }
    /**
     * 经营分析（聚德汇）
     */
    @RequestMapping(value = "/management_analyse", method = RequestMethod.GET)
    public String managementAnalyse(Pageable pageable, ModelMap model) {
    	
        return "/management_analyse";
    }
}
