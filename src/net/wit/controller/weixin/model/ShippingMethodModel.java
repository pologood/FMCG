package net.wit.controller.weixin.model;

import net.wit.controller.app.model.BaseModel;
import net.wit.entity.ShippingMethod;
import net.wit.entity.ShippingMethod.Method;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ShippingMethodModel extends BaseModel {
	
	/*ID*/
	private Long id;
	/*名称*/
	private String name;
	/*方式*/
	private Method method;
	
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
	
	public Method getMethod() {
		return method;
	}
	public void setMethod(Method method) {
		this.method = method;
	}
	
	public void copyFrom(ShippingMethod shippingMethod) {
		this.id = shippingMethod.getId();
		this.name = shippingMethod.getName();
		this.method = shippingMethod.getMethod();
	}
	
	public static List<ShippingMethodModel> bindData(List<ShippingMethod> shippingMethods) {
		List<ShippingMethodModel> models = new ArrayList<ShippingMethodModel>();
		for (ShippingMethod shippingMethod:shippingMethods) {
			ShippingMethodModel model = new ShippingMethodModel();
			model.copyFrom(shippingMethod);
			models.add(model);
		}
		return models;
	}
	public static Set<ShippingMethodModel> bindData(Set<ShippingMethod> shippingMethods) {
		Set<ShippingMethodModel> models = new HashSet<ShippingMethodModel>(shippingMethods.size());
		for (ShippingMethod shippingMethod:shippingMethods) {
			ShippingMethodModel model = new ShippingMethodModel();
			model.copyFrom(shippingMethod);
			models.add(model);
		}
		return models;
	}
	
	
}
