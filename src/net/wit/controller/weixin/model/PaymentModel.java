package net.wit.controller.weixin.model;

import net.wit.entity.Payment;
import net.wit.entity.Payment.Status;
import net.wit.entity.Payment.Type;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;

public class PaymentModel extends BaseModel {
    /**
     * ID
     */
    private Long id;
    /**
     * 支付单类型
     */
    private Type type;
    /**
     * 支付状态
     */
    private Status status;
    /**
     * 支付方式
     */
    private String paymentMethod;
    /**
     * 备注
     */
    private String memo;
    /**
     * 支付单号
     */
    private String sn;
    /**
     * 付款类型
     */
    private BigDecimal amount;
    /**
     * 支付手续费
     */
    private BigDecimal fee;
    /**
     * 收款商家
     */
    private String tenantName;
    /**
     * 创建时间
     */
    private String createDate;

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

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public void copyFrom(Payment payment) {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.id = payment.getId();
        this.type = payment.getType();
        this.amount = payment.getAmount();
        this.fee = payment.getFee();
        this.memo = payment.getMemo();
        this.paymentMethod = payment.getPaymentMethod();
        this.sn = payment.getSn();
        this.status = payment.getStatus();
        this.tenantName=payment.getTenantName();
        if(payment.getCreateDate()!=null){
            this.createDate=sdf.format(payment.getCreateDate());
        }
    }

}
