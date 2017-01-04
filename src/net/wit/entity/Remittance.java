package net.wit.entity;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 汇款单
 * Created by hujun on 16/7/5.
 */
@Entity
@Table(name = "xx_remittance")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_remittance_sequence")
public class Remittance extends BaseEntity {
    private static final long serialVersionUID = -5053540156564638635L;

    /**收款单*/
    @OneToOne(fetch = FetchType.LAZY)
    private Credit xCredit;

    /** 汇款款银行 */
    @Length(max = 200)
    @Column(nullable = false)
    private String bankName;

    /** 汇款账号 */
    @Length(max = 200)
    @Column(nullable = false)
    private String bankCode;

    /** 汇款金额*/
    @Length(max = 200)
    @Column(nullable = false)
    private BigDecimal amount;

    /** 凭证流水 */
    @Length(max = 200)
    @Column(nullable = false)
    private String documentFlow;

    /**经办人  */
    @Column(nullable = false)
    private String acntToName;

    /** 付款日期 */
    @Column(nullable = false)
    private Date remittanceDate;

    /** 备注 */
    @Length(max = 200)
    private String memo;

    public Credit getCredit() {
        return xCredit;
    }

    public void setCredit(Credit credit) {
        this.xCredit = credit;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDocumentFlow() {
        return documentFlow;
    }

    public void setDocumentFlow(String documentFlow) {
        this.documentFlow = documentFlow;
    }

    public String getAcntToName() {
        return acntToName;
    }

    public void setAcntToName(String acntToName) {
        this.acntToName = acntToName;
    }

    public Date getRemittanceDate() {
        return remittanceDate;
    }

    public void setRemittanceDate(Date remittanceDate) {
        this.remittanceDate = remittanceDate;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
