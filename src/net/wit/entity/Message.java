/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;

/**
 * Entity - 消息
 * @author rsico Team
 * @version 3.0
 */
@Entity
@Table(name = "xx_message")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_message_sequence")
public class Message extends BaseEntity {

	private static final long serialVersionUID = -5035343536762850722L;
	
	/** 消息类型  */
	public static enum Type {
		/** 订单提醒  */
		order,
		/** 账单提醒   */
		account,
		/** 系统公告   */
		notice,
		/** 系统消息   */
		message,
		/** 咨询回复   */
		consultation,
		/** 社交圈*/
		contact,
		/** 活动提醒*/
		activity,
		/** 评价提醒*/
	    review,
		/** 红包提醒*/
		redPacket
	}
	
	/** 接收类型  */
	public static enum Way {
		/** 所有  */
		all,
		/** 会员   */
		member,
		/** 企业   */
		tenant
	}
	
	/** 消息模板  */
	public static enum Templete {
		/** 默认  */
		none,
		/** 订单模版   */
		order,
		/** 账单模版   */
		account,
		/** 实名认证模版   */
		idcard,
		/** 卡券库存模版   */
		coupon,
		/** 活动模版   */
		activity
	}
	
	/** 类型 */
	@Expose
	@JsonProperty
	@Column(nullable = false, updatable = false)
	private Type type;
	
	/** 模板 */
	@Expose
	@JsonProperty
	@Column(updatable = false)
	private Templete templete;
	
	/** 接收类型 */
	@Expose
	@JsonProperty
	@Column(nullable = false, updatable = false)
	private Way way;

	/** 标题 */
	@Expose
	@JsonProperty
	@Column(nullable = false, updatable = false)
	private String title;

	/** 内容 */
	@Expose
	@JsonProperty
	@NotEmpty
	@Length(max = 1000)
	@Column(nullable = false, updatable = false, length = 1000)
	private String content;

	/** 图片 */
	@JsonProperty
	private String image;

	/** ip */
	@JsonProperty
	@Column(nullable = false, updatable = false)
	private String ip;
	
	/** 订单编号 */
	@JsonProperty
	private String sn;
	
	/** 订单状态 */
	@JsonProperty
	private String orderStatus;

	/** 是否为草稿 */
	@Expose
	@JsonProperty
	@Column(nullable = false, updatable = false)
	private Boolean isDraft;

	/** 发件人已读 */
	@Expose
	@JsonProperty
	@Column(nullable = false)
	private Boolean senderRead;

	/** 收件人已读 */
	@Expose
	@JsonProperty
	@Column(nullable = false)
	private Boolean receiverRead;

	/** 发件人删除 */
	@Expose
	@JsonProperty
	@Column(nullable = false)
	private Boolean senderDelete;

	/** 收件人删除 */
	@Expose
	@JsonProperty
	@Column(nullable = false)
	private Boolean receiverDelete;

	/** 子订单 */
	@JsonProperty
	@ManyToOne(fetch = FetchType.LAZY)
	private Trade trade;
	
	/** 账户流水 */
	@JsonProperty
	@ManyToOne(fetch = FetchType.LAZY)
	private Deposit deposit;

	/** 卡券 */
	@JsonProperty
	@ManyToOne(fetch = FetchType.LAZY)
	private Coupon coupon;
	
	/** 发件人 */
	@Expose
	@JsonProperty
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(updatable = false)
	private Member sender;

	/** 收件人 */
	@Expose
	@JsonProperty
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(updatable = false)
	private Member receiver;

