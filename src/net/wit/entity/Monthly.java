package net.wit.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Min;

/**
 * @ClassName: monthly
 * @Description: 月结账
 * @author Administrator
 * @date 2014年10月14日 上午9:10:49
 */
@Entity
@Table(name = "xx_monthly")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_monthly_sequence")
public class Monthly extends BaseEntity {

	private static final long serialVersionUID = -541766724349671849L;

	/**
     * 账户余额
     */
    @Min(0)
    @Column(nullable = false, precision = 27, scale = 12, columnDefinition = "decimal default 0")
    private BigDecimal balance;
    
	/**
     * 冻结余额
     */
    @Min(0)
    @Column(nullable = false, precision = 27, scale = 12, columnDefinition = "decimal default 0")
    private BigDecimal freezeBalance;
    
	/**
     * 待发货余额
     */
    @Min(0)
    @Column(nullable = false, precision = 27, scale = 12, columnDefinition = "decimal default 0")
    private BigDecimal unShippingBalance;


	/** 商家 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Tenant tenant;



	/** 库存 */
	@Expose
	@JsonProperty
	@Min(0)
	private Integer stock;

	/**锁定库存 */
	@Expose
	@JsonProperty
	@Min(0)
	private Integer lockStock;

	/**
	 *库存金额
	 */
	@Min(0)
	@Column(nullable = false, precision = 27, scale = 12, columnDefinition = "decimal default 0")
	private BigDecimal stockAmount;


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


	public BigDecimal getUnShippingBalance() {
		return unShippingBalance;
	}


	public void setUnShippingBalance(BigDecimal unShippingBalance) {
		this.unShippingBalance = unShippingBalance;
	}


	public Tenant getTenant() {
		return tenant;
	}


	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public Integer getLockStock() {
		return lockStock;
	}

	public void setLockStock(Integer lockStock) {
		this.lockStock = lockStock;
	}

	public BigDecimal getStockAmount() {
		return stockAmount;
	}

	public void setStockAmount(BigDecimal stockAmount) {
		this.stockAmount = stockAmount;
	}

	
}
