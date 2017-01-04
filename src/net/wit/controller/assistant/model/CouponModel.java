package net.wit.controller.assistant.model;

import net.wit.controller.assistant.model.BaseModel;
import net.wit.entity.Coupon;
import net.wit.entity.Coupon.Status;
import net.wit.entity.Coupon.Type;
import net.wit.entity.CouponCode;
import net.wit.entity.Member;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CouponModel extends BaseModel {
	
	/*ID*/
	private Long id;
	/*类型*/
	private Type type;
	/*名称*/
	private String name;
	/*面值*/
	private BigDecimal amount;
	/** 有效日期 */
	private Date startDate;
	
	/** 有效日期 */
	private Date endDate;
	/** 最小商品限额 */
	private BigDecimal minimumPrice;
	/** 是否启用 */
	private Boolean isEnabled;
	/** 可领取次数 */
	private Long receiveTimes;
	/** 已领次数 */
	private Long hasReceiveTimes;
	/** 已使用 */
	private Integer usedCount;
	/** 已发放 */
	private Integer sendCount;
	
	/** 总数 */
	private Integer count;
	
	/** 状态 */
	private Status status;
	
	/** 描述 */
	private String introduction;

	/** 是否已领完 */
	private Boolean hasReceived;
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
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
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
	public BigDecimal getMinimumPrice() {
		return minimumPrice;
	}
	public void setMinimumPrice(BigDecimal minimumPrice) {
		this.minimumPrice = minimumPrice;
	}
	public Boolean getIsEnabled() {
		return isEnabled;
	}
	public void setIsEnabled(Boolean isEnabled) {
		this.isEnabled = isEnabled;
	}
	public Long getReceiveTimes() {
		return receiveTimes;
	}
	public void setReceiveTimes(Long receiveTimes) {
		this.receiveTimes = receiveTimes;
	}

	public Long getHasReceiveTimes() {
		return hasReceiveTimes;
	}

	public void setHasReceiveTimes(Long hasReceiveTimes) {
		this.hasReceiveTimes = hasReceiveTimes;
	}

	public Integer getUsedCount() {
		return usedCount;
	}
	public void setUsedCount(Integer usedCount) {
		this.usedCount = usedCount;
	}
	public Integer getSendCount() {
		return sendCount;
	}
	public void setSendCount(Integer sendCount) {
		this.sendCount = sendCount;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}		
	public String getIntroduction() {
		return introduction;
	}
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public Boolean getHasReceived() {
		return hasReceived;
	}

	public void setHasReceived(Boolean hasReceived) {
		this.hasReceived = hasReceived;
	}

	public void copyFrom(Coupon coupon) {
		this.id = coupon.getId();
		this.name = coupon.getName();
		this.amount = coupon.getAmount();
		this.count = coupon.getCount()-coupon.getSendCount();
		this.endDate = coupon.getEndDate();
		this.isEnabled = coupon.getIsEnabled();
		this.minimumPrice = coupon.getMinimumPrice();
		this.receiveTimes = coupon.getReceiveTimes();
		this.hasReceiveTimes=0L;
		this.sendCount = coupon.getSendCount();
		this.startDate = coupon.getStartDate();
		this.status = coupon.getStatus();
		this.type = coupon.getType();
		this.usedCount = coupon.getUsedCount();
	    this.introduction = coupon.getIntroduction();
		this.hasReceived=false;
	}
	
	public static List<CouponModel> bindData(List<Coupon> coupons) {
		List<CouponModel> models = new ArrayList<CouponModel>();
		for (Coupon coupon:coupons) {
			CouponModel model = new CouponModel();
			model.copyFrom(coupon);
			models.add(model);
		}
		return models;
	}

	public static List<CouponModel> bindData(List<Coupon> coupons, Member member) {
		List<CouponModel> models = new ArrayList<>();
		for (Coupon coupon:coupons) {
			CouponModel model = new CouponModel();
			model.copyFrom(coupon);
			if(member!=null){
				Long hasReceiveTimes=0L;
				for(CouponCode couponCode:coupon.getCouponCodes()){
					if(couponCode.getMember()==member){
						hasReceiveTimes++;
					}
				}
				model.setHasReceiveTimes(hasReceiveTimes);
				if(hasReceiveTimes>=coupon.getReceiveTimes()){
					model.setHasReceived(true);
				}
			}
			models.add(model);
		}
		return models;
	}
}
