package net.wit.controller.app.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import net.wit.entity.Ad;
import net.wit.entity.Area;
import net.wit.entity.Equipment;
import net.wit.entity.Product;
import net.wit.entity.Tenant;
import net.wit.entity.Equipment.Status;

public class EquipmentModel extends BaseModel {
	
	/** 状态 */
	private Status status;
	
	/** 供货商 */
	private SingleModel tenant;
	
	/** 零售商 */
	private SingleModel store;
	
	
	public Status getStatus() {
		return status;
	}


	public void setStatus(Status status) {
		this.status = status;
	}


	public SingleModel getTenant() {
		return tenant;
	}


	public void setTenant(SingleModel tenant) {
		this.tenant = tenant;
	}


	public SingleModel getStore() {
		return store;
	}


	public void setStore(SingleModel store) {
		this.store = store;
	}


	public void copyFrom(Equipment equipment) {
		SingleModel tenant = new SingleModel();
		if (equipment.getTenant()!=null) {
	   	   tenant.setId(equipment.getTenant().getId());
		   tenant.setName(equipment.getTenant().getName());
		}
		this.tenant = tenant;
		SingleModel store = new SingleModel();
		if (equipment.getStore()!=null) {
		   store.setId(equipment.getStore().getId());
		   store.setName(equipment.getStore().getName());
		}
		this.store = store;
		this.status = equipment.getStatus();
	}
		
}
