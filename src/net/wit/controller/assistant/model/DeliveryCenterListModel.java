package net.wit.controller.assistant.model;

import net.wit.controller.assistant.model.AreaModel;
import net.wit.controller.assistant.model.BaseModel;
import net.wit.controller.assistant.model.SingleModel;
import net.wit.entity.DeliveryCenter;
import net.wit.entity.Location;
import net.wit.util.MapUtils;

import java.util.ArrayList;
import java.util.List;

public class DeliveryCenterListModel extends BaseModel {

	/**ID**/
	private Long id;
	/** 名称 */
	private String name;

	/** 所属店铺 */
	private SingleModel tenant;

	/** 地址 */
	private String address;

	/** 手机 */
	private String mobile;

	/** 是否默认 */
	private Boolean isDefault;

	/** 距离 */
	private double distance;

	/** 地理位置 */
	private Location location;

	/** 区域 */
	private AreaModel area;


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}

	public SingleModel getTenant() {
		return tenant;
	}


	public void setTenant(SingleModel tenant) {
		this.tenant = tenant;
	}


	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}


	public String getMobile() {
		return mobile;
	}


	public void setMobile(String mobile) {
		this.mobile = mobile;
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


	public double getDistance() {
		return distance;
	}


	public void setDistance(double distance) {
		this.distance = distance;
	}

	public AreaModel getArea() {
		return area;
	}

	public void setArea(AreaModel area) {
		this.area = area;
	}

	public void copyFrom(DeliveryCenter deliveryCenter, Location location) {
		this.id = deliveryCenter.getId();
		if(deliveryCenter.getAreaName()==null || deliveryCenter.getAddress()==null){
			this.address = "";
		}else{
			this.address = deliveryCenter.getAreaName().concat(deliveryCenter.getAddress());
		}
		this.isDefault = deliveryCenter.getIsDefault();
		this.location = deliveryCenter.getLocation();
		this.mobile = deliveryCenter.getMobile();
		this.name = deliveryCenter.getName();
		SingleModel tenant = new SingleModel();
		tenant.setId(deliveryCenter.getId());
		tenant.setName(deliveryCenter.getName());
		this.tenant = tenant;
		if ((location!=null && location.getLng()!=null && location.getLat()!=null) && (deliveryCenter.getLocation()!=null && deliveryCenter.getLocation().getLat()!=null && deliveryCenter.getLocation().getLng()!=null)) {
			this.distance = MapUtils.getDistatce(location.getLat().doubleValue(),deliveryCenter.getLocation().getLat().doubleValue(),
					location.getLng().doubleValue(), deliveryCenter.getLocation().getLng().doubleValue());
		} else {
			this.distance = 0;
		}
		AreaModel area = new AreaModel();
		area.copyFrom(deliveryCenter.getArea());
		this.area = area;
	}
	
	public static List<DeliveryCenterListModel> bindData(List<DeliveryCenter> deliveryCenters, Location location) {
		List<DeliveryCenterListModel> models = new ArrayList<DeliveryCenterListModel>();
		for (DeliveryCenter deliveryCenter:deliveryCenters) {
			DeliveryCenterListModel model = new DeliveryCenterListModel();
			model.copyFrom(deliveryCenter,location);
			models.add(model);
		}
		return models;
	}
}
