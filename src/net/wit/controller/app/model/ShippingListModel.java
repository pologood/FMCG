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

public class ShippingListModel extends BaseModel {
	
	/** 编号 */
	private Long id;
	
	/** 编号 */
	private String sn;

	/** 物流公司 */
	private String deliveryCorp;

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

	/** 电话 */
	private String phone;

	
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


	public String getDeliveryCorp() {
		return deliveryCorp;
	}

	public void setDeliveryCorp(String deliveryCorp) {
		this.deliveryCorp = deliveryCorp;
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


	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void copyFrom(Shipping shipping) {
		this.id = shipping.getId();
		this.address = shipping.getAddress();
		this.area = shipping.getArea();
		this.consignee = shipping.getConsignee();
		this.deliveryCorp = shipping.getDeliveryCorp();
		this.freight = shipping.getFreight();
		this.phone = shipping.getPhone();
		this.sn = shipping.getSn();
		this.trackingNo = shipping.getTrackingNo();
	}
	
	public static List<ShippingListModel> bindData(List<Shipping> shippings) {
		List<ShippingListModel> models = new ArrayList<ShippingListModel>();
		for (Shipping shipping:shippings) {
			ShippingListModel model = new ShippingListModel();
			model.copyFrom(shipping);
			models.add(model);
		}
		return models;
	}
	
}
