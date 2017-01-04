/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Entity - 序列号
 * @author rsico Team
 * @version 3.0
 */
@Entity
@Table(name = "xx_sn")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_sn_sequence")
public class Sn extends BaseEntity {

	private static final long serialVersionUID = -2330598144835706164L;

	/** 类型 */
	public enum Type {
		/** 商品		0*/
		product,
		/** 订单		1*/
		order,
		/** 收款单		2*/
		payment,
		/** 退款单		3*/
		refunds,
		/** 发货单		4*/
		shipping,
		/** 退货单		5*/
		returns,
		/** 付款单		6*/
		credit,
		/** 系统跟踪号	7*/
		epaybank,
		/** 手机快充	8*/
		mobile,
		/** 游戏直充	9*/
		game,
		/** 子订单号	10*/
		trade,
		/** 缴费		11*/
		application,
		/** 随机数		12*/
		random,
		/** 促销人员	13*/
		promotionMember,
		/** 邀请码		14*/
		inviteCode,
		/**采购单 		15*/
		purchase,
		/**采购退货单 	16*/
		purchaseReturns,
		/**提现结算单 	17*/
		account,
		/**买单立减		18*/
		paybill
	}

	/** 类型 */
	@Column(nullable = false, updatable = false, unique = true)
	private Type type;

	/** 末值 */
	@Column(nullable = false)
	private Long lastValue;

	// ===========================================getter/setter===========================================//
	/**
	 * 获取类型
	 * @return 类型
	 */
	public Type getType() {
		return type;
	}

	/**
	 * 设置类型
	 * @param type 类型
	 */
	public void setType(Type type) {
		this.type = type;
	}

	/**
	 * 获取末值
	 * @return 末值
	 */
	public Long getLastValue() {
		return lastValue;
	}

	/**
	 * 设置末值
	 * @param lastValue 末值
	 */
	public void setLastValue(Long lastValue) {
		this.lastValue = lastValue;
	}

}