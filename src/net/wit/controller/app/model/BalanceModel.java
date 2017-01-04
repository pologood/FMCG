package net.wit.controller.app.model;

import java.math.BigDecimal;

import net.wit.entity.Credit.Method;
import net.wit.entity.Member;
import net.wit.util.SettingUtils;

public class BalanceModel extends BaseModel {
	
	/*等级*/
	private Long id;
	/*余额*/
	private BigDecimal balance;
	/*冷结余额*/
	private BigDecimal freezeBalance;
	/*可提现金额*/
	private BigDecimal withdrawBalance;		
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public BigDecimal getFreezeBalance() {
		return freezeBalance;
	}

	public void setFreezeBalance(BigDecimal freezeBalance) {
		this.freezeBalance = freezeBalance;
	}

	public BigDecimal getWithdrawBalance() {
		return withdrawBalance;
	}

	public void setWithdrawBalance(BigDecimal withdrawBalance) {
		this.withdrawBalance = withdrawBalance;
	}

	/**
	 * 计算支付手续费
	 * @param amount 金额
	 * @return 支付手续费
	 */
	public BigDecimal calcFeeTemp(Member member, Method method, BigDecimal amount) {
		BigDecimal fee = new BigDecimal(0);
		if (method == Method.immediately) {
			fee = amount.multiply(member.getBaseWithdrawCashScale().add(SettingUtils.get().getWithdrawCashAddScale()));
		}
		if (method == Method.fast) {
			fee = amount.multiply(member.getBaseWithdrawCashScale());
		}
		if (method == Method.general) {
			fee = amount.multiply(member.getBaseWithdrawCashScale());
		}
		return fee.setScale(2, BigDecimal.ROUND_DOWN);
	}
	
	public void copyFrom(Member member) {
		this.id = member.getId();
		this.balance = member.getBalance();
		this.freezeBalance = member.getFreezeBalance();
		this.withdrawBalance = member.getBalance().subtract(member.getFreezeCashBalance());
	}
		
}
