package net.wit.controller.app.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import net.wit.entity.Deposit;
import net.wit.entity.Deposit.Status;
import net.wit.entity.Deposit.Type;
import net.wit.entity.Payment;

public class DepositModel extends BaseModel {
	/*等级*/
	private Long id;
	/*类型*/
	private Type type;
	/*状态*/
	private Status status;
	/*收入*/
	private BigDecimal credit;
	/*支出*/
	private BigDecimal debit;
	/*结余*/
	private BigDecimal balance;
	/*备注 */
	private String memo;
	/*时间 */
	private Date create_date;
	

	
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

	public BigDecimal getCredit() {
		return credit;
	}

	public void setCredit(BigDecimal credit) {
		this.credit = credit;
	}

	public BigDecimal getDebit() {
		return debit;
	}

	public void setDebit(BigDecimal debit) {
		this.debit = debit;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Date getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}

	public void copyFrom(Deposit deposit) {
		this.id = deposit.getId();
		this.balance = deposit.getBalance();
		this.credit = deposit.getCredit();
		this.debit = deposit.getDebit();
		this.memo = deposit.getMemo();
		this.status = deposit.getStatus();
		this.type = deposit.getType();
		this.create_date = deposit.getCreateDate();
	}
	
	public static List<DepositModel> bindData(List<Deposit> deposits) {
		List<DepositModel> models = new ArrayList<DepositModel>();
		for (Deposit deposit:deposits) {
			DepositModel model = new DepositModel();
			model.copyFrom(deposit);
			models.add(model);
		}
		return models;
	}
		
}
