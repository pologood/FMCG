/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.assistant;

import net.wit.Pageable;
import net.wit.controller.app.BaseController;
import net.wit.controller.app.model.DataBlock;
import net.wit.controller.app.model.PaymentModel;
import net.wit.entity.Member;
import net.wit.entity.Payment;
import net.wit.entity.Payment.Method;
import net.wit.entity.Payment.Status;
import net.wit.plugin.PaymentPlugin;
import net.wit.plugin.PaymentPlugin.NotifyMethod;
import net.wit.service.*;
import net.wit.util.Base64Util;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller - 移动支付
 * @author rsico Team
 * @version 3.0
 */
@Controller("assistantPaymentController")
@RequestMapping("/assistant/payment")
public class PaymentController extends BaseController {

	@Resource(name = "orderServiceImpl")
	private OrderService orderService;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "pluginServiceImpl")
	private PluginService pluginService;

	@Resource(name = "paymentServiceImpl")
	private PaymentService paymentService;

	@Resource(name = "snServiceImpl")
	private SnService snService;

	@Resource(name = "bankInfoServiceImpl")
	private BankInfoService bankInfoService;

	Map<String, String> parameterMap = new HashMap<String, String>();

	Map<String,String> customerInfoMap = new HashMap<String,String>();

	public Map<String, String> getCustomerInfoMap() {
		return customerInfoMap;
	}

	public void setCustomerInfoMap(Map<String, String> customerInfoMap) {
		this.customerInfoMap = customerInfoMap;
	}

	public PaymentService getPaymentService() {
		return paymentService;
	}

	public void setPaymentService(PaymentService paymentService) {
		this.paymentService = paymentService;
	}

	/**
	 * 通知  sync  sn
	 */
	@RequestMapping("/notify/{notifyMethod}/{sn}")
	public String notify(@PathVariable NotifyMethod notifyMethod, @PathVariable String sn, HttpServletRequest request, ModelMap model, Pageable pageable) {
		Payment payment = paymentService.findBySn(sn);
		if (payment != null) {
			PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin(payment.getPaymentPluginId());
			if (paymentPlugin != null && notifyMethod.equals(NotifyMethod.async)) {
				if (paymentPlugin.verifyNotify(sn, notifyMethod, request)) {
					Boolean sended = payment.getStatus().equals(Status.wait);
					try {
						paymentService.handle(payment);
					} catch (Exception e) {
						System.out.println(e.getMessage());
					}
				}
				model.addAttribute("notifyMessage", paymentPlugin.getNotifyMessage(sn, notifyMethod, request));
			}
			if (paymentPlugin != null && notifyMethod.equals(NotifyMethod.sync)) {
				String result = request.getParameter("result");
				if ("0".equals(result)) {
					try {
					  Boolean sended = payment.getStatus().equals(Status.wait);
					  if (paymentPlugin.queryOrder(payment).equals("0000")) {
						  paymentService.handle(payment);
					  }
				    } catch (Exception e) {
						System.out.println(e.getMessage());
					}
		   		    //payment.setStatus(Status.success);
				} else if ("-1".equals(result)) {
					payment.setStatus(Status.wait);
				} else if ("1".equals(result)) {
					payment.setStatus(Status.failure);
				}
			}
			model.addAttribute("notifyMessage", "{\"message\":{\"type\":\"success\",\"content\":\"支付成功\"},\"data\":\"success\"}");
		}
		return "/assistant/payment/notify";
	}
}