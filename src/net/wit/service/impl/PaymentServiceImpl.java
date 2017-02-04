/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.service.impl;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.LockModeType;

import com.alibaba.fastjson.JSONObject;
import net.wit.entity.*;
import net.wit.util.DesUtils;
import net.wit.util.HttpClientUtil;
import net.wit.util.MD5Utils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.PaymentDao;
import net.wit.entity.Order.PaymentStatus;
import net.wit.entity.Payment.Status;
import net.wit.entity.Payment.Type;
import net.wit.service.ApplicationService;
import net.wit.service.CouponService;
import net.wit.service.MemberService;
import net.wit.service.OrderService;
import net.wit.service.PayBillService;
import net.wit.service.PaymentService;
import net.wit.service.TenantService;
import net.wit.service.UnionTenantService;

/**
 * Service - 收款单
 * @author rsico Team
 * @version 3.0
 */
@Service("paymentServiceImpl")
public class PaymentServiceImpl extends BaseServiceImpl<Payment, Long> implements PaymentService {

	@Resource(name = "paymentDaoImpl")
	private PaymentDao paymentDao;

	@Resource(name = "orderServiceImpl")
	private OrderService orderService;

	@Resource(name = "tenantServiceImpl")
	private TenantService tenantService;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	
	@Resource(name = "applicationServiceImpl")
	private ApplicationService applicationService;
	
	@Resource(name = "payBillServiceImpl")
	private PayBillService payBillService;
	
	@Resource(name = "couponServiceImpl")
	private CouponService couponService;

	@Resource(name = "unionTenantServiceImpl")
	private UnionTenantService unionTenantService;

	@Resource(name = "paymentDaoImpl")
	public void setBaseDao(PaymentDao paymentDao) {
		super.setBaseDao(paymentDao);
	}

	@Transactional(readOnly = true)
	public Payment findBySn(String sn) {
		return paymentDao.findBySn(sn);
	}

	@Transactional(readOnly = true)
	public Payment findByPaySn(String paySn) {
		return paymentDao.findByPaySn(paySn);
	}

	public void handle(Payment payment) throws Exception {
		paymentDao.lock(payment, LockModeType.PESSIMISTIC_WRITE);
		paymentDao.refresh(payment, LockModeType.PESSIMISTIC_WRITE);
		if (payment != null && !payment.getStatus().equals(Status.success)) {
			if (payment.getType() == Type.payment) {
				if (payment.getTrade()==null ) {
					Order order = payment.getOrder();
					if (order != null && PaymentStatus.unpaid.equals(order.getPaymentStatus())) {
						    orderService.payment(order, payment, null);
							if (payment.getType().equals(Type.payment)) {
								   if (order.getPaymentStatus().equals(PaymentStatus.paid)) {
									  //authenticodeStrategy.sendNotify(order);
									  orderService.pushTo(order);
							   	   }
							}
					}
				} else {
					Trade trade = payment.getTrade();
					if (trade != null && PaymentStatus.unpaid.equals(trade.getPaymentStatus())) {
					    orderService.payment(trade, payment, null);
						if (payment.getType().equals(Type.payment)) {
							   if (trade.getPaymentStatus().equals(PaymentStatus.paid)) {
								  //authenticodeStrategy.sendNotify(trade);
								  orderService.pushTo(trade);
						   	   }
						}
					}
		     	}
			} else if (payment.getType() == Type.recharge) {
				Member member = payment.getMember();
				if (member != null) {
					memberService.Recharge(member, null, payment.getEffectiveAmount(), "钱包充值", null);
				}
			}  else if (payment.getType() == Type.cashier) {
				Member member = payment.getMember();
				if (member != null) {
					memberService.Cashier(member, null, payment.getEffectiveAmount(), "线下代收", null);
					if(payment.getPayBill()!=null){
						payment.getPayBill().setStatus(PayBill.Status.success);
						payBillService.update(payment.getPayBill());
					}

				}
			} else if (payment.getType() == Type.function) {
				Member member = payment.getMember();
				applicationService.payment(payment,member);
			} else if (payment.getType() == Type.paybill) {
				Member member = payment.getMember();
				payBillService.payment(payment,member);
			} else if (payment.getType() == Type.coupon) {
				couponService.payment(payment);
			} else if (payment.getType() == Type.union) {
				unionTenantService.payment(payment,null);
			}
			if (!payment.getAmount().equals(BigDecimal.ZERO)) { 
			    payment.setStatus(Status.success);
			} else {
				payment.setStatus(Status.failure);
			}
			payment.setPaymentDate(new Date());
			paymentDao.merge(payment);
		}
	}

	public void close(Payment payment) throws Exception {
		paymentDao.refresh(payment, LockModeType.PESSIMISTIC_WRITE);
		if (payment != null && payment.getStatus() == Status.wait) {
			payment.setMemo("超时关闭");
			payment.setStatus(Status.failure);
			paymentDao.merge(payment);
		};
	}

	public String  opService(Payment payment) throws Exception {
		paymentDao.refresh(payment, LockModeType.PESSIMISTIC_WRITE);
		Map<String,Object> data = new HashMap<String,Object>();

		data.put("ins_cd", "1000004");
		data.put("serviceId", "900001");
		data.put("order_no", payment.getPaySn());
		data.put("methodType", payment.getPaymentPluginId());

		String json = JSONObject.toJSONString(data);
		String desStr = DesUtils.getEncString(json, "cloudskycloudskycloudskycloudsky".getBytes());

		String sign = MD5Utils.getMD5Str(json+"f46b51f5793a3fdb8aac939a9422ba77");
		String params = "data="+ URLEncoder.encode(desStr)+"&sign="+sign;

		String rtn_msg = HttpClientUtil.doPost("http://222.92.116.42:18089/cloudsskyApi/opservice",params);
		JSONObject rtn = JSONObject.parseObject(rtn_msg);
		if (rtn.get("ret_code").equals("00")) {
//			try {
//				handle(payment);
				return "0000";
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				return "9999";
//			}
		} else if (rtn.get("ret_code").equals("01")) {
//			System.out.println(URLDecoder.decode(rtn.get("ret_msg").toString()));
//			payment.setStatus(Status.failure);
//			paymentDao.merge(payment);
			return "0001";
		} else {
			return "9999";
		}


	}

	/**
	 * 待支付查询
	 */
	public List<Payment> findWaitReleaseList(Integer first, Integer count) {
		return paymentDao.findWaitReleaseList(first,count);
	}
	
	@Transactional(readOnly = true)
	public Page<Payment> findPage(Member member, Pageable pageable, Payment.Type type) {
		return paymentDao.findPage(member, pageable, type);
	}

	@Transactional(readOnly = true)
	public Page<Payment> findPage(String paymentMethod, Type type, Date beginDate, Date endDate,String tenantName,String username,  Pageable pageable) {
		return paymentDao.findPage(paymentMethod, type, beginDate, endDate,tenantName,username,pageable);
	}
	@Override
	public Payment findByTrade(Trade trade,Payment.Status status) {
		return paymentDao.findByTrade(trade,status);
	}

	@Override
	public Payment findByPayMent(PayBill payBill) {
		return paymentDao.findByPayment(payBill);
	}
}