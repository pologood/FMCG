package net.wit.controller.support;

import java.math.BigDecimal;
import java.util.List;

import net.wit.entity.Deposit;

public class DepositModel {
	//明细列表
	private List<Deposit> deposits;
	//支出金额
	private BigDecimal outAmount;
	//收入金额
	private BigDecimal inAmount;
	//当前月份
	private Integer month;
	
	public List<Deposit> getDeposits() {
		return deposits;
	}
	public void setDeposits(List<Deposit> deposits) {
		this.deposits = deposits;
	}
	public BigDecimal getOutAmount() {
		return outAmount;
	}
	public void setOutAmount(BigDecimal outAmount) {
		this.outAmount = outAmount;
	}
	public BigDecimal getInAmount() {
		return inAmount;
	}
	public void setInAmount(BigDecimal inAmount) {
		this.inAmount = inAmount;
	}
	public Integer getMonth() {
		return month;
	}
	public void setMonth(Integer month) {
		this.month = month;
	}
}
