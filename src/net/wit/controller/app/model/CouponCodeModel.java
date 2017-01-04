package net.wit.controller.app.model;

import java.util.ArrayList;
import java.util.List;

import net.wit.entity.Coupon;
import net.wit.entity.CouponCode;

public class CouponCodeModel extends BaseModel {
	
	/*ID*/
	private Long id;
	/*名称*/
	private String name;
	private String code;
	private String tenantName;
	private String thumbnail;
	private CouponModel coupon;
		
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
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}

	public String getTenantName() {
		return tenantName;
	}

	public void setTenantName(String tenantName) {
		this.tenantName = tenantName;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public CouponModel getCoupon() {
		return coupon;
	}
	public void setCoupon(CouponModel coupon) {
		this.coupon = coupon;
	}
	
	public void copyFrom(CouponCode couponCode) {
		Coupon coupon = couponCode.getCoupon();
		this.id = coupon.getId();
		this.name = coupon.getName();
		this.tenantName=coupon.getTenant().getName();
		this.thumbnail=coupon.getTenant().getThumbnail();
		this.code = couponCode.getCode();
		CouponModel model = new CouponModel();
		model.copyFrom(coupon);
		this.coupon = model;
	}
	
	public static List<CouponCodeModel> bindData(List<CouponCode> couponCodes) {
		List<CouponCodeModel> models = new ArrayList<CouponCodeModel>();
		for (CouponCode couponCode:couponCodes) {
			if (couponCode.getCoupon()!=null) {
			   CouponCodeModel model = new CouponCodeModel();
			   model.copyFrom(couponCode);
			   models.add(model);
			}
		}
		return models;
	}
}
