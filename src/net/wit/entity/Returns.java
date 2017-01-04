/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * @ClassName: Returns
 * @Description: 退货单
 * @author Administrator
 * @date 2014年10月14日 上午9:11:12
 */
@Entity
@Table(name = "xx_returns")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_returns_sequence")
public class Returns extends BaseEntity {

	private static final long serialVersionUID = -8019074120457087212L;

	/** 编号 */
	@Column(nullable = false, updatable = false, unique = true, length = 100)
	private String sn;

	/** 配送方式 */
	@Column(updatable = false)
	private String shippingMethod;

	/** 物流公司 */
	@Column(updatable = false)
	private String deliveryCorp;

	/** 运单号 */
	@Length(max = 200)
	@Column(updatable = false)
	private String trackingNo;

	/** 物流费用 */
	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(updatable = false, precision = 21, scale = 6)
	private BigDecimal freight;

	/** 发货人 */
	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false, updatable = false)
	private String shipper;

	/** 地区 */
	@NotEmpty
	@Column(nullable = false, updatable = false)
	private String area;

	/** 地址 */
	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false, updatable = false)
	private String address;

	/** 邮编 */
	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false, updatable = false)
	private String zipCode;

	/** 电话 */
	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false, updatable = false)
	private String phone;

	/** 操作员 */
	@Column(nullable = false, updatable = false)
	private String operator;

	/** 备注 */
	@Length(max = 200)
	@Column(updatable = false)
	private String memo;

	/** 订单 */
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "orders", nullable = false, updatable = false)
	private Order order;

	/** 子订单 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Trade trade;

	/** 发货地 */
	@ManyToOne(fetch = FetchType.LAZY)
	private DeliveryCenter deliveryCenter;

	/** 退货项 */
	@Valid
	@NotEmpty
	@OneToMany(mappedBy = "returns", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<ReturnsItem> returnsItems = new ArrayList<ReturnsItem>();

	/** 获取数量 */
	public int getQuantity() {
		int quantity = 0;
		if (getReturnsItems() != null) {
			for (ReturnsItem returnsItem : getReturnsItems()) {
				if (returnsItem != null && returnsItem.getQuantity() != null) {
					quantity += returnsItem.getQuantity();
				}
			}
		}
		return quantity;
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
	 * 获取订单
	 * @return 订单
	 */
	public Order getOrder() {
		return order;
	}

	/**
	 * 设置订单
	 * @param order 订单
	 */
	public void setOrder(Order order) {
		this.order = order;
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

	/**
	 * 获取发货地
	 * @return 发货地
	 */
	public DeliveryCenter getDeliveryCenter() {
		return deliveryCenter;
	}

	/**
	 * 设置发货地
	 * @param deliveryCenter 发货地
	 */
	public void setDeliveryCenter(DeliveryCenter deliveryCenter) {
		this.deliveryCenter = deliveryCenter;
	}

	/**
	 * 获取退货项
	 * @return 退货项
	 */
	public List<ReturnsItem> getReturnsItems() {
		return returnsItems;
	}

	/**
	 * 设置退货项
	 * @param returnsItems 退货项
	 */
	public void setReturnsItems(List<ReturnsItem> returnsItems) {
		this.returnsItems = returnsItems;
	}

}