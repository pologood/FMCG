package net.wit.entity;

import com.google.gson.annotations.Expose;
import org.hibernate.search.annotations.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 采购单
 * Created by My-PC on 16/06/02.
 */
@Entity
@Table(name = "xx_purchase_returns")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_purchase_returns_sequence")
public class PurchaseReturns extends BaseEntity {

    private static final long serialVersionUID = 2167830430439593203L;

    /**
     * 采购状态
     */
    public enum Type{
        /***/
        applied,
        /***/
        outStorage
    }

    /** 编号 */
    @Column(nullable = false, updatable = false, unique = true, length = 100)
    private String sn;

    private Type type;

    /** 采购商 */
    @ManyToOne(fetch = FetchType.LAZY)
    private Tenant tenant;

    /** 供应商 */
    @ManyToOne(fetch = FetchType.LAZY)
    private Tenant supplier;

    /** 采购日期 */
    @Expose
    @Field(store = Store.YES, index = Index.UN_TOKENIZED)
    @DateBridge(resolution = Resolution.SECOND)
    @Column(nullable = false, updatable = false)
    private Date purchaseDate;

    /** 操作员 */
    @Column(nullable = false, updatable = false)
    private String operator;

    /** 备注 */
    @Length(max = 200)
    @Column(updatable = false)
    private String memo;

    /** 采购清单 */
    @Valid
    @NotEmpty
    @OneToMany(mappedBy = "purchaseReturns", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<PurchaseReturnsItem> purchaseItems = new ArrayList<PurchaseReturnsItem>();

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public Tenant getSupplier() {
        return supplier;
    }

    public void setSupplier(Tenant supplier) {
        this.supplier = supplier;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public List<PurchaseReturnsItem> getPurchaseItems() {
        return purchaseItems;
    }

    public void setPurchaseItems(List<PurchaseReturnsItem> purchaseItems) {
        this.purchaseItems = purchaseItems;
    }
}
