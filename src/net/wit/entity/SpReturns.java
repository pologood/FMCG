/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fr.third.org.apache.poi.util.SystemOutLogger;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @ClassName: Returns
 * @Description: 退货申请单
 * @author Administrator
 * @date 2014年10月14日 上午9:11:12
 */
@Entity
@Table(name = "xx_sp_returns")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_sp_returns_sequence")
public class SpReturns extends BaseEntity {

	private static final long serialVersionUID = -8019074120457087232L;

	/** 售后查询状态 */
	public enum  QuerySpReStatus {
		/** 全部订单 */
		all,
		/** 已申请 */
		applyed,
		/** 已退货 */
		returned,
		/** 退货中 */
		refundApply,
		/** 退款/维修 */
		refunded,

	}
	/** 退货状态 */
	public enum ReturnStatus {
		/** 未确认 */
		unconfirmed,
		/** 已确认 */
		confirmed,
		/** 已审核 */
	    audited,
		/** 已完成 */
		completed,
		/** 已取消 */
		cancelled
	}

	/** 退货状态 */
	public enum Type {
		/** 正常退货 */
		normal,
		/** 事件退货 */
		event
	}
	
	/** 编号 */
	@Column(nullable = false, updatable = false, unique = true, length = 100)
	private String sn;
	
	/** 编号 */
	@Column(nullable = false)
	private ReturnStatus  returnStatus;

	/** 编号 */
	@Column(nullable = false)
	private Type  type;
	
	/** 配送方式 */
	private String shippingMethod;

	/** 物流公司 */
	private String deliveryCorp;

	/** 运单号 */
	@Length(max = 200)
	private String trackingNo;

	/** 物流费用 */
	@Min(0)
	@Digits(integer = 12, fraction = 3)
	private BigDecimal freight;

	/** 退货金额，含运费 */
	@Min(0)
	@Digits(integer = 12, fraction = 3)
	private BigDecimal amount;

	/** 结算金额，含运费 */
	@Min(0)
	@Digits(integer = 12, fraction = 3)
	private BigDecimal cost;

	/** 发货人 */
	@NotEmpty
	@Length(max = 200)
	private String shipper;

	/** 地区 */
	@NotEmpty
	private String area;

	/** 地址 */
	@NotEmpty
	@Length(max = 200)
	private String address;

	/** 邮编 */
	@NotEmpty
	@Length(max = 200)
	private String zipCode;

	/** 电话 */
	@NotEmpty
	@Length(max = 200)
	private String phone;

	/** 操作员 */
	private String operator;

	/** 备注 */
	@Length(max = 200)
	private String memo;
	
	/** 完成日期 */
	private Date completedDate;
	
	/** 子订单 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, updatable = false)
	private Trade trade;
	
	/** 供应商 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(updatable = false)
	private Tenant supplier;

	/** 结算到供应商状态 */
	private Boolean suppliered;

	/** 结算到供应商时间 */
	private Date supplierDate;

	/** 打印时间 */
	private Date printDate;
	/** 打印次数 */
	private Integer print;

	/** 退货项 */
	@Valid
	@NotEmpty
	@OneToMany(mappedBy = "returns", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<SpReturnsItem> returnsItems = new ArrayList<SpReturnsItem>();

	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
	
	public BigDecimal getCost() {
		return cost;
	}
	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}
	
	public Date getCompletedDate() {
		return completedDate;
	}
	public void setCompletedDate(Date completedDate) {
		this.completedDate = completedDate;
	}
	
	/** 获取数量 */
	public int getQuantity() {
		int quantity = 0;
		if (getReturnsItems() != null) {
			for (SpReturnsItem returnsItem : getReturnsItems()) {
				if (returnsItem != null && returnsItem.getReturnQuantity() != null) {
					quantity += returnsItem.getReturnQuantity();
				}
			}
		}
		return quantity;
	}

	/** 获取商品金额 */
	@JsonProperty
	public BigDecimal calcAmount(Tenant supplier) {
		BigDecimal cost = BigDecimal.ZERO;
		Set<SpReturnsItem> spReturnsItems = new HashSet<SpReturnsItem>();
			for (SpReturnsItem spReturnsItem : getReturnsItems(supplier)) {
				if (spReturnsItem.getOrderItem() != null && spReturnsItem.getOrderItem().getCost() != null) {
					cost = cost.add(spReturnsItem.getOrderItem().getPrice().multiply(new BigDecimal(spReturnsItem.getReturnQuantity())));
				}
			}
		return cost;
	}

