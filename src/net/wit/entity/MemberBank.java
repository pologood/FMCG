/**
 *====================================================
 * 文件名称: MemberBank.java
 * 修订记录：
 * No    日期				作者(操作:具体内容)
 * 1.    2014年7月29日			Administrator(创建:创建文件)
 *====================================================
 * 类描述：(说明未实现或其它不应生成javadoc的内容)
 * 
 */
package net.wit.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

import net.wit.util.Base64Util;

/**
 * @ClassName: MemberBank
 * @Description: 会员银行卡
 * @author Administrator
 * @date 2014年7月29日 下午3:15:46
 */
@Entity
@Table(name = "xx_member_bank")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_member_bank_sequence")
public class MemberBank extends BaseEntity {

	private static final long serialVersionUID = -2331598645835706164L;

	/** 银行卡类型 */
	public static enum Type {
		/** 信用卡 */
		credit,
		/** 借记卡 */
		debit
	}
	
	/** 银行卡用途 */
	public static enum Flag {
		/** 支付 */
		payment,
		/** 提现 */
		cashier
	}

	/** 银行卡类型 */
	@Column(nullable = false)
	private Type type;
	
	/** 银行卡用途 */
	@Column(nullable = false)
	private Flag flag;
	
	/** 卡号 */
	@NotBlank
	@Column(nullable = false,length = 64)
	private String cardNo;

		/** 开户行名 */
	@NotBlank
	@Column(nullable = false,length = 100)
	private String depositBank;

	/** 开户名 */
	@NotBlank
	@JsonProperty
	@Column(nullable = false,length = 100)
	private String depositUser;

	/** 开通令牌 */
	private String token;
	
	/** 会员信息 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Member member;

	/** 银行信息 */
	@ManyToOne(fetch = FetchType.LAZY)
	private BankInfo bankInfo;
	
	// ===========================================getter/setter===========================================//
	
	/**
	 * 银行卡类型
	 * @return 银行卡类型
	 */
	public Type getType() {
		return type;
	}

	/**
	 * 银行卡类型
	 * @param type 银行卡类型
	 */
	public void setType(Type type) {
		this.type = type;
	}
	
	public Flag getFlag() {
		return flag;
	}

	public void setFlag(Flag flag) {
		this.flag = flag;
	}

	/**
	 * 卡号
	 * @return 卡号
	 */
	public String getCardNo() {
		return Base64Util.decode(this.cardNo);
	}

	/**
	 * 卡号
	 * @param cardNo 卡号
	 */
	public void setCardNo(String cardNo) {
		this.cardNo = Base64Util.encode(cardNo);
	}

	/**
	 * 开户行
	 * @return 开户行
	 */
	public String getDepositBank() {
		return depositBank;
	}

	/**
	 * 开户行
	 * @param depositBank 开户行
	 */
	public void setDepositBank(String depositBank) {
		this.depositBank = depositBank;
	}

	/**
	 * 会员信息
	 * @return 会员信息
	 */
	public Member getMember() {
		return member;
	}

	/**
	 * 会员信息
	 * @param member 会员信息
	 */
	public void setMember(Member member) {
		this.member = member;
	}

	/**
	 * 开户行
	 * @return 开户行
	 */
	public String getDepositUser() {
		return depositUser;
	}

	public void setDepositUser(String depositUser) {
		this.depositUser = depositUser;
	}

	public String getToken() {
		return Base64Util.decode(this.token);
	}

	public void setToken(String token) {
		this.token = Base64Util.encode(token);
	}

	public BankInfo getBankInfo() {
		return bankInfo;
	}

	public void setBankInfo(BankInfo bankInfo) {
		this.bankInfo = bankInfo;
	}
	
}
