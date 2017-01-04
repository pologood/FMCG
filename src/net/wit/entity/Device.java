package net.wit.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;

/**
 * Entity - 大华设备
 * @author rsico Team
 * @version 3.0
 */
@Entity
@Table(name = "xx_device")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_device_sequence")
public class Device extends BaseEntity {

	private static final long serialVersionUID = -5234652157151649662L;

	/** 设备id */
	@Length(max = 255)
	private String equipId;

	/** 门店 */
	@ManyToOne(fetch = FetchType.LAZY)
	private DeliveryCenter deliveryCenter;

	/** 激活码 */
	@Length(max = 255)
	private String verification;

	/** 标题 */
	@Length(max = 255)
	private String name;

	/** 地址 */
	@Length(max = 255)
	private String  url;

	// ===========================================getter/setter===========================================//
	/**
	 * 获取设备id
	 * @return 设备id
	 */
	public String getEquipId() {
		return equipId;
	}

	public void setEquipId(String equipId) {
		this.equipId = equipId;
	}

	/**
	 * 获取门店
	 * @return 门店
	 */
	public DeliveryCenter getDeliveryCenter() {
		return deliveryCenter;
	}

	public void setDeliveryCenter(DeliveryCenter deliveryCenter) {
		this.deliveryCenter = deliveryCenter;
	}

	/**
	 * 获取激活码
	 * @return 激活码
	 */
	public String getVerification() {
		return verification;
	}

	public void setVerification(String verification) {
		this.verification = verification;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	
}
