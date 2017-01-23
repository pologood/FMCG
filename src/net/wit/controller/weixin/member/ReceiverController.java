/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.weixin.member;

import net.wit.Filter;
import net.wit.Order;
import net.wit.controller.weixin.BaseController;
import net.wit.controller.weixin.model.DataBlock;
import net.wit.controller.weixin.model.ReceiverModel;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 会员中心 - 收货地址
 */
@Controller("weixinReceiverController")
@RequestMapping("/weixin/member/receiver")
public class ReceiverController extends BaseController {

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "receiverServiceImpl")
    private ReceiverService receiverService;

    @Resource(name = "areaServiceImpl")
    private AreaService areaService;


    /**
     * 获取收获地址
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock getReceivers() {
        Member member = memberService.getCurrent();
        if (member == null) {
            return DataBlock.error(DataBlock.SESSION_INVAILD);
        }
        List<Filter> filters = new ArrayList<>();
        filters.add(new Filter("member", Filter.Operator.eq, member));
        List<Order> orders = new ArrayList<>();
        orders.add(new Order("isDefault", Order.Direction.desc));
        orders.add(new Order("id", Order.Direction.asc));
        List<Receiver> receivers = receiverService.findList(null, filters, orders);
        return DataBlock.success(ReceiverModel.bindData(receivers), "执行成功");
    }

    /**
     * 保存收货地址
     * areaId 区域Id
     * consignee 收货人
     * address 详细地址
     * phone 电话
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public DataBlock saveReceiver(Receiver receiver, Long areaId) {
        Member member = memberService.getCurrent();
        if (member == null) {
            return DataBlock.error(DataBlock.SESSION_INVAILD);
        }
        if (Receiver.MAX_RECEIVER_COUNT != null && member.getReceivers().size() >= Receiver.MAX_RECEIVER_COUNT) {
            return DataBlock.error("最多允许添加{0}个收货地址", Receiver.MAX_RECEIVER_COUNT);
        }
        Area area = areaService.find(areaId);
        if (area == null) {
            return DataBlock.error("无效区域Id");
        }
        receiver.setArea(area);
        receiver.setMember(member);
        receiver.setAreaName(area.getFullName());
        receiver.setIsDefault(member.getReceivers().size() == 0);
        receiver.setZipCode(area.getZipCode() == null ? "000000" : area.getZipCode());
        if (!isValid(receiver)) {
            return DataBlock.error("无效的收货地址");
        }
        receiverService.save(receiver);
        return DataBlock.success("success", "操作成功");
    }

    /**
     * 修改收货地址
     * areaId 区域Id
     * consignee 收货人
     * address 详细地址
     * phone 电话
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public DataBlock update(Receiver receiver, Long id, Long areaId) {
        Member member = memberService.getCurrent();
        if (member == null) {
            return DataBlock.error(DataBlock.SESSION_INVAILD);
        }
        Receiver pReceiver = receiverService.find(id);
        if (pReceiver == null) {
            return DataBlock.error("无效的收货地址Id");
        }
        Area area = areaService.find(areaId);
        if (area == null) {
            return DataBlock.error("无效区域Id");
        }
        pReceiver.setMember(member);
        pReceiver.setArea(area);
        pReceiver.setAreaName(area.getFullName());
        pReceiver.setAddress(receiver.getAddress());
        pReceiver.setConsignee(receiver.getConsignee());
        pReceiver.setPhone(receiver.getPhone());
        if (!isValid(pReceiver)) {
            return DataBlock.error("无效的收货地址");
        }
        receiverService.update(pReceiver);
        return DataBlock.success("success", "操作成功");
    }

    /**
     * 删除收货地址
     * <p>
     * id 收货地址Id
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public
    @ResponseBody
    DataBlock delete(Long id) {
        Member member = memberService.getCurrent();
        if (member == null) {
            return DataBlock.error(DataBlock.SESSION_INVAILD);
        }
        Receiver receiver = receiverService.find(id);
        if (receiver == null) {
            return DataBlock.error("无效的收货地址");
        }
        receiverService.delete(id);
        return DataBlock.success("success", "操作成功");
    }


    /**
     * 收货地址设为默认
     * <p>
     * id 收货地址Id
     */
    @RequestMapping(value = "/setIsDefault", method = RequestMethod.POST)
    @ResponseBody
    public DataBlock setIsDefault(Long id) {
        Member member = memberService.getCurrent();
        if (member == null) {
            return DataBlock.error(DataBlock.SESSION_INVAILD);
        }
        Set<Receiver> receivers = member.getReceivers();
        for (Receiver receiver : receivers) {
            if (receiver.getId().equals(id)) {
                receiver.setIsDefault(true);
            } else {
                receiver.setIsDefault(false);
            }
            receiverService.update(receiver);
        }
        return DataBlock.success("success", "执行成功");
    }

    /**
     * 编辑页
     *
     * @param id 收货地址Id
     */
    @RequestMapping(value = "/view", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock view(Long id) {
        Receiver receiver = receiverService.find(id);
        if (receiver == null) {
            return DataBlock.error("收货地址不存在");
        }
        ReceiverModel model = new ReceiverModel();
        model.copyFrom(receiver);
        return DataBlock.success(model, "执行成功");
    }

}