package net.wit.controller.assistant.model;

import net.wit.entity.PayBill;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PayBillModel extends BaseModel {
	/*ID*/
	private Long id;
	private PayBill.Type type;
	private SingleModel member;
	private String sn;
	private BigDecimal amount;
	private String memo;
	private Date createDate;
	private String des;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public PayBill.Type getType() {
		return type;
	}

	public void setType(PayBill.Type type) {
		this.type = type;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public SingleModel getMember() {
		return member;
	}

	public void setMember(SingleModel member) {
		this.member = member;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	public void copyFrom(PayBill payBill) {
		this.id = payBill.getId();
		this.type = payBill.getType();
		this.amount = payBill.getAmount();
		this.sn = payBill.getSn();
		SingleModel model =new SingleModel();
		if(payBill.getTenant()!=null){
			model.setId(payBill.getTenant().getId());
			model.setName(payBill.getTenant().getName());
			this.member = model;
			if(payBill.getType()==PayBill.Type.coupon){
				this.memo = payBill.getTenant().getName()+"-代金券";
				this.des = "代金券";
			}if(payBill.getType()==PayBill.Type.promotion){
				this.memo = payBill.getTenant().getName()+"-活动优惠";
				this.des = "活动优惠";
			}if(payBill.getType()==PayBill.Type.cashier){
				this.memo = payBill.getTenant().getName()+"-收银台";
				this.des = "收银台";
			}
		}else{
			if(payBill.getType()==PayBill.Type.coupon){
				this.memo = "代金券";
				this.des = "代金券";
			}if(payBill.getType()==PayBill.Type.promotion){
				this.memo = "活动优惠";
				this.des = "活动优惠";
			}if(payBill.getType()==PayBill.Type.cashier){
				this.memo = "收银台";
				this.des = "收银台";
			}
		}
		this.member = model;



		this.createDate = payBill.getCreateDate();

	}
	public static List<PayBillModel> bindData(List<PayBill> list) {
		List<PayBillModel> models = new ArrayList<PayBillModel>();
		for (PayBill payBill:list) {
			PayBillModel model = new PayBillModel();
			model.copyFrom(payBill);
			models.add(model);
		}
		return models;
	}
}
