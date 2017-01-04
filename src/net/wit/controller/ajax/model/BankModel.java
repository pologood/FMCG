/**
 *====================================================
 * 文件名称: BankModel.java
 * 修订记录：
 * No    日期				作者(操作:具体内容)
 * 1.    2014-9-5			Administrator(创建:创建文件)
 *====================================================
 * 类描述：(说明未实现或其它不应生成javadoc的内容)
 * 
 */
package net.wit.controller.ajax.model;

import java.util.Date;

import net.wit.entity.MemberBank.Type;

/**
 * @ClassName: BankModel
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author Administrator
 * @date 2014-9-5 下午1:58:15
 */
public class BankModel extends BaseModel{

	/** 银行卡类型 */
	private Type type;

	/** 卡号 */
	private String cardNo;

	/** 有效期 */
	private Date validity;

	/** 还款日期--信用卡 */
	private Integer repaymentDay;

	/** 开户行 */
	private String depositBank;
	
	/** 开户名 */
	private String depositUser;

	public String getDepositUser() {
		return depositUser;
	}

	public void setDepositUser(String depositUser) {
		this.depositUser = depositUser;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public Date getValidity() {
		return validity;
	}

	public void setValidity(Date validity) {
		this.validity = validity;
	}

	public Integer getRepaymentDay() {
		return repaymentDay;
	}

	public void setRepaymentDay(Integer repaymentDay) {
		this.repaymentDay = repaymentDay;
	}

	public String getDepositBank() {
		return depositBank;
	}

	public void setDepositBank(String depositBank) {
		this.depositBank = depositBank;
	}


}