	/** 获取商品成本 */
	@JsonProperty
	public BigDecimal calcCost(Tenant supplier) {
		BigDecimal cost = BigDecimal.ZERO;
		Set<SpReturnsItem> spReturnsItems = new HashSet<SpReturnsItem>();
			for (SpReturnsItem spReturnsItem : getReturnsItems(supplier)) {
				if (spReturnsItem.getOrderItem() != null && spReturnsItem.getOrderItem().getCost() != null) {
					cost = cost.add(spReturnsItem.getOrderItem().getCost().multiply(new BigDecimal(spReturnsItem.getReturnQuantity())));
				}
			}
		return cost;
	}
	
	/** 获取商品成本 */
	@JsonProperty
	public BigDecimal calcOrderItemCost(Tenant supplier) {
		BigDecimal cost = BigDecimal.ZERO;
		Set<SpReturnsItem> spReturnsItems = new HashSet<SpReturnsItem>();
			for (SpReturnsItem spReturnsItem : getReturnsItems(supplier)) {
				if (spReturnsItem.getOrderItem() != null && spReturnsItem.getOrderItem().getCost() != null) {
					cost = cost.add(spReturnsItem.getOrderItem().getCost().multiply(new BigDecimal(spReturnsItem.getOrderItem().getQuantity())));
				}
			}
		return cost;
	}

	/** 获取商品成本 */
	@JsonProperty
	public BigDecimal calcCost() {
		BigDecimal cost = BigDecimal.ZERO;
		Set<SpReturnsItem> spReturnsItems = new HashSet<SpReturnsItem>();
			for (SpReturnsItem spReturnsItem : getReturnsItems()) {
				if (spReturnsItem.getOrderItem() != null && spReturnsItem.getOrderItem().getCost() != null) {
					cost = cost.add(spReturnsItem.getOrderItem().getCost().multiply(new BigDecimal(spReturnsItem.getReturnQuantity())));
				}
			}
		return cost;
	}
	/** 获取退货单的结算金额 */
    @JsonProperty
    public BigDecimal getSettle() {
        BigDecimal cost = BigDecimal.ZERO;
        for (SpReturnsItem spReturnsItem : getReturnsItems()) {
            if (spReturnsItem.getOrderItem() != null && spReturnsItem.getOrderItem().getCost() != null) {
                cost = cost.add(spReturnsItem.getOrderItem().getCost().multiply(new BigDecimal(spReturnsItem.getOrderItem().getReturnQuantity().intValue())));
            }
        }
        return cost;
    }
    /** 获取退货单的退货金额 */
    @JsonProperty
    public BigDecimal getTotal() {
        BigDecimal cost = BigDecimal.ZERO;
        for (SpReturnsItem spReturnsItem : getReturnsItems()) {
            if (spReturnsItem.getOrderItem() != null && spReturnsItem.getOrderItem().getPrice() != null) {
                cost = cost.add(spReturnsItem.getOrderItem().getPrice().multiply(new BigDecimal(spReturnsItem.getReturnQuantity().intValue())));
            }
        }
        return cost;
    }
	// ===========================================getter/setter===========================================//
	/**
	 * 获取编号
	 * @return 编号
	 */
	public String getSn() {
		return sn;
	}

	/**
	 * 设置编号
	 * @param sn 编号
	 */
	public void setSn(String sn) {
		this.sn = sn;
	}

	/**
	 * 获取配送方式
	 * @return 配送方式
	 */
	public String getShippingMethod() {
		return shippingMethod;
	}

	/**
	 * 设置配送方式
	 * @param shippingMethod 配送方式
	 */
	public void setShippingMethod(String shippingMethod) {
		this.shippingMethod = shippingMethod;
	}

	/**
	 * 获取物流公司
	 * @return 物流公司
	 */
	public String getDeliveryCorp() {
		return deliveryCorp;
	}

	/**
	 * 设置物流公司
	 * @param deliveryCorp 物流公司
	 */
	public void setDeliveryCorp(String deliveryCorp) {
		this.deliveryCorp = deliveryCorp;
	}

	/**
	 * 获取运单号
	 * @return 运单号
	 */
	public String getTrackingNo() {
		return trackingNo;
	}

	/**
	 * 设置运单号
	 * @param trackingNo 运单号
	 */
	public void setTrackingNo(String trackingNo) {
		this.trackingNo = trackingNo;
	}

	/**
	 * 获取物流费用
	 * @return 物流费用
	 */
	public BigDecimal getFreight() {
		return freight;
	}

