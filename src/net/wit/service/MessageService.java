/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.service;

import java.util.List;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Member;
import net.wit.entity.Message;

/**
 * Service - 消息
 * 
 * @author rsico Team
 * @version 3.0
 */
public interface MessageService extends BaseService<Message, Long> {

	/**
	 * 新增消息列表
	 * 
	 * @param message
	 * 
	 */
	void save(Message message);
	
	/**
	 * 查找消息分页
	 * 
	 * @param member
	 *            会员,null表示管理员
	 * @param pageable
	 *            分页信息
	 * @return 消息分页
	 */
	Page<Message> findPage(Member member, Pageable pageable);

	/**
	 * 查找消息分页
	 * @param receiver 收件人
	 * @param pageable 分页信息
	 * @return 消息分页
	 */
	Page<Message> findReceivePage(Member receiver, Pageable pageable);

	/**
	 * 查找消息分页
	 * 
	 * @param member
	 *            会员,null表示管理员
	 * @param pageable
	 *            分页信息
	 * @return 消息分页
	 */
	Page<Message> findPage(Member member,Boolean read, Pageable pageable);

	/**
	 * 查找草稿分页
	 * 
	 * @param sender
	 *            发件人,null表示管理员
	 * @param pageable
	 *            分页信息
	 * @return 草稿分页
	 */
	Page<Message> findDraftPage(Member sender, Pageable pageable);

	/**
	 * 查找消息数量
	 * 
	 * @param member
	 *            会员，null表示管理员
	 * @param read
	 *            是否已读
	 * @return 消息数量，不包含草稿
	 */
	Long count(Member member, Boolean read);

	Long count(Member member, Boolean read, Message.Type type);

	/**
	 * 删除消息
	 * 
	 * @param id
	 *            ID
	 * @param member
	 *            执行人,null表示管理员
	 */
	void delete(Long id, Member member);

	
	List<Message> findList(Member member, Boolean read, Integer first, Integer count);

	List<Message> findList(Member member, Boolean read, Integer first, Integer count, Message.Type type);
	List<Message> findMessageList(Member member, Boolean read, Integer first, Integer count, Message.Type type);
	List<Message> findMsgOrderList(Member member, Boolean read, Integer first, Integer count, Message.Type type);

}