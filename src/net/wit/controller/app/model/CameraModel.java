package net.wit.controller.app.model;

import java.util.*;

import net.wit.entity.Camera;
import net.wit.entity.DeliveryCenter;
import net.wit.entity.Tenant;

public class CameraModel extends BaseModel {
	private Long id;
	private String device;
	private String name;
	private SingleModel tenant;
	private SingleModel deliveryCenter;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
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

	public SingleModel getDeliveryCenter() {
		return deliveryCenter;
	}

	public void setDeliveryCenter(SingleModel deliveryCenter) {
		this.deliveryCenter = deliveryCenter;
	}

	public void copyFrom(Camera camera) {
		this.id = camera.getId();
		this.name = camera.getName();
		this.device = camera.getDevice();
		SingleModel tenantModel = new SingleModel();
		Tenant tenant = camera.getTenant();
		if (tenant!=null) {
			tenantModel.setId(tenant.getId());
			tenantModel.setName(tenant.getName());
		}
		this.tenant = tenantModel;
		SingleModel deliveryCenterModel = new SingleModel();
		DeliveryCenter deliveryCenter = camera.getDeliveryCenter();
		if (deliveryCenter!=null) {
			tenantModel.setId(deliveryCenter.getId());
			tenantModel.setName(deliveryCenter.getName());
		}
		this.deliveryCenter = deliveryCenterModel;
	}

	public static List<CameraModel> bindData(List<Camera> cameras) {
		List<CameraModel> models = new ArrayList<CameraModel>();
		for (Camera camera : cameras) {
			CameraModel model = new CameraModel();
			model.copyFrom(camera);
			models.add(model);
		}
		return models;
	}
	
}