	/**
	 * 设置物流费用
	 * @param freight 物流费用
	 */
	public void setFreight(BigDecimal freight) {
		this.freight = freight;
	}

	/**
	 * 获取发货人
	 * @return 发货人
	 */
	public String getShipper() {
		return shipper;
	}

	/**
	 * 设置发货人
	 * @param shipper 发货人
	 */
	public void setShipper(String shipper) {
		this.shipper = shipper;
	}

	/**
	 * 获取地区
	 * @return 地区
	 */
	public String getArea() {
		return area;
	}

	/**
	 * 设置地区
	 * @param area 地区
	 */
	public void setArea(String area) {
		this.area = area;
	}

	/**
	 * 获取地址
	 * @return 地址
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * 设置地址
	 * @param address 地址
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * 获取邮编
	 * @return 邮编
	 */
	public String getZipCode() {
		return zipCode;
	}

	/**
	 * 设置邮编
	 * @param zipCode 邮编
	 */
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	/**
	 * 获取电话
	 * @return 电话
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * 设置电话
	 * @param phone 电话
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * 获取操作员
	 * @return 操作员
	 */
	public String getOperator() {
		return operator;
	}

	/**
	 * 设置操作员
	 * @param operator 操作员
	 */
	public void setOperator(String operator) {
		this.operator = operator;
	}

	/**
	 * 获取备注
	 * @return 备注
	 */
	public String getMemo() {
		return memo;
	}

	/**
	 * 设置备注
	 * @param memo 备注
	 */
	public void setMemo(String memo) {
		this.memo = memo;
	}

	/**
	 * 获取子订单
	 * @return 子订单
	 */
	public Trade getTrade() {
		return trade;
	}

	/**
	 * 设置子订单
	 * @param trade 子订单
	 */
	public void setTrade(Trade trade) {
		this.trade = trade;
	}

	public ReturnStatus getReturnStatus() {
		return returnStatus;
	}

	public void setReturnStatus(ReturnStatus returnStatus) {
		this.returnStatus = returnStatus;
	}

	public List<SpReturnsItem> getReturnsItems() {
		return returnsItems;
	}

	public void setReturnsItems(List<SpReturnsItem> returnsItems) {
		this.returnsItems = returnsItems;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public Tenant getSupplier() {
		return supplier;
	}
	public void setSupplier(Tenant supplier) {
		this.supplier = supplier;
	}


	/** 获取供应商 */
	public Set<Tenant> getSuppliers() {
		Set<Tenant> tenants = new HashSet<Tenant>();
		for (SpReturnsItem spReturnsItem : getReturnsItems()) {
			if (!tenants.contains(spReturnsItem.getOrderItem().getSupplier())) {
				tenants.add(spReturnsItem.getOrderItem().getSupplier());
			}
		}
		return tenants;
	}
	
	/**
	 * 获取商家购物车项
	 * @param tenant 商家
	 * @return 商家购物车项
	 */
	@JsonProperty
	public Set<SpReturnsItem> getReturnsItems(Tenant supplier) {
		Set<SpReturnsItem> spReturnsItems = new HashSet<SpReturnsItem>();
		if (supplier != null && getReturnsItems() != null) {
			for (SpReturnsItem spReturnsItem : getReturnsItems()) {
				if (spReturnsItem != null && spReturnsItem.getOrderItem() != null && spReturnsItem.getOrderItem().getSupplier().equals(supplier)) {
					spReturnsItems.add(spReturnsItem);
				}
			}
		}
		return spReturnsItems;
	}
	public  Integer getReturnQuantity() {
		Integer quantity = 0;
			for (SpReturnsItem spReturnsItem : getReturnsItems()) {
				quantity = quantity + spReturnsItem.getReturnQuantity();
			}
		return quantity;
	}
    public  Integer getShippedQuantity() {
        Integer quantity = 0;
        for (SpReturnsItem spReturnsItem : getReturnsItems()) {
            quantity = quantity + spReturnsItem.getShippedQuantity();
        }
        return quantity;
    }
	public Boolean getSuppliered() {
		return suppliered;
	}
	public void setSuppliered(Boolean suppliered) {
		this.suppliered = suppliered;
	}
	public Date getSupplierDate() {
		return supplierDate;
	}
	public void setSupplierDate(Date supplierDate) {
		this.supplierDate = supplierDate;
	}

	public Date getPrintDate() {
		return printDate;
	}

	public void setPrintDate(Date printDate) {
		this.printDate = printDate;
	}

	public Integer getPrint() {
		return print;
	}

	public void setPrint(Integer print) {
		this.print = print;
	}
}