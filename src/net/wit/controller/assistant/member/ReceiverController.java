/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.assistant.member;

import net.wit.Filter;
import net.wit.Order;
import net.wit.controller.assistant.BaseController;
import net.wit.controller.assistant.model.DataBlock;
import net.wit.controller.assistant.model.ReceiverModel;
import net.wit.entity.Area;
import net.wit.entity.Member;
import net.wit.entity.Receiver;
import net.wit.service.AreaService;
import net.wit.service.MemberService;
import net.wit.service.ReceiverService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Controller - 会员中心 - 收货地址
 * 
 * @author rsico Team
 * @version 3.0
 */    
@Controller("assistantMemberReceiverController")
	@RequestMapping("/assistant/member/receiver")
public class ReceiverController extends BaseController {
	
	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	@Resource(name = "areaServiceImpl")
	private AreaService areaService;
	@Resource(name = "receiverServiceImpl")
	private ReceiverService receiverService;

	/**
	 * 获取收货地址
	 */
	@RequestMapping(value = "/getReceivers", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock getReceivers() {
		Member member = memberService.getCurrent();
		if (member==null) {
			DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		List<Filter> filters=new ArrayList<>();
		filters.add(new Filter("member", Filter.Operator.eq,member));
		List<Order> orders=new ArrayList<>();
		orders.add(new Order("isDefault", Order.Direction.desc));
		orders.add(new Order("id", Order.Direction.asc));
		List<Receiver> receivers=receiverService.findList(null,filters,orders);
		return DataBlock.success(ReceiverModel.bindData(receivers),"执行成功");
	}

	/**
	 * 获取收货地址
	 */
	@RequestMapping(value = "/setIsDefault", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock setIsDefault(Long id) {
		Member member = memberService.getCurrent();
		if (member==null) {
			DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		Set<Receiver> receivers  = member.getReceivers();
		for (Receiver receiver:receivers) {
			if (receiver.getId().equals(id)) {
				receiver.setIsDefault(true);
			} else {
				receiver.setIsDefault(false);
			}
			receiverService.update(receiver);
		}
		return DataBlock.success("success","执行成功");
	}
	/**
	 * 保存收货地址
	 * areaId 所属区域
     * 收货人  consignee;
     * 地区名称  areaName;
     * 地址  address;
     * 邮编  zipCode;
     * 电话 phone;
     * 是否默认 isDefault;
	 */
	@RequestMapping(value = "/save_receiver", method = RequestMethod.POST)
	@ResponseBody
	public DataBlock saveReceiver(Receiver receiver, Long areaId,HttpServletRequest request) {
		Member member = memberService.getCurrent();
		if (member==null) {
			DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		Area area = areaService.find(areaId);
		if(area==null){
			return DataBlock.error("ajax.area.NotExist");
		}
		receiver.setArea(area);

		if (receiver.getArea() != null) {
			receiver.setZipCode(receiver.getArea().getZipCode());
		}
		if(receiver.getZipCode()==null){
			receiver.setZipCode("000000");
		}

		if (Receiver.MAX_RECEIVER_COUNT != null && member.getReceivers().size() >= Receiver.MAX_RECEIVER_COUNT) {
			return DataBlock.error("shop.order.addReceiverCountNotAllowed", Receiver.MAX_RECEIVER_COUNT);
		}
		receiver.setMember(member);
		if (!isValid(receiver)) {
			return DataBlock.error("ajax.order.receiver.isValid");
		}
		receiverService.save(receiver);
		return DataBlock.success("success","ajax.message.success");
	}
	
	/**
	 * 修改收货地址
	 * areaId 所属区域
     * 收货人  consignee;
     * 地区名称  areaName;
     * 地址  address;
     * 邮编  zipCode;
     * 电话 phone;
     * 是否默认 isDefault;
	 */
	@RequestMapping(value = "/update_receiver", method = RequestMethod.POST)
	@ResponseBody
	public DataBlock update(Receiver receiver, Long id, Long areaId) {
		Member member = memberService.getCurrent();
		if (member==null) {
			DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		receiver.setArea(areaService.find(areaId));
		Receiver pReceiver = receiverService.find(id);
		if (pReceiver == null) {
			return DataBlock.error("ajax.order.receiver.isValid");
		}
		receiver.setMember(member);
		pReceiver.setAddress(receiver.getAddress());
		pReceiver.setArea(receiver.getArea());
		pReceiver.setAreaName(receiver.getArea().getFullName());
		pReceiver.setCommunity(receiver.getCommunity());
		pReceiver.setConsignee(receiver.getConsignee());
		pReceiver.setIsDefault(receiver.getIsDefault());
		pReceiver.setPhone(receiver.getPhone());
		pReceiver.setZipCode(receiver.getZipCode());
		pReceiver.setLocation(receiver.getLocation());
		pReceiver.setZipCode(receiver.getZipCode());
		pReceiver.setMember(receiver.getMember());
		pReceiver.setIsDefault(receiver.getIsDefault());
		if (!isValid(pReceiver)) {
			return DataBlock.error("ajax.order.receiver.isValid");
		}
		receiverService.update(pReceiver);
		return DataBlock.success("success","ajax.message.success");
	}
	
	/**
	 * 删除收货地址
	 * id 删除收货地址
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	DataBlock delete(Long id) {
		Member member = memberService.getCurrent();
		if (member==null) {
			DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		Receiver receiver = receiverService.find(id);
		if (receiver == null) {
			return DataBlock.error("ajax.order.receiver.isValid");
		}
		receiverService.delete(id);
		return DataBlock.success("success","ajax.message.success");
	}

}