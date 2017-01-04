package net.wit.controller.assistant.model;

import net.wit.entity.CouponCode;
import net.wit.entity.model.BaseModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CouponSumerModel extends BaseModel {

	/*ID*/
	private Long id;
	/*名称*/
	private String name;
	private String headImg;
	private BigDecimal amount;
	private Date createDate;
	private Date userDate;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		amount = amount;
	}

	public String getHeadImg() {
		return headImg;
	}

	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}

	public Date getUserDate() {
		return userDate;
	}

	public void setUserDate(Date userDate) {
		this.userDate = userDate;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public void copyFrom(CouponCode couponCode) {
		this.id = couponCode.getId();
		this.name = couponCode.getMember().getName();
		this.headImg=couponCode.getMember().getHeadImg();
		this.amount = couponCode.getCoupon().getAmount();
		this.createDate = couponCode.getCreateDate();
		this.userDate = couponCode.getUsedDate();
	}

	public static List<CouponSumerModel> bindData(List<CouponCode> couponCodes) {
		List<CouponSumerModel> models = new ArrayList<CouponSumerModel>();
		for (CouponCode couponCode:couponCodes) {
			if (couponCode.getCoupon()!=null) {
				CouponSumerModel model = new CouponSumerModel();
				model.copyFrom(couponCode);
				models.add(model);
			}
		}
		return models;
	}
}
