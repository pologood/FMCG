package net.wit.entity.model;

import java.math.BigDecimal;
import java.util.Date;

public class CouponSumerModel extends BaseModel {
	
	/** 类型 */
	public enum Type {
		/** 领取情况 */
		send,
		/** 使用情况 */
		used,
	}

	/*领用时间*/
	private Date sumerDate;
	/*张数*/
	private Integer sumerCount;
	/*人数*/
	private Integer sumerNumber;
	public Date getSumerDate() {
		return sumerDate;
	}
	public void setSumerDate(Date sumerDate) {
		this.sumerDate = sumerDate;
	}
	public Integer getSumerCount() {
		return sumerCount;
	}
	public void setSumerCount(Integer sumerCount) {
		this.sumerCount = sumerCount;
	}
	public Integer getSumerNumber() {
		return sumerNumber;
	}
	public void setSumerNumber(Integer sumerNumber) {
		this.sumerNumber = sumerNumber;
	}

}
