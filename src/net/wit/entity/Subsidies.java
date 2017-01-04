package net.wit.entity;

import com.google.gson.annotations.Expose;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.*;

/**
 * @ClassName: Subsidies
 * @Description: 奖励补贴
 * @author Administrator
 * @date 2014年10月14日 上午9:10:49
 */
@Entity
@Table(name = "xx_subsidies")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_subsidies_sequence")
public class Subsidies extends BaseEntity {

	private static final long serialVersionUID = -540706734359671149L;
	/** 结算状态 */
	public enum Type {
		/** 充值 - 收入 */
		recharge,
		/** 货款 - 收入 */
		receipts,
		/** 分润 - 收入 */
		profit,
		/** 其他 - 收入 */
		other
	}
	/** 是否入账 */
	private Type type;
	/** 备注信息 */
	private String remark;
	/** 模版信息 */
	private String Message;
	/** 奖励总人数 */
	private Integer count;
 	/** 奖励总金额 */
	private BigDecimal amount;
	/** 补贴项 */
	@Expose
	@Valid
	@NotEmpty
	@OneToMany(mappedBy = "subsidies", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<SubsidiesItem> subsidiesItems = new ArrayList<SubsidiesItem>();
	
	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public List<SubsidiesItem> getSubsidiesItems() {
		return subsidiesItems;
	}

	public void setSubsidiesItems(List<SubsidiesItem> subsidiesItems) {
		this.subsidiesItems = subsidiesItems;
	}

	public String getMessage() {
		return Message;
	}

	public void setMessage(String message) {
		Message = message;
	}
}
