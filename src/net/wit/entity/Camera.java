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

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

/**
 * @ClassName: camera
 * @Description: 摄头像
 * @author Administrator
 * @date 2014年5月9日 下午3:26:44
 */
@Entity
@Table(name = "xx_camera")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_camera_sequence")
public class Camera extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/** 别名 */
	@NotNull
	@Length(max = 100)
	private String name;
	
	/** 设备号 */
	@NotNull
	@Length(max = 100)
	private String device;
	
	/** 所属门店 */
	@ManyToOne(fetch = FetchType.LAZY)
	private  Tenant tenant;
	
	/** 所属门店 */
	@ManyToOne(fetch = FetchType.LAZY)
	private  DeliveryCenter deliveryCenter;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	public DeliveryCenter getDeliveryCenter() {
		return deliveryCenter;
	}

	public void setDeliveryCenter(DeliveryCenter deliveryCenter) {
		this.deliveryCenter = deliveryCenter;
	}

	public Tenant getTenant() {
		return tenant;
	}

	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
	}
	

}
