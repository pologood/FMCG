/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.dao.impl;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;

import net.wit.dao.SnDao;
import net.wit.entity.Sn;
import net.wit.entity.Sn.Type;
import net.wit.util.FreemarkerUtils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import freemarker.template.TemplateException;

/**
 * Dao - 序列号
 * @author rsico Team
 * @version 3.0
 */
@Repository("snDaoImpl")
public class SnDaoImpl implements SnDao, InitializingBean {

	private HiloOptimizer productHiloOptimizer;

	private HiloOptimizer orderHiloOptimizer;

	private HiloOptimizer paymentHiloOptimizer;

	private HiloOptimizer refundsHiloOptimizer;

	private HiloOptimizer shippingHiloOptimizer;

	private HiloOptimizer returnsHiloOptimizer;

	private HiloOptimizer creditHiloOptimizer;

	private HiloOptimizer epaybankHiloOptimizer;

	private HiloOptimizer mobileHiloOptimizer;

	private HiloOptimizer gameHiloOptimizer;

	private HiloOptimizer tradeHiloOptimizer;

	private HiloOptimizer applicationHiloOptimizer;

	private HiloOptimizer randomHiloOptimizer;

	private HiloOptimizer promotioMemberHiloOptimizer;

	private HiloOptimizer inviteCodeHiloOptimizer;

	private HiloOptimizer purchaseHiloOptimizer;

	private HiloOptimizer purchaseReturnsHiloOptimizer;

	private HiloOptimizer payBillHiloOptimizer;
	@PersistenceContext
	private EntityManager entityManager;

	@Value("${sn.product.prefix}")
	private String productPrefix;

	@Value("${sn.product.maxLo}")
	private int productMaxLo;

	@Value("${sn.order.prefix}")
	private String orderPrefix;

	@Value("${sn.order.maxLo}")
	private int orderMaxLo;

	@Value("${sn.payment.prefix}")
	private String paymentPrefix;

	@Value("${sn.payment.maxLo}")
	private int paymentMaxLo;

	@Value("${sn.refunds.prefix}")
	private String refundsPrefix;

	@Value("${sn.refunds.maxLo}")
	private int refundsMaxLo;

	@Value("${sn.shipping.prefix}")
	private String shippingPrefix;

	@Value("${sn.shipping.maxLo}")
	private int shippingMaxLo;

	@Value("${sn.returns.prefix}")
	private String returnsPrefix;

	@Value("${sn.returns.maxLo}")
	private int returnsMaxLo;

	@Value("${sn.credit.prefix}")
	private String creditPrefix;

	@Value("${sn.credit.maxLo}")
	private int creditMaxLo;

	@Value("${sn.epaybank.prefix}")
	private String epaybankPrefix;

	@Value("${sn.epaybank.maxLo}")
	private int epaybankMaxLo;

	@Value("${sn.mobile.prefix}")
	private String mobilePrefix;

	@Value("${sn.mobile.maxLo}")
	private int mobileMaxLo;

	@Value("${sn.game.prefix}")
	private String gamePrefix;

	@Value("${sn.game.maxLo}")
	private int gameMaxLo;

	@Value("${sn.trade.prefix}")
	private String tradePrefix;

	@Value("${sn.trade.maxLo}")
	private int tradeMaxLo;

	@Value("${sn.application.prefix}")
	private String applicationPrefix;

	@Value("${sn.application.maxLo}")
	private int applicationMaxLo;

	private String randomPrefix = "";

	@Value("${sn.random.maxLo}")
	private int randomMaxLo;

	@Value("${sn.promotioMember.prefix}")
	private String promotioMemberPrefix;

	@Value("${sn.promotioMember.maxLo}")
	private int promotioMemberMaxLo;

//	@Value("${sn.inviteCode.prefix}")
//	private String inviteCodePrefix;

	private String inviteCodePrefix="";

	@Value("${sn.inviteCode.maxLo}")
	private int inviteCodeMaxLo;


	@Value("${sn.purchase.prefix}")
	private String purchasePrefix;

	@Value("${sn.purchase.maxLo}")
	private int purchaseMaxLo;

	@Value("${sn.purchaseReturns.prefix}")
	private String purchaseReturnsPrefix;

	@Value("${sn.purchaseReturns.maxLo}")
	private int purchaseReturnsMaxLo;

	@Value("${sn.paybill.prefix}")
	private String payBillPrefix;

	@Value("${sn.paybill.maxLo}")
	private int payBillMaxLo;

