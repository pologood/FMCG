package net.wit.controller.assistant.model;

import net.wit.controller.assistant.model.BaseModel;
import net.wit.entity.DeliveryCorp;

import java.util.ArrayList;
import java.util.List;

public class DeliveryCorpModel extends BaseModel {
	
	/**ID**/
	private Long id;

	/** 名称 */
	private String name;


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
	public void copyFrom(DeliveryCorp deliveryCorp) {
		this.id = deliveryCorp.getId();
		this.name = deliveryCorp.getName();
		
	}
	public static List<DeliveryCorpModel> bindData(List<DeliveryCorp> deliveryCorps) {
		List<DeliveryCorpModel> models = new ArrayList<DeliveryCorpModel>();
		for (DeliveryCorp deliveryCorp:deliveryCorps) {
			DeliveryCorpModel model = new DeliveryCorpModel();
			model.copyFrom(deliveryCorp);
			models.add(model);
		}
		return models;
	}
	
}
