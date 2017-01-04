package net.wit.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * @ClassName: Rebate
 * @Description: 利润分配
 * @author Administrator
 * @date 2014年10月14日 上午9:10:49
 */
@Entity
@Table(name = "xx_profit")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_profit_sequence")
public class Profit extends BaseEntity {

	private static final long serialVersionUID = -540706724349671149L;

	/** 分账状态 */
	public enum Status {
		/** 没分账 */
		none,
		/** 已分账 */
		share
	}

	/** 分类状态 */
	public enum Type {
		/** 订单分润 */
		order,
		/** 功能年费 */
		fee
	}

	/** 入账日期 */
	private Date profitDate;

 	/** 分润金额 */
	private BigDecimal amount;

	/** 导购分润 */
	private BigDecimal guideAmount;

	/** 店主分润 */
	private BigDecimal guideOwnerAmount;

	/** 推广分润 */
	private BigDecimal shareAmount;

	/** 推广店主分润 */
	private BigDecimal shareOwnerAmount;

	/** 平台 */
	private BigDecimal providerAmount;

	/** 是否入账 */
	private Status status;

	/** 类型 */
	private Type type;

	/** 层级 */
	private Integer level;
	
	/** 摘要 */
	private String memo;
	
	/** 消费者 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Member member;

	/** 导购员 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Member guide;
	
	/** 导购员所在店 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Tenant guideOwner;
	
	/** 发展者 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Member share;
	
	/** 发展者所在店 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Tenant shareOwner;

	/** 订单 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "orders", nullable = false, updatable = false)
	private Order order;

	public Date getProfitDate() {
		return profitDate;
	}

	public void setProfitDate(Date profitDate) {
		this.profitDate = profitDate;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}


	public BigDecimal getGuideAmount() {
		return guideAmount;
	}

	public void setGuideAmount(BigDecimal guideAmount) {
		this.guideAmount = guideAmount;
	}

	public BigDecimal getGuideOwnerAmount() {
		return guideOwnerAmount;
	}

	public void setGuideOwnerAmount(BigDecimal guideOwnerAmount) {
		this.guideOwnerAmount = guideOwnerAmount;
	}

	public BigDecimal getShareAmount() {
		return shareAmount;
	}

	public void setShareAmount(BigDecimal shareAmount) {
		this.shareAmount = shareAmount;
	}

	public BigDecimal getShareOwnerAmount() {
		return shareOwnerAmount;
	}

	public void setShareOwnerAmount(BigDecimal shareOwnerAmount) {
		this.shareOwnerAmount = shareOwnerAmount;
	}

	public BigDecimal getProviderAmount() {
		return providerAmount;
	}

	public void setProviderAmount(BigDecimal providerAmount) {
		this.providerAmount = providerAmount;
	}

	public Member getGuide() {
		return guide;
	}

	public void setGuide(Member guide) {
		this.guide = guide;
	}

	public Tenant getGuideOwner() {
		return guideOwner;
	}

	public void setGuideOwner(Tenant guideOwner) {
		this.guideOwner = guideOwner;
	}

	public Member getShare() {
		return share;
	}

	public void setShare(Member share) {
		this.share = share;
	}

	public Tenant getShareOwner() {
		return shareOwner;
	}

	public void setShareOwner(Tenant shareOwner) {
		this.shareOwner = shareOwner;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}
	
	
}