	public void afterPropertiesSet() throws Exception {
		productHiloOptimizer = new HiloOptimizer(Type.product, productPrefix, productMaxLo);
		orderHiloOptimizer = new HiloOptimizer(Type.order, orderPrefix, orderMaxLo);
		paymentHiloOptimizer = new HiloOptimizer(Type.payment, paymentPrefix, paymentMaxLo);
		refundsHiloOptimizer = new HiloOptimizer(Type.refunds, refundsPrefix, refundsMaxLo);
		shippingHiloOptimizer = new HiloOptimizer(Type.shipping, shippingPrefix, shippingMaxLo);
		returnsHiloOptimizer = new HiloOptimizer(Type.returns, returnsPrefix, returnsMaxLo);
		creditHiloOptimizer = new HiloOptimizer(Type.credit, creditPrefix, creditMaxLo);
		epaybankHiloOptimizer = new HiloOptimizer(Type.epaybank, epaybankPrefix, epaybankMaxLo);
		mobileHiloOptimizer = new HiloOptimizer(Type.mobile, mobilePrefix, mobileMaxLo);
		gameHiloOptimizer = new HiloOptimizer(Type.game, gamePrefix, gameMaxLo);
		tradeHiloOptimizer = new HiloOptimizer(Type.trade, tradePrefix, tradeMaxLo);
		applicationHiloOptimizer = new HiloOptimizer(Type.application, applicationPrefix, applicationMaxLo);
		randomHiloOptimizer = new HiloOptimizer(Type.application, randomPrefix, randomMaxLo);
		promotioMemberHiloOptimizer = new HiloOptimizer(Type.promotionMember, promotioMemberPrefix, promotioMemberMaxLo);
		inviteCodeHiloOptimizer = new HiloOptimizer(Type.inviteCode, inviteCodePrefix, inviteCodeMaxLo);
		purchaseHiloOptimizer  = new HiloOptimizer(Type.purchase,purchasePrefix,purchaseMaxLo);
		purchaseReturnsHiloOptimizer = new HiloOptimizer(Type.purchaseReturns,purchaseReturnsPrefix,purchaseReturnsMaxLo);
		payBillHiloOptimizer = new HiloOptimizer(Type.paybill,payBillPrefix,payBillMaxLo);
	}

	public String generate(Type type) {
		Assert.notNull(type);
		if (type == Type.product) {
			return productHiloOptimizer.generate();
		} else if (type == Type.order) {
			return orderHiloOptimizer.generate();
		} else if (type == Type.payment) {
			return paymentHiloOptimizer.generate();
		} else if (type == Type.refunds) {
			return refundsHiloOptimizer.generate();
		} else if (type == Type.shipping) {
			return shippingHiloOptimizer.generate();
		} else if (type == Type.returns) {
			return returnsHiloOptimizer.generate();
		} else if (type == Type.credit) {
			return creditHiloOptimizer.generate();
		} else if (type == Type.epaybank) {
			return epaybankHiloOptimizer.generate();
		} else if (type == Type.mobile) {
			return mobileHiloOptimizer.generate();
		} else if (type == Type.game) {
			return gameHiloOptimizer.generate();
		} else if (type == Type.trade) {
			return tradeHiloOptimizer.generate();
		} else if (type == Type.application) {
			return applicationHiloOptimizer.generate();
		} else if (type == Type.random) {
			return randomHiloOptimizer.generate();
		} else if (type == Type.promotionMember) {
			return promotioMemberHiloOptimizer.generate();
		} else if (type == Type.inviteCode) {
			return inviteCodeHiloOptimizer.generate();
		}else if(type == Type.purchase){
			return purchaseHiloOptimizer.generate();
		}else if(type == Type.purchaseReturns){
			return purchaseReturnsHiloOptimizer.generate();
		}else if(type == Type.paybill){
			return payBillHiloOptimizer.generate();
		}
		return null;
	}

	private long getLastValue(Type type) {
		String jpql = "select sn from Sn sn where sn.type = :type";
		Sn sn = entityManager.createQuery(jpql, Sn.class).setFlushMode(FlushModeType.COMMIT).setLockMode(LockModeType.PESSIMISTIC_WRITE).setParameter("type", type).getSingleResult();
		long lastValue = sn.getLastValue();
		sn.setLastValue(lastValue + 1);
		entityManager.merge(sn);
		return lastValue;
	}

	/**
	 * 高低位算法
	 */
	private class HiloOptimizer {

		private Type type;

		private String prefix;

		private int maxLo;

		private int lo;

		private long hi;

		private long lastValue;

		public HiloOptimizer(Type type, String prefix, int maxLo) {
			this.type = type;
			this.prefix = prefix != null ? prefix.replace("{", "${") : "";
			this.maxLo = maxLo;
			this.lo = maxLo + 1;
		}

		public synchronized String generate() {
			if (lo > maxLo) {
				lastValue = getLastValue(type);
				lo = lastValue == 0 ? 1 : 0;
				hi = lastValue * (maxLo + 1);
			}
			try {
				return FreemarkerUtils.process(prefix, null) + (hi + lo++);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (TemplateException e) {
				e.printStackTrace();
			}
			return String.valueOf(hi + lo++);
		}
	}

}