	/** 原消息 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(updatable = false)
	private Message forMessage;

	/** 回复消息 */
	@Expose
	@JsonProperty
	@OneToMany(mappedBy = "forMessage", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@OrderBy(value = "createDate asc")
	private Set<Message> replyMessages = new HashSet<Message>();
	
	public Message() {
		super();
	}

	public Message(long l) {
		super();
		setId(l);
	}

	
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	// ===========================================getter/setter===========================================//
	/**
	 * 获取标题
	 * @return 标题
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * 设置标题
	 * @param title 标题
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * 获取内容
	 * @return 内容
	 */
	public String getContent() {
		return content;
	}

	/**
	 * 设置内容
	 * @param content 内容
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * 获取ip
	 * @return ip
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * 设置ip
	 * @param ip ip
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * 获取订单号
	 * @return sn
	 */
	public String getSn() {
		return sn;
	}

	/**
	 * 设置sn
	 * @param sn sn
	 */
	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	/**
	 * 获取是否为草稿
	 * @return 是否为草稿
	 */
	public Boolean getIsDraft() {
		return isDraft;
	}

	/**
	 * 设置是否为草稿
	 * @param isDraft 是否为草稿
	 */
	public void setIsDraft(Boolean isDraft) {
		this.isDraft = isDraft;
	}

	/**
	 * 获取发件人已读
	 * @return 发件人已读
	 */
	public Boolean getSenderRead() {
		return senderRead;
	}

	/**
	 * 设置发件人已读
	 * @param senderRead 发件人已读
	 */
	public void setSenderRead(Boolean senderRead) {
		this.senderRead = senderRead;
	}

	/**
	 * 获取收件人已读
	 * @return 收件人已读
	 */
	public Boolean getReceiverRead() {
		return receiverRead;
	}

	/**
	 * 设置收件人已读
	 * @param receiverRead 收件人已读
	 */
	public void setReceiverRead(Boolean receiverRead) {
		this.receiverRead = receiverRead;
	}

	/**
	 * 获取发件人删除
	 * @return 发件人删除
	 */
	public Boolean getSenderDelete() {
		return senderDelete;
	}

	/**
	 * 设置发件人删除
	 * @param senderDelete 发件人删除
	 */
	public void setSenderDelete(Boolean senderDelete) {
		this.senderDelete = senderDelete;
	}

	/**
	 * 获取收件人删除
	 * @return 收件人删除
	 */
	public Boolean getReceiverDelete() {
		return receiverDelete;
	}

	/**
	 * 设置收件人删除
	 * @param receiverDelete 收件人删除
	 */
	public void setReceiverDelete(Boolean receiverDelete) {
		this.receiverDelete = receiverDelete;
	}

	/**
	 * 获取发件人
	 * @return 发件人
	 */
	public Member getSender() {
		return sender;
	}

	/**
	 * 设置发件人
	 * @param sender 发件人
	 */
	public void setSender(Member sender) {
		this.sender = sender;
	}

	/**
	 * 获取收件人
	 * @return 收件人
	 */
	public Member getReceiver() {
		return receiver;
	}

	/**
	 * 设置收件人
	 * @param receiver 收件人
	 */
	public void setReceiver(Member receiver) {
		this.receiver = receiver;
	}

	/**
	 * 获取原消息
	 * @return 原消息
	 */
	public Message getForMessage() {
		return forMessage;
	}

	/**
	 * 设置原消息
	 * @param forMessage 原消息
	 */
	public void setForMessage(Message forMessage) {
		this.forMessage = forMessage;
	}

	/**
	 * 获取回复消息
	 * @return 回复消息
	 */
	public Set<Message> getReplyMessages() {
		return replyMessages;
	}

	/**
	 * 设置回复消息
	 * @param replyMessages 回复消息
	 */
	public void setReplyMessages(Set<Message> replyMessages) {
		this.replyMessages = replyMessages;
	}

	public Trade getTrade() {
		return trade;
	}

	public void setTrade(Trade trade) {
		this.trade = trade;
	}

	public Deposit getDeposit() {
		return deposit;
	}

	public void setDeposit(Deposit deposit) {
		this.deposit = deposit;
	}

	public Coupon getCoupon() {
		return coupon;
	}

	public void setCoupon(Coupon coupon) {
		this.coupon = coupon;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Way getWay() {
		return way;
	}

	public void setWay(Way way) {
		this.way = way;
	}

	public Templete getTemplete() {
		return templete;
	}

	public void setTemplete(Templete templete) {
		this.templete = templete;
	}

	
	
}