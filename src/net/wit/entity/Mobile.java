package net.wit.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * @ClassName: Mobile
 * @Description: 手机快充
 * @author Administrator
 * @date 2014年10月14日 上午9:09:42
 */
@Entity
@Table(name = "xx_mobile")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_mobile_sequence")
public class Mobile extends BaseEntity {

	private static final long serialVersionUID = -245736763497251149L;

	/** 订单号 */
	@Column(nullable = false)
	private String sn;

	/** 充值手机 */
	@Column(nullable = false)
	private String mobile;

	/** 产品编码 */
	@Column(nullable = false)
	private String prodId;
	
	/** 说明 */
	@Column(nullable = false)
	private String descr;

	/** 操作人 */
	@Column(nullable = false)
	private String operator;
	
	/** 交易金额 */
	@Column(nullable = false, precision = 21, scale = 6)
	private BigDecimal amount;

	/** 支付费用 */
	@Column(nullable = false, precision = 21, scale = 6)
	private BigDecimal fee;

	/** 成本金额 */
	@Column(nullable = false, precision = 21, scale = 6)
	private BigDecimal cost;

	/** 响应码 */
	private String respCode;

	/** 响应信息 */
	private String respMsg;

	/** 应答时间 */
	private Date respTime;

	/** 受理号 */
	private String busiRefNo;

	/** 绑定的会员 */
	@NotNull
	@ManyToOne
	@JoinColumn(nullable = false)
	private Member member;

	public BigDecimal getPrfAmount() {
		return getAmount().subtract(getFee());
	}

	// ===========================================getter/setter===========================================//

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getFee() {
		return fee;
	}

	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}

	public BigDecimal getCost() {
		return cost;
	}

	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}

	public String getRespCode() {
		return respCode;
	}

	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}

	public String getRespMsg() {
		return respMsg;
	}

	public void setRespMsg(String respMsg) {
		this.respMsg = respMsg;
	}

	public String getBusiRefNo() {
		return busiRefNo;
	}

	public void setBusiRefNo(String busiRefNo) {
		this.busiRefNo = busiRefNo;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getProdId() {
		return prodId;
	}

	public void setProdId(String prodId) {
		this.prodId = prodId;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public Date getRespTime() {
		return respTime;
	}

	public void setRespTime(Date respTime) {
		this.respTime = respTime;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}
	
}
