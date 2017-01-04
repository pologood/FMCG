package net.wit.controller.app.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Store;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import net.wit.entity.Area;
import net.wit.entity.Community;
import net.wit.entity.DeliveryCenter;
import net.wit.entity.Location;
import net.wit.entity.Tenant;
import net.wit.util.MapUtils;

public class DeliveryCenterModel extends BaseModel {
	
	/**ID**/
	private Long id;
	/** 编码 */
	private String sn;

	/** 名称 */
	private String name;

	/** 联系人 */
	private String contact;

	/** 地区名称 */
	private AreaModel area;

	/** 地址 */
	private String address;

	/** 邮编 */
	private String zipCode;

	/** 电话 */
	private String phone;

	/** 手机 */
	private String mobile;

	/** 备注 */
	private String memo;

	/** 是否默认 */
	private Boolean isDefault;

	/** 地理位置 */
	private Location location;

	/** 我家店铺 */
	private SingleModel tenant;

	/** 距离 */
	private double distance;

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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public AreaModel getArea() {
		return area;
	}
	public void setArea(AreaModel area) {
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
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public Boolean getIsDefault() {
		return isDefault;
	}
	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	public SingleModel getTenant() {
		return tenant;
	}
	public void setTenant(SingleModel tenant) {
		this.tenant = tenant;
	}	
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	
	public void copyFrom(DeliveryCenter deliveryCenter) {
		this.id = deliveryCenter.getId();
		this.address = deliveryCenter.getAddress();
		AreaModel area = new AreaModel();
		area.copyFrom(deliveryCenter.getArea());
		this.area = area;
		this.contact = deliveryCenter.getContact();
		this.isDefault = deliveryCenter.getIsDefault();
		this.location = deliveryCenter.getLocation();
		this.memo = deliveryCenter.getMemo();
		this.mobile = deliveryCenter.getMobile();
		this.phone = deliveryCenter.getPhone();
		this.name = deliveryCenter.getName();
		this.sn = deliveryCenter.getSn();
		this.zipCode = deliveryCenter.getZipCode();
		SingleModel tenant = new SingleModel();
		tenant.setId(deliveryCenter.getId());
		tenant.setName(deliveryCenter.getName());
		this.tenant = tenant;
		if ((location!=null) && (deliveryCenter.getLocation()!=null)) {
			this.distance = MapUtils.getDistatce(location.getLat().doubleValue(),deliveryCenter.getLocation().getLat().doubleValue(),
					location.getLng().doubleValue(), deliveryCenter.getLocation().getLng().doubleValue());
		} else {
			this.distance = 0;
		}
		
	}
	
}
