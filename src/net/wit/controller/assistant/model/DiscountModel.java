package net.wit.controller.assistant.model;

import net.wit.entity.CouponCode;
import net.wit.entity.model.BaseModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DiscountModel extends BaseModel {

	/** 买赠 单品活动*/
	private String buyfree;
	/** 秒杀、限时抢购 单品活动 */
	private String seckill;
	/** 满额折扣  商家活动  */
	private String discount;
	/** 满额包邮  商家活动 */
	private String mail;
	/** 积分兑换 */
	private String points;
	/** 送优惠券 */
	private String coupon;
	/** 活动立减（平台主办） */
	private String activity;

	public String getBuyfree() {
		return buyfree;
	}

	public void setBuyfree(String buyfree) {
		this.buyfree = buyfree;
	}

	public String getSeckill() {
		return seckill;
	}

	public void setSeckill(String seckill) {
		this.seckill = seckill;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getPoints() {
		return points;
	}

	public void setPoints(String points) {
		this.points = points;
	}

	public String getCoupon() {
		return coupon;
	}

	public void setCoupon(String coupon) {
		this.coupon = coupon;
	}

	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}
}
