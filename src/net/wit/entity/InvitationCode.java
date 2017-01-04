package net.wit.entity;

import com.google.gson.annotations.Expose;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by WangChao on 2016-1-11.
 */
@Entity
@Table(name = "xx_invitation_code")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_invitation_code_sequence")
public class InvitationCode extends BaseEntity {

    private static final long serialVersionUID = -541766727343251441L;
    /**
     * 邀请码
     */
    @Column(nullable = false, updatable = false, unique = true, length = 100)
    private String code;

    /**
     * 价格
     */
    @Expose
    @Column(nullable = false, precision = 27, scale = 12)
    private BigDecimal price;

    /**
     * 会员
     */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin", nullable = false)
    private Admin admin;

    /**
     * 结束日期
     */
    private Date endDate;

    /**
     * 备注
     */
    @Expose
    private String remark;

    // ===========================================getter/setter===========================================//
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
