 package net.wit.controller.weixin.model;

import net.wit.entity.Deposit;
import net.wit.entity.Deposit.Status;
import net.wit.entity.Deposit.Type;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    /*备注*/
    private String memo;
    /*时间*/
    private String create_date;


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

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public void copyFrom(Deposit deposit) {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        this.id = deposit.getId();
        this.balance = deposit.getBalance();
        this.credit = deposit.getCredit();
        this.debit = deposit.getDebit();
        this.memo = deposit.getMemo();
        this.status = deposit.getStatus();
        this.type = deposit.getType();
        if(deposit.getCreateDate()!=null){
            this.create_date = sdf.format(deposit.getCreateDate());
        }
    }

    public static List<DepositModel> bindData(List<Deposit> deposits) {
        List<DepositModel> models = new ArrayList<>();
        for (Deposit deposit : deposits) {
            DepositModel model = new DepositModel();
            model.copyFrom(deposit);
            models.add(model);
        }
        return models;
    }

}
