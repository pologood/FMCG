/**
 *====================================================
 * 文件名称: BankInfo.java
 * 修订记录：
 * No    日期				作者(操作:具体内容)
 * 1.    2014年9月4日			Administrator(创建:创建文件)
 *====================================================
 * 类描述：(说明未实现或其它不应生成javadoc的内容)
 * 
 */
package net.wit.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @ClassName: BankInfo
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author Administrator
 * @date 2014年9月4日 下午5:26:38
 */
@Entity
@Table(name = "xx_bank_info")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_bank_info_sequence")
public class BankInfo extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/** 银行卡号开头 */
	@JsonProperty
	@NotBlank
	@Column(nullable = false)
	private String cardNoHead;

	/** 银行代码 */
	@JsonProperty
	@NotBlank
	@Column(nullable = false, updatable = false, length = 12)
	private String bankCode;
	
	/** 开户行名 */
	@JsonProperty
	@NotBlank
	@Column(nullable = false, updatable = false, length = 64)
	private String depositBank;
	
	/** 支持编号 */
	@JsonProperty
	@NotBlank
	@Column(nullable = false, updatable = false, length = 3)
	private String flag;

	/** 银行Logo */
	@JsonProperty
	private String logo;

	/** 背景颜色 */
	@JsonProperty
	private String bkgColor;

	// ===========================================getter/setter===========================================//
	/**
	 * 银行卡号开头
	 * @return 银行卡号开头
	 */
	public String getCardNoHead() {
		return cardNoHead;
	}

	/**
	 * 银行卡号开头
	 * @param type 银行卡号开头
	 */
	public void setCardNoHead(String cardNoHead) {
		this.cardNoHead = cardNoHead;
	}

	/**
	 * 卡号
	 * @return 卡号
	 */
	public String getDepositBank() {
		return depositBank;
	}

	/**
	 * 卡号
	 * @param type 卡号
	 */
	public void setDepositBank(String depositBank) {
		this.depositBank = depositBank;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getBkgColor() {
		return bkgColor;
	}

	public void setBkgColor(String bkgColor) {
		this.bkgColor = bkgColor;
	}
}
