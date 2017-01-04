package net.wit.controller.assistant.model;

import net.wit.entity.Payment;
import net.wit.entity.Payment.Status;
import net.wit.entity.Payment.Type;

import java.math.BigDecimal;
import java.util.Date;

public class PaymentDetailsModel extends BaseModel {
	/*ID*/
	private Long id;
	//支付状态
	private Status status;
	//支付方式
	private String paymentMethod;
	//单号
	private String sn;
	//付款金额
	private BigDecimal amount;
	//付款人
	private String payer;
	//创建时间
	private Date paymentDate;
	//商户号
	private String tenantName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
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

	public String getPayer() {
		return payer;
	}

	public void setPayer(String payer) {
		this.payer = payer;
	}

	public Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}

	public String getTenantName() {
		return tenantName;
	}

	public void setTenantName(String tenantName) {
		this.tenantName = tenantName;
	}

	public void copyFrom(Payment payment) {
		this.id = payment.getId();
		this.amount = payment.getAmount();
		this.paymentMethod = payment.getPaymentMethod();
		this.sn = payment.getSn();
		this.status = payment.getStatus();
		this.payer = payment.getPayer();
		this.paymentDate = payment.getPaymentDate();
	}
	
}
