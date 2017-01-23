/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.service.impl;

import net.wit.Filter;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.BindUserDao;
import net.wit.dao.MessageDao;
import net.wit.entity.*;
import net.wit.entity.BindUser.Type;
import net.wit.entity.Message.Way;
import net.wit.service.MessageService;
import net.wit.support.PushMessage;
import net.wit.weixin.main.MenuManager;
import net.wit.weixin.main.MessageManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * Service - 消息
 *
 * @author rsico Team
 * @version 3.0
 */
@Service("messageServiceImpl")
public class MessageServiceImpl extends BaseServiceImpl<Message, Long> implements MessageService {

    @Resource(name = "messageDaoImpl")
    private MessageDao messageDao;

    @Resource(name = "bindUserDaoImpl")
    private BindUserDao bindUserDao;

    @Resource(name = "messageDaoImpl")
    public void setBaseDao(MessageDao messageDao) {
        super.setBaseDao(messageDao);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"message"}, allEntries = true)
    public void save(Message message) {
        Assert.notNull(message);
        if (message.getWay() == null) {
            message.setWay(Way.all);
        }
        super.save(message);
        messageDao.flush();
//        try {
//            if (message.getReceiver() != null) {
//                Long unReadCount = messageDao.count(message.getReceiver(), false, null,null);
//                PushMessage.aliPush(message, unReadCount);
//            }
//        } catch (Exception e) {
//            // TODO: handle exception
//            System.out.println(e);
//        }
//        if (message.getReceiver() != null && (message.getWay() == Way.member || message.getWay() == Way.all)) {
//            BindUser bindUser = bindUserDao.findByMember(message.getReceiver(), Type._wx);
//            if (bindUser != null) {
//                String data = null;
//                NumberFormat nf = new DecimalFormat("￥,###.##");
//                SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时MM分ss秒");
//                ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
//                if (message.getTemplete() == null || message.getTemplete() == Message.Templete.none) {
//                    if (message.getType().equals(Message.Type.account)) {
//                        Deposit deposit = message.getDeposit();
//                        if (deposit != null) {
//                            String url = MenuManager.codeUrlO2(bundle.getString("WeiXinSiteUrl") + "/weixin/member/purse/index.jhtml");
//                            data = MessageManager.createDepsitTempelete(
//                                    bindUser.getUsername(),
//                                    "您好，账户余额发生变化：",
//                                    url,
//                                    sdf.format(deposit.getCreateDate()),
//                                    nf.format(deposit.getAdCharge()),
//                                    nf.format(deposit.getBalance()),
//                                    message.getContent()
//                            );
//                        }
//                    }
//
//                    if (message.getType().equals(Message.Type.order)) {
//                        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月dd日 HH时MM分ss秒");
//                        Trade trade = message.getTrade();
//                        if (trade != null) {
//                            String url = MenuManager.codeUrlO2(bundle.getString("WeiXinSiteUrl") + "/weixin/member/order/detail.jhtml?id=" + trade.getId());
//                            data = MessageManager.createOrderTempelete(
//                                    bindUser.getUsername(),
//                                    "您好，订单状态发生变化：",
//                                    url,
//                                    trade.getOrder().getSn(),
//                                    trade.getFinalOrderStatus().get(0).getDesc(),
//                                    message.getContent(),
//                                    sdf1.format(message.getCreateDate())
//                            );
//                        }
//                    }
//
//                    if (message.getType().equals(Message.Type.consultation)) {
//                        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月dd日 HH时MM分ss秒");
//
//                        String url = MenuManager.codeUrlO2(bundle.getString("WeiXinSiteUrl") + "/wap/tenant/order/order_info.jhtml?id");
//                        data = MessageManager.createReplyTempelete(
//                                bindUser.getUsername(),
//                                "您好，订单状态发生变化：",
//                                url,
//                                message.getContent(),
//                                message.getContent(),
//                                sdf1.format(message.getCreateDate()),
//                                message.getContent()
//                        );
//                    }
//                    //系统消息中实名认证
//                    if (message.getType().equals(Message.Type.message) && message.getTitle().equals("实名认证")) {
//                        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月dd日 HH时MM分ss秒");
//                        String url = MenuManager.codeUrlO2(bundle.getString("WeiXinSiteUrl") + "/weixin/member/index.jhtml");
//                        data = MessageManager.createCertificationTempelete(
//                                bindUser.getUsername(),
//                                "您好，实名认证状态发生变化：",
//                                url, sdf1.format(message.getCreateDate()),
//                                message.getContent(),
//                                message.getOrderStatus()
//                        );
//                    }
//                } else {
//                    if (message.getTemplete() == Message.Templete.coupon) {
//                        Coupon coupon = message.getCoupon();
//                        if (coupon != null) {
//                            String url = null;
//                            if (message.getWay() == Way.member) {
//                                url = MenuManager.codeUrlO2(bundle.getString("WeiXinSiteUrl") + "/weixin/member/redPacket/red.jhtml");
//                            }
//                            data = MessageManager.createCouponTempelete(
//                                    bindUser.getUsername(),
//                                    "提醒事项：",
//                                    url,
//                                    coupon.getTenant().getName(),
//                                    coupon.getName(),
//                                    coupon.getCount(),
//                                    sdf.format(new Date()),
//                                    message.getContent()
//                            );
//                        }
//                    }
//                }
//                if (data != null) {
//                    MessageManager.sendMsg(data);
//                }
//
//            }
//
//        }
    }

    @Override
    @Transactional
    @CacheEvict(value = {"message"}, allEntries = true)
    public Message update(Message message) {
        Assert.notNull(message);

        Message pMessage = super.update(message);
        messageDao.flush();
        return pMessage;
    }

    @Transactional(readOnly = true)
    public Page<Message> findPage(Member member, Pageable pageable) {
        return messageDao.findPage(member, pageable);
    }

    public Page<Message> findReceivePage(Member receiver, Pageable pageable) {
        return messageDao.findPage(receiver, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Message> findPage(Member member, Boolean read, Pageable pageable) {
        return messageDao.findPage(member, read, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Message> findDraftPage(Member sender, Pageable pageable) {
        return messageDao.findDraftPage(sender, pageable);
    }

    @Transactional(readOnly = true)
    public Long count(Member member, Boolean read) {
        return messageDao.count(member, read, null,null);
    }

    @Transactional(readOnly = true)
    public Long count(Member member, Boolean read, Message.Type type,List<Filter> filters) {
        return messageDao.count(member, read, type,filters);
    }

    public void delete(Long id, Member member) {
        messageDao.remove(id, member);
    }

    public List<Message> findList(Member member, Boolean read, Integer first, Integer count) {
        return messageDao.findList(member, read, first, count, null);
    }

    public List<Message> findList(Member member, Boolean read, Integer first, Integer count, Message.Type type) {
        return messageDao.findList(member, read, first, count, type);
    }

    public List<Message> findMessageList(Member member, Boolean read, Integer first, Integer count, Message.Type type) {
        return messageDao.findMessageList(member, read, first, count, type);
    }

    public List<Message> findMsgOrderList(Member member, Boolean read, Integer first, Integer count, Message.Type type) {
        return messageDao.findMsgOrderList(member, read, first, count, type);
    }
}