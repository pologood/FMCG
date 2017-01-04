package net.wit.controller.wap.model;

import net.wit.controller.app.model.BaseModel;
import net.wit.entity.Coupon;
import net.wit.entity.CouponCode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CouponCodeModel extends BaseModel {
	private String thumbnail;
	private String logo;
	private String tenantName;
	private Date startDate;
	private Date endDate;
	private BigDecimal amount;
	private BigDecimal minimumPrice;
	private Boolean hasExpired;

	private Coupon.Type type;

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getTenantName() {
		return tenantName;
	}

	public void setTenantName(String tenantName) {
		this.tenantName = tenantName;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getMinimumPrice() {
		return minimumPrice;
	}

	public void setMinimumPrice(BigDecimal minimumPrice) {
		this.minimumPrice = minimumPrice;
	}

	public Boolean getHasExpired() {
		return hasExpired;
	}

	public void setHasExpired(Boolean hasExpired) {
		this.hasExpired = hasExpired;
	}

	public Boolean getGoingExpired() {
		return !getHasExpired()&&getEndDate().getTime()-new Date().getTime()<=86400000;
	}

	public Coupon.Type getType() {
		return type;
	}

	public void setType(Coupon.Type type) {
		this.type = type;
	}

	public void copyFrom(CouponCode couponCode) {
		Coupon coupon = couponCode.getCoupon();
		if(coupon.getTenant()!=null){
			this.thumbnail=coupon.getTenant().getThumbnail();
			this.logo=coupon.getTenant().getLogo();
			this.tenantName=coupon.getTenant().getName();
		}
		this.startDate=coupon.getStartDate();
		this.endDate=coupon.getEndDate();
		if(coupon.getType().equals(Coupon.Type.multipleCoupon)){
			this.amount=couponCode.getBalance();
		}else {
			this.amount=coupon.getAmount();
		}
		this.minimumPrice=coupon.getMinimumPrice();
		this.hasExpired=coupon.getExpired();
		this.type=couponCode.getCoupon().getType();
	}

	public static List<CouponCodeModel> bindData(List<CouponCode> couponCodes) {
		List<CouponCodeModel> models = new ArrayList<CouponCodeModel>();
		for (CouponCode couponCode:couponCodes) {
			CouponCodeModel model = new CouponCodeModel();
			model.copyFrom(couponCode);
			models.add(model);
		}
		return models;
	}
}
