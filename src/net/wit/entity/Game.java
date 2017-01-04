package net.wit.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * @ClassName: Game
 * @Description: 游戏快充
 * @author Administrator
 * @date 2014年10月14日 上午10:18:43
 */
@Entity
@Table(name = "xx_game")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_game_sequence")
public class Game extends BaseEntity {

	private static final long serialVersionUID = -141736723497251149L;

	/** 终端号 */
	@Column(nullable = false)
	private String termNo;

	/** 商户号 */
	@Column(nullable = false)
	private String merNo;

	/** 请求时间 */
	@Column(nullable = false)
	private String reqTime;

	/** 订单号 */
	@Column(nullable = false)
	private String sn;

	/** 账号 */
	private String account;

	/** 服务 */
	private String server;

	/** 区域 */
	private String area;

	/** 游戏类型 */
	private int cardId;

	/** 面值 */
	private int priceId;

	/** 数量 */
	private int chargeCount;

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
	private String retTime;

	/** 交易结果码 */
	private String retCode;

	/** 交易错误信息 */
	private String retMsg;

	/** 绑定的会员 */
	@NotNull
	@ManyToOne
	@JoinColumn(nullable = false)
	private Member member;

	public BigDecimal getPrfAmount() {
		return getAmount().subtract(getFee());
	}

	// ===========================================getter/setter===========================================//
	public String getTermNo() {
		return termNo;
	}

	public void setTermNo(String termNo) {
		this.termNo = termNo;
	}

	public String getMerNo() {
		return merNo;
	}

	public void setMerNo(String merNo) {
		this.merNo = merNo;
	}

	public String getReqTime() {
		return reqTime;
	}

	public void setReqTime(String reqTime) {
		this.reqTime = reqTime;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getAccount() {
		return account;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public int getCardId() {
		return cardId;
	}

	public void setCardId(int cardId) {
		this.cardId = cardId;
	}

	public int getPriceId() {
		return priceId;
	}

	public void setPriceId(int priceId) {
		this.priceId = priceId;
	}

	public int getChargeCount() {
		return chargeCount;
	}

	public void setChargeCount(int chargeCount) {
		this.chargeCount = chargeCount;
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

	public String getRetCode() {
		return retCode;
	}

	public String getRetTime() {
		return retTime;
	}

	public void setRetTime(String retTime) {
		this.retTime = retTime;
	}

	public void setRetCode(String retCode) {
		this.retCode = retCode;
	}

	public String getRetMsg() {
		return retMsg;
	}

	public void setRetMsg(String retMsg) {
		this.retMsg = retMsg;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

}
