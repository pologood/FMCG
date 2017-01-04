package net.wit.controller.assistant.model;

import net.wit.controller.assistant.model.BaseModel;
import net.wit.entity.Payment;
import net.wit.entity.Payment.Status;
import net.wit.entity.Payment.Type;

import java.math.BigDecimal;

public class PaymentModel extends BaseModel {
	/*ID*/
	private Long id;
	private Type type;
	private Status status;
	private String paymentMethod;
	private String memo;
	private String sn;
	private BigDecimal amount;
	private BigDecimal fee;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
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

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
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

	public BigDecimal getFee() {
		return fee;
	}

	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}

	public void copyFrom(Payment payment) {
		this.id = payment.getId();
		this.type = payment.getType();
		this.amount = payment.getAmount();
		this.fee = payment.getFee();
		this.memo = payment.getMemo();
		this.paymentMethod = payment.getPaymentMethod();
		this.sn = payment.getSn();
		this.status = payment.getStatus();
	}
	
}
