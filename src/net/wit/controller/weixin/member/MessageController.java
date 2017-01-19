/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.weixin.member;

import net.wit.Filter;
import net.wit.Filter.Operator;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.controller.weixin.BaseController;
import net.wit.controller.weixin.model.DataBlock;
import net.wit.controller.weixin.model.MessageModel;
import net.wit.entity.Member;
import net.wit.entity.Message;
import net.wit.service.MemberService;
import net.wit.service.MessageService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller - 我的消息
 *
 * @author rsico Team
 * @version 3.0
 */
@Controller("weixinMemberMessageController")
@RequestMapping("/weixin/member/message")
public class MessageController extends BaseController {

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "messageServiceImpl")
    private MessageService messageService;

    /**
     * 消息列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock list(Message.Type type, Pageable pageable) {
        Member member = memberService.getCurrent();
        if (member == null) {
            return DataBlock.error(DataBlock.SESSION_INVAILD);
        }
        if (type != null) {
            List<Filter> filters = new ArrayList<>();
            filters.add(new Filter("type", Operator.eq, type));
            pageable.setFilters(filters);
        }
        Page<Message> page = messageService.findPage(member, pageable);
        for (Message message : page.getContent()) {
            message.setReceiverRead(true);
            messageService.update(message);
        }
        return DataBlock.success(MessageModel.bindData(page.getContent()), "执行成功");
    }

    /**
     * 通过用户获取头像
     */
    @RequestMapping(value = "/heads", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock heads(String[] usernames) {
        Map<String, String> data = new HashMap<>();
        for (String username : usernames) {
            Member member = memberService.findByUsername(username);
            data.put(username, member.getHeadImg());
        }
        return DataBlock.success(data, "执行成功");
    }

    /**
     * 未读消息数和首条消息
     */
    @RequestMapping(value = "/count", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock count() {
        Member member = memberService.getCurrent();
        if (member == null) {
            return DataBlock.error(DataBlock.SESSION_INVAILD);
        }

        Map<String, Object> map = new HashMap<>();
        //未读消息数
        map.put("order", messageService.count(member, false, Message.Type.order,null));
        map.put("account", messageService.count(member, false, Message.Type.account,null));
        map.put("message", messageService.count(member, false, Message.Type.message,null));

//        Pageable pageable = new Pageable(1, 1);
//        Page<Message> page;
//
//        List<Filter> filters = new ArrayList<>();
//        filters.add(new Filter("type", Operator.eq, Message.Type.order));
//        pageable.setFilters(filters);
//        page = messageService.findPage(member, pageable);
//        MessageModel model = new MessageModel();
//        if (page.getContent().size() == 0) {
//            model = null;
//        } else {
//            model.copyFrom(page.getContent().get(0));
//        }
//        //首条订单消息
//        map.put("firstOrder", model);
//
//        filters = new ArrayList<>();
//        filters.add(new Filter("type", Operator.eq, Message.Type.account));
//        pageable.setFilters(filters);
//        page = messageService.findPage(member, pageable);
//        model = new MessageModel();
//        if (page.getContent().size() == 0) {
//            model = null;
//        } else {
//            model.copyFrom(page.getContent().get(0));
//        }
//        //首条账单消息
//        map.put("firstAccount", model);
//
//        filters = new ArrayList<>();
//        filters.add(new Filter("type", Operator.eq, Message.Type.message));
//        pageable.setFilters(filters);
//        page = messageService.findPage(member, pageable);
//        model = new MessageModel();
//        if (page.getContent().size() == 0) {
//            model = null;
//        } else {
//            model.copyFrom(page.getContent().get(0));
//        }
//        //首条系统消息
//        map.put("firstMessage", model);

        return DataBlock.success(map, "执行成功");
    }

    /**
     * 清除消息
     */
    @RequestMapping(value = "/clear", method = RequestMethod.POST)
    @ResponseBody
    public DataBlock clear(Message.Type type) {
        Member member = memberService.getCurrent();
        if (member == null) {
            return DataBlock.error(DataBlock.SESSION_INVAILD);
        }
        List<Message> msgs = messageService.findList(member, null, null, null, type);
        for (Message message : msgs) {
            messageService.delete(message);
        }
        return DataBlock.success("success", "执行成功");
    }


    /**
     * 标记已读
     */
    @RequestMapping(value = "/tag", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock tag(Long[] ids) {
        Member member = memberService.getCurrent();
        if (member == null) {
            return DataBlock.error(DataBlock.SESSION_INVAILD);
        }
        for (Long id : ids) {
            Message message = messageService.find(id);
            message.setReceiverRead(true);
            messageService.update(message);
        }
        return DataBlock.success("success", "执行成功");
    }
}