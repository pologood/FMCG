/**
 *====================================================
 * 文件名称: HServer.java
 * 修订记录：
 * No    日期				作者(操作:具体内容)
 * 1.    2014年5月9日			Administrator(创建:创建文件)
 *====================================================
 * 类描述：(说明未实现或其它不应生成javadoc的内容)
 * 
 */
package net.wit.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

/**
 * @ClassName: equipment
 * @Description: 投放设备
 * @author Administrator
 * @date 2014年5月9日 下午3:26:44
 */
@Entity
@Table(name = "xx_equipment")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_equipment_sequence")
public class Equipment extends BaseEntity {

	private static final long serialVersionUID = 1L;
	
	/** 操作系统 */
	public enum OperatingSystem {
		andriod,
		ios,
		windows
	}

	/** 状态 */
	public enum Status {
		/** 待投放  */
		none,
		/** 已启动 */
		enabled,
		/** 已停用 */
		disable
	}
	
	/** 设备号 */
	private String uuid;


	/** 操作系统 */
	@NotNull
	private OperatingSystem operatingSystem;

	/** 状态 */
	@NotNull
	private Status status;
	
	/** 投资商家 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Tenant tenant;
	
	/** 投放商家 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Tenant store;
	
	/** 投放门店 */
	@ManyToOne(fetch = FetchType.LAZY)
	private  DeliveryCenter deliveryCenter;

	/** 投放门店 */
	@ManyToOne(fetch = FetchType.LAZY)
	private  Union unions;

	/** 押金 */
	@Column(nullable = false, precision = 21, scale = 6)
	private BigDecimal deposit;

	/** 年费 */
	@Column(nullable = false, precision = 21, scale = 6)
	private BigDecimal fee;
	
	/** 备注 */
	@Length(max = 200)
	private String memo;

	/** 累计佣金 */
	@NotNull
	@Digits(integer = 12, fraction = 3)
	@Column(nullable = false, precision = 21, scale = 6, columnDefinition = "decimal default 0")
	private BigDecimal totalAmount;

	/** 累计销售 */
	@NotNull
	@Digits(integer = 12, fraction = 3)
	@Column(nullable = false, precision = 21, scale = 6, columnDefinition = "decimal default 0")
	private BigDecimal totalSaleAmount;

	public OperatingSystem getOperatingSystem() {
		return operatingSystem;
	}

	public void setOperatingSystem(OperatingSystem operatingSystem) {
		this.operatingSystem = operatingSystem;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Tenant getTenant() {
		return tenant;
	}

	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
	}

	public Tenant getStore() {
		return store;
	}

	public void setStore(Tenant store) {
		this.store = store;
	}

	public BigDecimal getDeposit() {
		return deposit;
	}

	public void setDeposit(BigDecimal deposit) {
		this.deposit = deposit;
	}

	public BigDecimal getFee() {
		return fee;
	}

	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public DeliveryCenter getDeliveryCenter() {
		return deliveryCenter;
	}

	public void setDeliveryCenter(DeliveryCenter deliveryCenter) {
		this.deliveryCenter = deliveryCenter;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public BigDecimal getTotalSaleAmount() {
		return totalSaleAmount;
	}

	public void setTotalSaleAmount(BigDecimal totalSaleAmount) {
		this.totalSaleAmount = totalSaleAmount;
	}

	public Union getUnions() {
		return unions;
	}

	public void setUnions(Union unions) {
		this.unions = unions;
	}
}