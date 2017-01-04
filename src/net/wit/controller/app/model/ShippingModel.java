package net.wit.controller.app.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import net.wit.entity.Shipping;
import net.wit.entity.ShippingMethod;
import net.wit.entity.ShippingMethod.Method;

public class ShippingModel extends BaseModel {
	
	/** 编号 */
	private Long id;
	
	/** 编号 */
	private String sn;

	/** 发货地 */
	private String deliveryCenterName;
	
	/** 配送方式 */
	private String shippingMethod;

	/** 物流公司 */
	private String deliveryCorp;

	/** 物流公司网址 */
	private String deliveryCorpUrl;

	/** 物流公司代码 */
	private String deliveryCorpCode;

	/** 运单号 */
	private String trackingNo;

	/** 物流费用 */
	private BigDecimal freight;

	/** 收货人 */
	private String consignee;

	/** 地区 */
	private String area;

	/** 地址 */
	private String address;

	/** 邮编 */
	private String zipCode;

	/** 电话 */
	private String phone;

	/** 操作员 */
	private String operator;

	/** 备注 */
	private String memo;
	
	/** 发货单项 */
	private List<ShippingItemModel> shippingItems;
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getShippingMethod() {
		return shippingMethod;
	}

	public void setShippingMethod(String shippingMethod) {
		this.shippingMethod = shippingMethod;
	}

	public String getDeliveryCorp() {
		return deliveryCorp;
	}

	public void setDeliveryCorp(String deliveryCorp) {
		this.deliveryCorp = deliveryCorp;
	}

	public String getDeliveryCorpUrl() {
		return deliveryCorpUrl;
	}

	public void setDeliveryCorpUrl(String deliveryCorpUrl) {
		this.deliveryCorpUrl = deliveryCorpUrl;
	}

	public String getDeliveryCorpCode() {
		return deliveryCorpCode;
	}

	public void setDeliveryCorpCode(String deliveryCorpCode) {
		this.deliveryCorpCode = deliveryCorpCode;
	}

	public String getTrackingNo() {
		return trackingNo;
	}

	public void setTrackingNo(String trackingNo) {
		this.trackingNo = trackingNo;
	}

	public BigDecimal getFreight() {
		return freight;
	}

	public void setFreight(BigDecimal freight) {
		this.freight = freight;
	}

	public String getConsignee() {
		return consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
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
	
	public List<ShippingItemModel> getShippingItems() {
		return shippingItems;
	}

	public void setShippingItems(List<ShippingItemModel> shippingItems) {
		this.shippingItems = shippingItems;
	}

	public String getDeliveryCenterName() {
		return deliveryCenterName;
	}

	public void setDeliveryCenterName(String deliveryCenterName) {
		this.deliveryCenterName = deliveryCenterName;
	}

	public void copyFrom(Shipping shipping) {
		this.id = shipping.getId();
		if (shipping.getDeliveryCenter()!=null) {
		    this.deliveryCenterName = shipping.getDeliveryCenter().getName();
		}
		this.address = shipping.getAddress();
		this.area = shipping.getArea();
		this.consignee = shipping.getConsignee();
		this.deliveryCorp = shipping.getDeliveryCorp();
		this.deliveryCorpCode = shipping.getDeliveryCorpCode();
		this.deliveryCorpUrl = shipping.getDeliveryCorpUrl();
		this.freight = shipping.getFreight();
		this.memo = shipping.getMemo();
		this.operator = shipping.getOperator();
		this.phone = shipping.getPhone();
		this.shippingMethod = shipping.getShippingMethod();
		this.sn = shipping.getSn();
		this.trackingNo = shipping.getTrackingNo();
		this.zipCode = shipping.getZipCode();
		this.shippingItems = ShippingItemModel.bindData(shipping.getShippingItems());
	}
	
	public static List<ShippingModel> bindData(List<Shipping> shippings) {
		List<ShippingModel> models = new ArrayList<ShippingModel>();
		for (Shipping shipping:shippings) {
			ShippingModel model = new ShippingModel();
			model.copyFrom(shipping);
			models.add(model);
		}
		return models;
	}
	
}
