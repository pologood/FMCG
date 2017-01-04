package net.wit.controller.assistant.model;

import net.wit.controller.assistant.model.AreaModel;
import net.wit.controller.assistant.model.BaseModel;
import net.wit.entity.Receiver;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ReceiverModel extends BaseModel {

	/** id */
	private Long id;

	/** 联系人 */
	private String consignee;

	/** 地区名称 */
	private AreaModel area;

	/** 地址 */
	private String address;

	/** 邮编 */
	private String zipCode;

	/** 电话 */
	private String phone;

	/** 是否默认 */
	private Boolean isDefault;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public AreaModel getArea() {
		return area;
	}

	public void setArea(AreaModel area) {
		this.area = area;
	}

	public String getConsignee() {
		return consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
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

	public Boolean getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}

	public void copyFrom(Receiver receiver) {
		this.id = receiver.getId();
		AreaModel areaModel = new AreaModel();
		areaModel.copyFrom(receiver.getArea());
		this.area = areaModel; 
		this.consignee = receiver.getConsignee();
		this.address = receiver.getAddress();
		this.phone = receiver.getPhone();
		this.zipCode = receiver.getZipCode();
		this.isDefault = receiver.getIsDefault();
	}
	
	public static Set<ReceiverModel> bindData(Set<Receiver> receivers) {
		Set<ReceiverModel> models = new HashSet<ReceiverModel>(receivers.size());
		for (Receiver receiver:receivers) {
			ReceiverModel model = new ReceiverModel();
			model.copyFrom(receiver);
			models.add(model);
		}
		return models;
	}

	public static List<ReceiverModel> bindData(List<Receiver> receivers) {
		List<ReceiverModel> models=new ArrayList<>();
		for (Receiver receiver:receivers) {
			ReceiverModel model = new ReceiverModel();
			model.copyFrom(receiver);
			models.add(model);
		}
		return models;
	}
	
}
