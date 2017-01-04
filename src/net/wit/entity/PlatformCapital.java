package net.wit.entity;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Entity - 平台概况
 * @author rsico Team
 * @version 3.0
 */
@Entity
@Table(name = "xx_platform_capital")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_platform_capital_sequence")
public class PlatformCapital extends BaseEntity{
    private static final long serialVersionUID = 1L;

    /** 收款 */
    private BigDecimal gathering;

    /** 提现 */
    private BigDecimal withdrawCash;

    /** 手续费 */
    private BigDecimal fee;

    /** 佣金 */
    private BigDecimal brokerage;

    public BigDecimal getGathering() {
        return gathering;
    }

    public void setGathering(BigDecimal gathering) {
        this.gathering = gathering;
    }

    public BigDecimal getWithdrawCash() {
        return withdrawCash;
    }

    public void setWithdrawCash(BigDecimal withdrawCash) {
        this.withdrawCash = withdrawCash;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public BigDecimal getBrokerage() {
        return brokerage;
    }

    public void setBrokerage(BigDecimal brokerage) {
        this.brokerage = brokerage;
    }
}
