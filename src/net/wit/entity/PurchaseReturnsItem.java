package net.wit.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;
import net.wit.BigDecimalNumericFieldBridge;
import org.hibernate.search.annotations.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 采购退货单
 * Created by My-PC on 16/06/02.
 */
@Entity
@Table(name = "xx_purchase_returns_item")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_purchase_returns_item_sequence")
public class PurchaseReturnsItem extends BaseEntity {

    private static final long serialVersionUID = 2267830430439593293L;

    /** 商品编号 */
    @NotEmpty
    @Column(nullable = false, updatable = false)
    private String sn;

    /** 商品名称 */
    @NotEmpty
    @Column(nullable = false, updatable = false)
    private String name;

    /** 商品条形码 */
    @Column(updatable = false)
    private String barcode;

    /** 销售价 */
    @Expose
    @JsonProperty
    @Field(store = Store.YES, index = Index.UN_TOKENIZED)
    @NumericField
    @FieldBridge(impl = BigDecimalNumericFieldBridge.class)
    @NotNull
    @Min(0)
    @Digits(integer = 12, fraction = 3)
    @Column(nullable = false, precision = 21, scale = 6)
    private BigDecimal price;

    /** 商品型号 */
    @NotEmpty
    @Column(nullable = true, updatable = false)
    private String spec;

    /** 商品规格 */
    @NotEmpty
    @Column(nullable = true, updatable = false)
    private String model;

    /** 数量 */
    @NotNull
    @Min(1)
    @Column(nullable = false, updatable = false)
    private Integer quantity;

    /** 备注*/
    @Length(max = 200)
    @Column(updatable = false)
    private String memo;

    /** 采购单 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, updatable = false)
    private PurchaseReturns purchaseReturns;

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public PurchaseReturns getPurchaseReturns() {
        return purchaseReturns;
    }

    public void setPurchaseReturns(PurchaseReturns purchaseReturns) {
        this.purchaseReturns = purchaseReturns;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
