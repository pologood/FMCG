package net.wit.entity;

import com.google.gson.annotations.Expose;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.Valid;
import java.math.BigDecimal;

/**
 * @ClassName: SubsidiesItem
 * @Description: 奖励补贴项
 * @author Administrator
 * @date 2014年10月14日 上午9:10:49
 */
@Entity
@Table(name = "xx_subsidies_item")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_subsidies_item_sequence")
public class SubsidiesItem extends BaseEntity {

	private static final long serialVersionUID = -540706734359671149L;

	/** 入帐状态 */
	public enum Status {
		/** 已入账 */
		yes,
		/** 未入账 */
		no,
		/** 失败 */
		fail
	}
	/** 是否入账 */
	private Status status;
	/** 店铺名称 */
	private String tenantName;
	/** 补贴用户名称 */
	private String username;
 	/** 补贴金额 */
	private BigDecimal amount;
	/** 失败原因 */
	private String failReason;
	/** 补贴 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="subsidiess",nullable = false, updatable = false)
	private Subsidies subsidies;

	
	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getTenantName() {
		return tenantName;
	}

	public void setTenantName(String tenantName) {
		this.tenantName = tenantName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFailReason() {
		return failReason;
	}

	public void setFailReason(String failReason) {
		this.failReason = failReason;
	}

	public Subsidies getSubsidies() {
		return subsidies;
	}

	public void setSubsidies(Subsidies subsidies) {
		this.subsidies = subsidies;
	}
}
