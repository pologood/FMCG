/**
 * @Title：FinalOrderStatus.java 
 * @Package：net.wit.support 
 * @Description：
 * @author：Chenlf
 * @date：2015年5月10日 下午6:40:00 
 * @version：V1.0   
 */

package net.wit.support;

/**
 * @ClassName：FinalOrderStatus
 * @Description：
 * @author：Chenlf
 * @date：2015年5月10日 下午6:40:00
 */
public class FinalOrderStatus {

	public enum Status {
		/** 已过期 */
		isExpired,
		/** 已取消 */
		cancelled,
		/** 待评价 */
		toReview,
		/** 已完成 */
		completed,
		/** 待退货 */
		waitReturn,
		/** 待发货 */
		waitShipping,
		/** 待签收 */
		sign,
		/** 待付款 */
		waitPay,
		/** 部分发货 */
		partialShipment,
		/** 待确认*/
		unconfirmed

	}

	private Status status;// 状态

	private String desc;// 描述

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

}
